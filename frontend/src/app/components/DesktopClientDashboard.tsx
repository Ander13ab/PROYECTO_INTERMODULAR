import { useEffect, useState } from 'react';
import type { ReactNode } from 'react';
import { getClientDashboardData } from '../services/clientDashboardService';
import type {
  ClientAttendance,
  ClientDashboardData,
  ClientGymClass,
  ClientMachine,
  ClientRoutine,
} from '../types/client';

type ClientSection = 'inicio' | 'asistencia' | 'maquinas' | 'rutinas' | 'perfil';

function SidebarItem({
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
          ? 'bg-[#FF4D2E] font-semibold text-white'
          : 'bg-[#181B24] text-[#C9D0DB]'
      }`}
      onClick={onClick}
      type="button"
    >
      {label}
    </button>
  );
}

function StatCard({
  label,
  value,
  helper,
  accent,
}: {
  label: string;
  value: string;
  helper: string;
  accent: string;
}) {
  return (
    <div className="rounded-[24px] border border-[#E7EAEE] bg-white p-6">
      <div className="mb-5 flex items-center gap-3">
        <div className="h-8 w-2 rounded-full" style={{ backgroundColor: accent }} />
        <p className="text-sm font-semibold text-[#667085]">{label}</p>
      </div>
      <p className="font-['Syne'] text-4xl font-extrabold text-[#101828]">{value}</p>
      <p className="mt-3 text-sm text-[#98A2B3]">{helper}</p>
    </div>
  );
}

function ClientListCard({
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
      <h4 className="font-['Syne'] text-2xl font-bold text-[#101828]">{title}</h4>
      <p className="mt-2 text-sm text-[#98A2B3]">{description}</p>
      <div className="mt-6 space-y-4">{children}</div>
    </section>
  );
}

function ClientRow({
  title,
  subtitle,
}: {
  title: string;
  subtitle: string;
}) {
  return (
    <div className="rounded-[20px] border border-[#EEF2F7] bg-[#F8FAFC] px-4 py-4">
      <p className="text-base font-semibold text-[#101828]">{title}</p>
      <p className="mt-2 text-sm text-[#667085]">{subtitle}</p>
    </div>
  );
}

function formatDate(dateValue: string) {
  return new Intl.DateTimeFormat('es-ES', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  }).format(new Date(dateValue));
}

function buildCurrentStreak(attendances: ClientAttendance[]) {
  if (attendances.length === 0) {
    return 0;
  }

  const distinctDays = [
    ...new Set(
      attendances
        .map((attendance) => attendance.fechaHora.slice(0, 10))
        .sort((first, second) => second.localeCompare(first)),
    ),
  ];

  let streak = 0;
  const cursor = new Date();
  cursor.setHours(0, 0, 0, 0);

  for (const day of distinctDays) {
    const attendanceDate = new Date(`${day}T00:00:00`);
    const diffDays = Math.round(
      (cursor.getTime() - attendanceDate.getTime()) / (1000 * 60 * 60 * 24),
    );

    if (diffDays === 0) {
      streak += 1;
      cursor.setDate(cursor.getDate() - 1);
      continue;
    }

    break;
  }

  return streak;
}

function buildNextClasses(classes: ClientGymClass[]) {
  return classes.filter((gymClass) => gymClass.activa).slice(0, 3);
}

function buildRecentAttendances(attendances: ClientAttendance[]) {
  return [...attendances]
    .sort(
      (first, second) =>
        new Date(second.fechaHora).getTime() - new Date(first.fechaHora).getTime(),
    )
    .slice(0, 6);
}

interface DesktopClientDashboardProps {
  userId: number;
  userName: string;
  onLogout: () => void;
}

export function DesktopClientDashboard({
  userId,
  userName,
  onLogout,
}: DesktopClientDashboardProps) {
  const [activeSection, setActiveSection] = useState<ClientSection>('inicio');
  const [data, setData] = useState<ClientDashboardData | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  useEffect(() => {
    const loadDashboard = async () => {
      try {
        setIsLoading(true);
        setErrorMessage(null);
        const dashboardData = await getClientDashboardData();
        setData(dashboardData);
      } catch (error) {
        setErrorMessage(
          error instanceof Error
            ? error.message
            : 'No se ha podido cargar el panel del cliente.',
        );
      } finally {
        setIsLoading(false);
      }
    };

    void loadDashboard();
  }, [userId]);

  const attendances = data?.attendances ?? [];
  const routines = data?.routines ?? [];
  const machines = data?.machines ?? [];
  const classes = data?.classes ?? [];

  const streak = buildCurrentStreak(attendances);
  const nextClasses = buildNextClasses(classes);
  const recentAttendances = buildRecentAttendances(attendances);

  const renderHome = () => (
    <>
      <div className="grid grid-cols-3 gap-6">
        <StatCard
          accent="#FF4D2E"
          helper={isLoading ? 'cargando...' : 'entradas registradas'}
          label="Visitas totales"
          value={isLoading ? '...' : String(attendances.length)}
        />
        <StatCard
          accent="#2266FF"
          helper={isLoading ? 'cargando...' : 'dias consecutivos'}
          label="Racha actual"
          value={isLoading ? '...' : String(streak)}
        />
        <StatCard
          accent="#22CC66"
          helper={isLoading ? 'cargando...' : 'asignadas por tu entrenador'}
          label="Rutinas activas"
          value={isLoading ? '...' : String(routines.length)}
        />
      </div>

      <div className="mt-8 grid grid-cols-[1fr_320px] gap-6">
        <section className="rounded-[28px] border border-[#E7EAEE] bg-white p-7">
          <div className="mb-6 flex items-center justify-between">
            <h4 className="font-['Syne'] text-2xl font-bold text-[#101828]">
              Acciones y seguimiento
            </h4>
            <p className="text-sm text-[#98A2B3]">
              La asistencia por QR se registra mejor desde la app movil
            </p>
          </div>
          <div className="grid grid-cols-3 gap-5">
            <div className="rounded-[22px] bg-[#0D0D14] p-6 text-white">
              <p className="text-lg font-semibold">Usar QR en movil</p>
              <p className="mt-3 text-sm text-white/55">
                Desde web puedes consultar tu historial, pero el escaneo principal
                se hace desde el telefono.
              </p>
            </div>
            <div className="rounded-[22px] bg-[#F8FAFC] p-6">
              <p className="text-lg font-semibold text-[#101828]">Ver maquinas</p>
              <p className="mt-3 text-sm text-[#98A2B3]">
                {isLoading
                  ? 'Preparando catalogo'
                  : `${machines.length} maquinas visibles en el catalogo`}
              </p>
            </div>
            <div className="rounded-[22px] bg-[#F8FAFC] p-6">
              <p className="text-lg font-semibold text-[#101828]">Mis rutinas</p>
              <p className="mt-3 text-sm text-[#98A2B3]">
                {isLoading
                  ? 'Preparando rutinas'
                  : `${routines.length} rutinas asignadas a tu cuenta`}
              </p>
            </div>
          </div>
        </section>

        <aside className="rounded-[28px] bg-[#0D0D14] p-7 text-white">
          <h4 className="font-['Syne'] text-2xl font-bold">Proximas clases</h4>
          <div className="mt-6 space-y-4">
            {isLoading ? (
              <div className="rounded-[20px] bg-[#181B24] p-5 text-sm text-white/60">
                Cargando clases activas.
              </div>
            ) : nextClasses.length > 0 ? (
              nextClasses.map((gymClass) => (
                <div key={gymClass.id} className="rounded-[20px] bg-[#181B24] p-5">
                  <p className="text-lg font-semibold">{gymClass.nombre}</p>
                  <p className="mt-2 text-sm text-white/55">
                    {gymClass.entrenadorNombre
                      ? `Con ${gymClass.entrenadorNombre}`
                      : 'Clase disponible en el gimnasio'}
                  </p>
                </div>
              ))
            ) : (
              <div className="rounded-[20px] bg-[#181B24] p-5 text-sm text-white/60">
                No hay clases activas para mostrar ahora mismo.
              </div>
            )}
          </div>
        </aside>
      </div>
    </>
  );

  const renderAttendances = () => (
    <ClientListCard
      description="Historial de asistencias registradas con tu cuenta."
      title="Asistencia"
    >
      {isLoading ? (
        <ClientRow
          subtitle="Estamos cargando tu historial de asistencias."
          title="Cargando historial"
        />
      ) : recentAttendances.length > 0 ? (
        recentAttendances.map((attendance) => (
          <ClientRow
            key={attendance.id}
            subtitle={`${formatDate(attendance.fechaHora)} - QR ${attendance.qrType.toLowerCase()}`}
            title={`Acceso #${attendance.id}`}
          />
        ))
      ) : (
        <ClientRow
          subtitle="Todavia no hay asistencias registradas en tu historial."
          title="Sin asistencias"
        />
      )}
    </ClientListCard>
  );

  const renderMachines = () => (
    <ClientListCard
      description="Consulta del catalogo visible de maquinas del gimnasio."
      title="Maquinas"
    >
      {isLoading ? (
        <ClientRow
          subtitle="Estamos cargando el catalogo de maquinas."
          title="Cargando maquinas"
        />
      ) : machines.length > 0 ? (
        machines.map((machine: ClientMachine) => (
          <ClientRow
            key={machine.id}
            subtitle={`${machine.grupoMuscular || 'Sin grupo muscular'} - ${
              machine.estado
            }`}
            title={machine.nombre}
          />
        ))
      ) : (
        <ClientRow
          subtitle="No hay maquinas registradas para mostrar."
          title="Sin maquinas"
        />
      )}
    </ClientListCard>
  );

  const renderRoutines = () => (
    <ClientListCard
      description="Rutinas asignadas especificamente a tu cuenta."
      title="Rutinas"
    >
      {isLoading ? (
        <ClientRow
          subtitle="Estamos cargando tus rutinas asignadas."
          title="Cargando rutinas"
        />
      ) : routines.length > 0 ? (
        routines.map((routine: ClientRoutine) => (
          <ClientRow
            key={routine.id}
            subtitle={routine.descripcion || 'Rutina disponible en tu plan actual.'}
            title={routine.nombre}
          />
        ))
      ) : (
        <ClientRow
          subtitle="Aun no tienes rutinas asignadas."
          title="Sin rutinas"
        />
      )}
    </ClientListCard>
  );

  const renderProfile = () => (
    <ClientListCard
      description="Resumen del alcance de este panel web."
      title="Perfil"
    >
      <ClientRow
        subtitle="Consulta tu progreso, historial, maquinas y rutinas desde una vista de escritorio."
        title={userName}
      />
      <ClientRow
        subtitle="El uso principal del QR se mantiene en la app movil para que el registro de asistencia sea rapido y natural."
        title="Rol: Cliente"
      />
    </ClientListCard>
  );

  const renderSection = () => {
    switch (activeSection) {
      case 'asistencia':
        return renderAttendances();
      case 'maquinas':
        return renderMachines();
      case 'rutinas':
        return renderRoutines();
      case 'perfil':
        return renderProfile();
      case 'inicio':
      default:
        return renderHome();
    }
  };

  const sectionTitles: Record<ClientSection, string> = {
    asistencia: 'Asistencia',
    inicio: 'Inicio',
    maquinas: 'Maquinas',
    perfil: 'Perfil',
    rutinas: 'Rutinas',
  };

  return (
    <div className="overflow-hidden rounded-[32px] border border-[#E7EAEE] bg-[#F2F5F2] shadow-[0_24px_60px_rgba(15,23,42,0.10)]">
      <div className="grid min-h-[760px] grid-cols-[240px_1fr]">
        <aside className="bg-[#0D0D14] px-5 py-7">
          <h2 className="font-['Syne'] text-3xl font-extrabold text-white">
            Hazel Gym
          </h2>
          <p className="mt-2 text-sm text-[#98A2B3]">Panel cliente</p>
          <div className="mt-10 space-y-3">
            <SidebarItem
              active={activeSection === 'inicio'}
              label="Inicio"
              onClick={() => setActiveSection('inicio')}
            />
            <SidebarItem
              active={activeSection === 'asistencia'}
              label="Asistencia"
              onClick={() => setActiveSection('asistencia')}
            />
            <SidebarItem
              active={activeSection === 'maquinas'}
              label="Maquinas"
              onClick={() => setActiveSection('maquinas')}
            />
            <SidebarItem
              active={activeSection === 'rutinas'}
              label="Rutinas"
              onClick={() => setActiveSection('rutinas')}
            />
            <SidebarItem
              active={activeSection === 'perfil'}
              label="Perfil"
              onClick={() => setActiveSection('perfil')}
            />
          </div>
        </aside>

        <main className="p-10">
          <div className="mb-9 flex items-center justify-between gap-6">
            <div>
              <p className="text-sm text-[#667085]">Hola de nuevo</p>
              <h3 className="font-['Syne'] text-5xl font-extrabold text-[#101828]">
                {userName}
              </h3>
              <p className="mt-3 text-sm text-[#98A2B3]">
                Seccion actual: {sectionTitles[activeSection]}
              </p>
            </div>
            <div className="flex items-center gap-3">
              <div className="rounded-full bg-[#FFE1D8] px-5 py-3 text-sm font-semibold text-[#C2410C]">
                Cliente
              </div>
              <button
                className="rounded-full bg-[#0D0D14] px-5 py-3 text-sm font-semibold text-white"
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
