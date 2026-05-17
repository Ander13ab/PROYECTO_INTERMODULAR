import { useEffect, useMemo, useState } from 'react';
import type { ReactNode } from 'react';
import {
  createMachine,
  deleteMachine,
  getAdminDashboardData,
  updateMachine,
} from '../services/adminDashboardService';
import type {
  AdminDashboardData,
  AttendanceSummary,
  GymClassSummary,
  MachineDraft,
  MachineSummary,
  MembershipFeeSummary,
  UserSummary,
} from '../types/admin';

type AdminSection = 'dashboard' | 'usuarios' | 'qr' | 'maquinas' | 'clases' | 'cuotas';

const EMPTY_MACHINE_DRAFT: MachineDraft = {
  advertenciaSeguridad: '',
  descripcion: '',
  estado: 'ACTIVA',
  grupoMuscular: '',
  imagenUrl: '',
  instrucciones: '',
  nivel: '',
  nombre: '',
};

function AdminSidebarItem({
  label,
  active = false,
  onClick,
}: {
  label: string;
  active?: boolean;
  onClick: () => void;
}) {
  return (
    <button
      className={`w-full rounded-2xl px-4 py-3 text-left text-sm transition ${
        active
          ? 'bg-[#22CC66] font-semibold text-[#0D2010]'
          : 'bg-[#133B21] text-[#D1FAE5]'
      }`}
      onClick={onClick}
      type="button"
    >
      {label}
    </button>
  );
}

function AdminMetric({
  label,
  value,
}: {
  label: string;
  value: string;
}) {
  return (
    <div className="rounded-[22px] border border-[#E7EAEE] bg-white p-6">
      <p className="text-sm text-[#667085]">{label}</p>
      <p className="mt-4 font-['Syne'] text-4xl font-extrabold text-[#0D2010]">
        {value}
      </p>
    </div>
  );
}

function AdminInfoCard({
  title,
  subtitle,
}: {
  title: string;
  subtitle: string;
}) {
  return (
    <div className="rounded-[20px] border border-[#EEF2F7] bg-white px-5 py-4">
      <p className="text-lg font-semibold text-[#0D2010]">{title}</p>
      <p className="mt-2 text-sm text-[#98A2B3]">{subtitle}</p>
    </div>
  );
}

function AdminListCard({
  title,
  description,
  children,
}: {
  title: string;
  description: string;
  children: ReactNode;
}) {
  return (
    <section className="rounded-[28px] border border-[#E7EAEE] bg-white p-7">
      <div className="mb-6 flex items-start justify-between gap-4">
        <div>
          <h4 className="font-['Syne'] text-2xl font-bold text-[#0D2010]">
            {title}
          </h4>
          <p className="mt-2 text-sm text-[#98A2B3]">{description}</p>
        </div>
      </div>
      <div className="space-y-4">{children}</div>
    </section>
  );
}

function MachineField({
  label,
  value,
  onChange,
  placeholder,
}: {
  label: string;
  value: string;
  onChange: (value: string) => void;
  placeholder: string;
}) {
  return (
    <label className="block">
      <span className="mb-2 block text-sm font-semibold text-[#667085]">{label}</span>
      <input
        className="w-full rounded-2xl border border-[#E5E7EB] bg-[#F8FAFC] px-4 py-3 text-sm text-[#101828] outline-none transition focus:border-[#22CC66]"
        placeholder={placeholder}
        type="text"
        value={value}
        onChange={(event) => onChange(event.target.value)}
      />
    </label>
  );
}

function countActiveClients(users: UserSummary[]) {
  return users.filter((user) => user.role === 'CLIENT' && user.activo).length;
}

function countTrainers(users: UserSummary[]) {
  return users.filter((user) => user.role === 'TRAINER').length;
}

function countActiveClasses(data: AdminDashboardData) {
  return data.classes.filter((gymClass) => gymClass.activa).length;
}

function formatActivityTime(dateTime: string) {
  const date = new Date(dateTime);

  return new Intl.DateTimeFormat('es-ES', {
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    month: '2-digit',
  }).format(date);
}

function buildRecentActivity(attendances: AttendanceSummary[]) {
  return [...attendances]
    .sort(
      (first, second) =>
        new Date(second.fechaHora).getTime() - new Date(first.fechaHora).getTime(),
    )
    .slice(0, 6)
    .map((attendance) => ({
      subtitle: `${attendance.usuarioNombre} - ${formatActivityTime(attendance.fechaHora)}`,
      title: `Asistencia QR ${attendance.qrType.toLowerCase()}`,
    }));
}

function buildQrStats(attendances: AttendanceSummary[]) {
  const byType = new Map<string, number>();

  attendances.forEach((attendance) => {
    byType.set(attendance.qrType, (byType.get(attendance.qrType) ?? 0) + 1);
  });

  return [...byType.entries()]
    .map(([type, count]) => ({ count, type }))
    .sort((first, second) => second.count - first.count);
}

function formatPrice(value: number) {
  return new Intl.NumberFormat('es-ES', {
    currency: 'EUR',
    style: 'currency',
  }).format(value);
}

function machineToDraft(machine: MachineSummary): MachineDraft {
  return {
    advertenciaSeguridad: machine.advertenciaSeguridad ?? '',
    descripcion: machine.descripcion ?? '',
    estado: machine.estado,
    grupoMuscular: machine.grupoMuscular ?? '',
    imagenUrl: machine.imagenUrl ?? '',
    instrucciones: machine.instrucciones ?? '',
    nivel: machine.nivel ?? '',
    nombre: machine.nombre,
  };
}

interface DesktopAdminDashboardProps {
  userName: string;
  onLogout: () => void;
}

export function DesktopAdminDashboard({
  userName,
  onLogout,
}: DesktopAdminDashboardProps) {
  const [activeSection, setActiveSection] = useState<AdminSection>('dashboard');
  const [data, setData] = useState<AdminDashboardData | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [machineDraft, setMachineDraft] = useState<MachineDraft>(EMPTY_MACHINE_DRAFT);
  const [selectedMachineId, setSelectedMachineId] = useState<number | null>(null);
  const [machineMessage, setMachineMessage] = useState<string | null>(null);
  const [isSavingMachine, setIsSavingMachine] = useState(false);

  const loadDashboard = async () => {
    try {
      setIsLoading(true);
      setErrorMessage(null);
      const dashboardData = await getAdminDashboardData();
      setData(dashboardData);
    } catch (error) {
      setErrorMessage(
        error instanceof Error
          ? error.message
          : 'No se ha podido cargar el panel de administrador.',
      );
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    void loadDashboard();
  }, []);

  const activeClients = data ? countActiveClients(data.users) : '--';
  const trainers = data ? countTrainers(data.users) : '--';
  const machines = data ? data.machines.length : '--';
  const activeClasses = data ? countActiveClasses(data) : '--';
  const recentActivity = data ? buildRecentActivity(data.attendances) : [];
  const qrStats = useMemo(
    () => (data ? buildQrStats(data.attendances) : []),
    [data],
  );

  const resetMachineEditor = () => {
    setSelectedMachineId(null);
    setMachineDraft(EMPTY_MACHINE_DRAFT);
    setMachineMessage(null);
  };

  const selectMachine = (machine: MachineSummary) => {
    setSelectedMachineId(machine.id);
    setMachineDraft(machineToDraft(machine));
    setMachineMessage(`Editando la maquina ${machine.nombre}.`);
  };

  const handleMachineSave = async () => {
    if (!machineDraft.nombre.trim()) {
      setMachineMessage('El nombre de la maquina es obligatorio.');
      return;
    }

    try {
      setIsSavingMachine(true);
      setMachineMessage(null);

      if (selectedMachineId) {
        await updateMachine(selectedMachineId, machineDraft);
        setMachineMessage('Maquina actualizada correctamente.');
      } else {
        await createMachine(machineDraft);
        setMachineMessage('Maquina creada correctamente.');
      }

      await loadDashboard();
      setSelectedMachineId(null);
      setMachineDraft(EMPTY_MACHINE_DRAFT);
    } catch (error) {
      setMachineMessage(
        error instanceof Error
          ? error.message
          : 'No se ha podido guardar la maquina.',
      );
    } finally {
      setIsSavingMachine(false);
    }
  };

  const handleMachineDelete = async () => {
    if (!selectedMachineId) {
      setMachineMessage('Selecciona una maquina antes de intentar eliminarla.');
      return;
    }

    try {
      setIsSavingMachine(true);
      setMachineMessage(null);
      await deleteMachine(selectedMachineId);
      await loadDashboard();
      resetMachineEditor();
      setMachineMessage('Maquina eliminada correctamente.');
    } catch (error) {
      setMachineMessage(
        error instanceof Error
          ? error.message
          : 'No se ha podido eliminar la maquina.',
      );
    } finally {
      setIsSavingMachine(false);
    }
  };

  const renderDashboardSection = () => (
    <>
      <div className="grid grid-cols-4 gap-5">
        <AdminMetric
          label="Socios activos"
          value={isLoading ? '...' : String(activeClients)}
        />
        <AdminMetric
          label="Entrenadores"
          value={isLoading ? '...' : String(trainers)}
        />
        <AdminMetric
          label="Maquinas"
          value={isLoading ? '...' : String(machines)}
        />
        <AdminMetric
          label="Clases activas"
          value={isLoading ? '...' : String(activeClasses)}
        />
      </div>

      <div className="mt-8 grid grid-cols-2 gap-6">
        <AdminListCard
          description="Resumen rapido de los bloques principales del gimnasio."
          title="Gestion rapida"
        >
          {[
            [
              'Gestion de usuarios',
              data
                ? `${data.users.length} usuarios disponibles en el sistema`
                : 'Preparando el resumen de usuarios',
            ],
            [
              'Maquinas',
              data
                ? `${data.machines.length} maquinas cargadas desde la API`
                : 'Preparando el catalogo actual',
            ],
            [
              'Clases',
              data
                ? `${data.classes.length} clases visibles para administracion`
                : 'Preparando el listado de clases',
            ],
            [
              'Asistencias',
              data
                ? `${data.attendances.length} registros de actividad disponibles`
                : 'Preparando la actividad reciente',
            ],
          ].map(([title, subtitle]) => (
            <AdminInfoCard key={title} subtitle={subtitle} title={title} />
          ))}
        </AdminListCard>

        <AdminListCard
          description="Ultimas asistencias registradas en el sistema."
          title="Actividad reciente"
        >
          {isLoading ? (
            <AdminInfoCard
              subtitle="Estamos leyendo la actividad del backend."
              title="Cargando actividad"
            />
          ) : recentActivity.length > 0 ? (
            recentActivity.map((activity) => (
              <AdminInfoCard
                key={`${activity.title}-${activity.subtitle}`}
                subtitle={activity.subtitle}
                title={activity.title}
              />
            ))
          ) : (
            <AdminInfoCard
              subtitle="Todavia no hay asistencias registradas para mostrar aqui."
              title="Sin actividad reciente"
            />
          )}
        </AdminListCard>
      </div>
    </>
  );

  const renderUsersSection = () => (
    <AdminListCard
      description="Consulta de usuarios conectada al backend con rol y estado."
      title="Usuarios del sistema"
    >
      {isLoading ? (
        <AdminInfoCard
          subtitle="Estamos cargando los usuarios registrados."
          title="Cargando usuarios"
        />
      ) : data && data.users.length > 0 ? (
        data.users.map((user) => (
          <AdminInfoCard
            key={user.id}
            subtitle={`${user.email} - ${user.role} - ${
              user.activo ? 'activo' : 'inactivo'
            }`}
            title={user.nombre}
          />
        ))
      ) : (
        <AdminInfoCard
          subtitle="No hay usuarios para mostrar ahora mismo."
          title="Sin usuarios"
        />
      )}
    </AdminListCard>
  );

  const renderQrSection = () => (
    <div className="grid grid-cols-[0.9fr_1.1fr] gap-6">
      <AdminListCard
        description="Resumen por tipo de QR detectado en las asistencias."
        title="Resumen de QR"
      >
        {isLoading ? (
          <AdminInfoCard
            subtitle="Estamos contando los usos de QR."
            title="Cargando resumen"
          />
        ) : qrStats.length > 0 ? (
          qrStats.map((entry) => (
            <AdminInfoCard
              key={entry.type}
              subtitle={`${entry.count} usos registrados`}
              title={`QR ${entry.type.toLowerCase()}`}
            />
          ))
        ) : (
          <AdminInfoCard
            subtitle="Aun no hay actividad QR registrada."
            title="Sin datos QR"
          />
        )}
      </AdminListCard>

      <AdminListCard
        description="La web se centra en gestionar y supervisar el uso del QR."
        title="Logica por plataforma"
      >
        <AdminInfoCard
          subtitle="Registro rapido de asistencia y escaneo con camara del telefono."
          title="Movil: escaneo principal"
        />
        <AdminInfoCard
          subtitle="Seguimiento de actividad, revision y control del sistema."
          title="Web: supervision y gestion"
        />
      </AdminListCard>
    </div>
  );

  const renderMachinesSection = () => (
    <div className="grid grid-cols-[0.95fr_1.05fr] gap-6">
      <AdminListCard
        description="Selecciona una maquina para editarla o crea una nueva."
        title="Catalogo de maquinas"
      >
        {isLoading ? (
          <AdminInfoCard
            subtitle="Estamos cargando el catalogo."
            title="Cargando maquinas"
          />
        ) : data && data.machines.length > 0 ? (
          data.machines.map((machine) => (
            <button
              key={machine.id}
              className={`w-full rounded-[20px] border px-5 py-4 text-left transition ${
                selectedMachineId === machine.id
                  ? 'border-[#22CC66] bg-[#F0FFF4]'
                  : 'border-[#EEF2F7] bg-[#F8FAFC]'
              }`}
              onClick={() => selectMachine(machine)}
              type="button"
            >
              <p className="text-base font-semibold text-[#0D2010]">{machine.nombre}</p>
              <p className="mt-2 text-sm text-[#667085]">
                {machine.grupoMuscular || 'Sin grupo muscular'} - {machine.estado}
              </p>
            </button>
          ))
        ) : (
          <AdminInfoCard
            subtitle="No hay maquinas registradas en este momento."
            title="Sin maquinas"
          />
        )}
      </AdminListCard>

      <section className="rounded-[28px] border border-[#E7EAEE] bg-white p-7">
        <div className="mb-6 flex items-start justify-between gap-4">
          <div>
            <h4 className="font-['Syne'] text-2xl font-bold text-[#0D2010]">
              {selectedMachineId ? 'Editar maquina' : 'Nueva maquina'}
            </h4>
            <p className="mt-2 text-sm text-[#98A2B3]">
              Formulario sencillo para alta, edicion y borrado desde la web.
            </p>
          </div>
          <button
            className="rounded-full bg-[#F3F4F6] px-4 py-2 text-sm font-semibold text-[#374151]"
            onClick={resetMachineEditor}
            type="button"
          >
            Limpiar
          </button>
        </div>

        <div className="grid grid-cols-2 gap-4">
          <MachineField
            label="Nombre"
            placeholder="Press de banca"
            value={machineDraft.nombre}
            onChange={(value) =>
              setMachineDraft((current) => ({ ...current, nombre: value }))
            }
          />
          <MachineField
            label="Grupo muscular"
            placeholder="Pectoral, hombro"
            value={machineDraft.grupoMuscular}
            onChange={(value) =>
              setMachineDraft((current) => ({ ...current, grupoMuscular: value }))
            }
          />
          <MachineField
            label="Descripcion"
            placeholder="Maquina guiada para trabajo principal"
            value={machineDraft.descripcion}
            onChange={(value) =>
              setMachineDraft((current) => ({ ...current, descripcion: value }))
            }
          />
          <MachineField
            label="Nivel"
            placeholder="Inicial, medio o avanzado"
            value={machineDraft.nivel}
            onChange={(value) =>
              setMachineDraft((current) => ({ ...current, nivel: value }))
            }
          />
          <MachineField
            label="Instrucciones"
            placeholder="Indicaciones de uso"
            value={machineDraft.instrucciones}
            onChange={(value) =>
              setMachineDraft((current) => ({ ...current, instrucciones: value }))
            }
          />
          <MachineField
            label="Advertencia"
            placeholder="Consejo de seguridad"
            value={machineDraft.advertenciaSeguridad}
            onChange={(value) =>
              setMachineDraft((current) => ({
                ...current,
                advertenciaSeguridad: value,
              }))
            }
          />
          <MachineField
            label="Imagen URL"
            placeholder="https://..."
            value={machineDraft.imagenUrl}
            onChange={(value) =>
              setMachineDraft((current) => ({ ...current, imagenUrl: value }))
            }
          />
          <label className="block">
            <span className="mb-2 block text-sm font-semibold text-[#667085]">
              Estado
            </span>
            <select
              className="w-full rounded-2xl border border-[#E5E7EB] bg-[#F8FAFC] px-4 py-3 text-sm text-[#101828] outline-none transition focus:border-[#22CC66]"
              value={machineDraft.estado}
              onChange={(event) =>
                setMachineDraft((current) => ({
                  ...current,
                  estado: event.target.value,
                }))
              }
            >
              <option value="ACTIVA">ACTIVA</option>
              <option value="FUERA_DE_SERVICIO">FUERA_DE_SERVICIO</option>
            </select>
          </label>
        </div>

        {machineMessage ? (
          <div className="mt-5 rounded-2xl border border-[#D1FADF] bg-[#F0FDF4] px-4 py-3 text-sm text-[#166534]">
            {machineMessage}
          </div>
        ) : null}

        <div className="mt-6 flex flex-wrap gap-3">
          <button
            className="rounded-full bg-[#22CC66] px-5 py-3 text-sm font-semibold text-[#0D2010] disabled:cursor-not-allowed disabled:opacity-60"
            disabled={isSavingMachine}
            onClick={handleMachineSave}
            type="button"
          >
            {isSavingMachine
              ? 'Guardando...'
              : selectedMachineId
                ? 'Guardar cambios'
                : 'Crear maquina'}
          </button>
          <button
            className="rounded-full bg-[#0D2010] px-5 py-3 text-sm font-semibold text-white disabled:cursor-not-allowed disabled:opacity-60"
            disabled={isSavingMachine || !selectedMachineId}
            onClick={handleMachineDelete}
            type="button"
          >
            Eliminar maquina
          </button>
        </div>
      </section>
    </div>
  );

  const renderClassesSection = () => (
    <AdminListCard
      description="Listado de clases visibles para administracion."
      title="Clases"
    >
      {isLoading ? (
        <AdminInfoCard
          subtitle="Estamos cargando las clases activas e inactivas."
          title="Cargando clases"
        />
      ) : data && data.classes.length > 0 ? (
        data.classes.map((gymClass: GymClassSummary) => (
          <AdminInfoCard
            key={gymClass.id}
            subtitle={`${gymClass.entrenadorNombre || 'Sin entrenador'} - ${
              gymClass.activa ? 'activa' : 'inactiva'
            }`}
            title={gymClass.nombre}
          />
        ))
      ) : (
        <AdminInfoCard
          subtitle="No hay clases registradas."
          title="Sin clases"
        />
      )}
    </AdminListCard>
  );

  const renderFeesSection = () => (
    <AdminListCard
      description="Consulta simple de cuotas disponibles en el sistema."
      title="Cuotas"
    >
      {isLoading ? (
        <AdminInfoCard
          subtitle="Estamos cargando las cuotas del backend."
          title="Cargando cuotas"
        />
      ) : data && data.membershipFees.length > 0 ? (
        data.membershipFees.map((fee: MembershipFeeSummary) => (
          <AdminInfoCard
            key={fee.id}
            subtitle={fee.descripcion || 'Cuota disponible en el sistema'}
            title={`${fee.nombre} - ${formatPrice(fee.precio)}`}
          />
        ))
      ) : (
        <AdminInfoCard
          subtitle="No hay cuotas registradas para mostrar."
          title="Sin cuotas"
        />
      )}
    </AdminListCard>
  );

  const renderSection = () => {
    switch (activeSection) {
      case 'usuarios':
        return renderUsersSection();
      case 'qr':
        return renderQrSection();
      case 'maquinas':
        return renderMachinesSection();
      case 'clases':
        return renderClassesSection();
      case 'cuotas':
        return renderFeesSection();
      case 'dashboard':
      default:
        return renderDashboardSection();
    }
  };

  const sectionTitles: Record<AdminSection, string> = {
    clases: 'Clases',
    cuotas: 'Cuotas',
    dashboard: 'Dashboard',
    maquinas: 'Maquinas',
    qr: 'QR',
    usuarios: 'Usuarios',
  };

  return (
    <div className="overflow-hidden rounded-[32px] border border-[#E7EAEE] bg-[#F2F5F2] shadow-[0_24px_60px_rgba(15,23,42,0.10)]">
      <div className="grid min-h-[760px] grid-cols-[240px_1fr]">
        <aside className="bg-[#0D2010] px-5 py-7">
          <h2 className="font-['Syne'] text-3xl font-extrabold text-white">
            Hazel Gym
          </h2>
          <p className="mt-2 text-sm text-[#A7F3D0]">Administracion</p>
          <div className="mt-10 space-y-3">
            <AdminSidebarItem
              active={activeSection === 'dashboard'}
              label="Dashboard"
              onClick={() => setActiveSection('dashboard')}
            />
            <AdminSidebarItem
              active={activeSection === 'usuarios'}
              label="Usuarios"
              onClick={() => setActiveSection('usuarios')}
            />
            <AdminSidebarItem
              active={activeSection === 'qr'}
              label="QR"
              onClick={() => setActiveSection('qr')}
            />
            <AdminSidebarItem
              active={activeSection === 'maquinas'}
              label="Maquinas"
              onClick={() => setActiveSection('maquinas')}
            />
            <AdminSidebarItem
              active={activeSection === 'clases'}
              label="Clases"
              onClick={() => setActiveSection('clases')}
            />
            <AdminSidebarItem
              active={activeSection === 'cuotas'}
              label="Cuotas"
              onClick={() => setActiveSection('cuotas')}
            />
          </div>
        </aside>

        <main className="p-10">
          <div className="mb-9 flex items-center justify-between gap-6">
            <div>
              <p className="text-sm text-[#667085]">Panel de administracion</p>
              <h3 className="font-['Syne'] text-5xl font-extrabold text-[#0D2010]">
                {userName}
              </h3>
              <p className="mt-3 text-sm text-[#98A2B3]">
                Seccion actual: {sectionTitles[activeSection]}
              </p>
            </div>
            <div className="flex items-center gap-3">
              <div className="rounded-full bg-[#DCFCE7] px-5 py-3 text-sm font-semibold text-[#166534]">
                Administrador
              </div>
              <button
                className="rounded-full bg-[#0D2010] px-5 py-3 text-sm font-semibold text-white"
                onClick={onLogout}
              >
                Cerrar sesion
              </button>
            </div>
          </div>

          {errorMessage ? (
            <div className="mb-6 rounded-[22px] border border-[#FFB4A3] bg-[#FFF3EF] px-5 py-4 text-sm text-[#B93815]">
              {errorMessage}
            </div>
          ) : null}

          {renderSection()}
        </main>
      </div>
    </div>
  );
}
