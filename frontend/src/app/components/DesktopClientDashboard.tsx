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
      className={`w-full rounded-2xl border px-4 py-3 text-left text-sm transition duration-200 ${
        active
          ? 'border-[#FF7A64] bg-[#FF4D2E] font-semibold text-white shadow-[0_12px_24px_rgba(255,77,46,0.28)]'
          : 'border-[#222633] bg-[#181B24] text-[#C9D0DB] hover:bg-[#242936]'
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
    <div className="rounded-[24px] border border-white/70 bg-white/88 p-6 shadow-[0_22px_50px_rgba(16,24,40,0.08)] backdrop-blur-sm">
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
    <section className="rounded-[28px] border border-white/70 bg-white/88 p-5 shadow-[0_24px_50px_rgba(16,24,40,0.08)] backdrop-blur-sm sm:p-6 xl:p-7">
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
    <div className="rounded-[20px] border border-[#E8EDF3] bg-[#F8FAFC] px-4 py-4 shadow-[0_10px_24px_rgba(16,24,40,0.04)]">
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
  const [attendanceSearch, setAttendanceSearch] = useState('');
  const [attendanceTypeFilter, setAttendanceTypeFilter] = useState<'ALL' | string>('ALL');
  const [selectedAttendanceId, setSelectedAttendanceId] = useState<number | null>(null);
  const [routineSearch, setRoutineSearch] = useState('');
  const [selectedRoutineId, setSelectedRoutineId] = useState<number | null>(null);

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
  const assignments = data?.assignments ?? [];
  const machines = data?.machines ?? [];
  const classes = data?.classes ?? [];

  const streak = buildCurrentStreak(attendances);
  const nextClasses = buildNextClasses(classes);
  const recentAttendances = buildRecentAttendances(attendances);
  const filteredAttendances = recentAttendances.filter((attendance) => {
    const matchesType =
      attendanceTypeFilter === 'ALL' || attendance.qrType === attendanceTypeFilter;
    const matchesSearch = [attendance.usuarioNombre, attendance.qrType, String(attendance.id)]
      .join(' ')
      .toLowerCase()
      .includes(attendanceSearch.trim().toLowerCase());

    return matchesType && matchesSearch;
  });
  const selectedAttendance =
    recentAttendances.find((attendance) => attendance.id === selectedAttendanceId) ?? null;
  const filteredRoutines = routines.filter((routine) =>
    [routine.nombre, routine.descripcion || '', routine.entrenadorNombre || '', String(routine.id)]
      .join(' ')
      .toLowerCase()
      .includes(routineSearch.trim().toLowerCase()),
  );
  const selectedRoutine =
    routines.find((routine) => routine.id === selectedRoutineId) ?? null;
  const selectedAssignment =
    assignments.find((assignment) => assignment.routineId === selectedRoutine?.id) ?? null;

  const renderHome = () => (
    <>
      <div className="grid grid-cols-1 gap-6 md:grid-cols-2 2xl:grid-cols-3">
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

      <div className="mt-8 grid grid-cols-1 gap-6 2xl:grid-cols-[1fr_320px]">
        <section className="rounded-[28px] border border-[#E7EAEE] bg-white p-5 sm:p-6 xl:p-7">
          <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
            <h4 className="font-['Syne'] text-2xl font-bold text-[#101828]">
              Acciones y seguimiento
            </h4>
            <p className="text-sm text-[#98A2B3]">
              La asistencia por QR se registra mejor desde la app movil
            </p>
          </div>
          <div className="grid grid-cols-1 gap-5 lg:grid-cols-2 xl:grid-cols-3">
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
    <div className="grid grid-cols-1 gap-6 2xl:grid-cols-[0.95fr_1.05fr]">
      <ClientListCard
        description="Consulta tu historial con mas detalle y filtra por tipo de QR."
        title="Asistencia"
      >
        <div className="grid grid-cols-1 gap-4 lg:grid-cols-[1fr_220px]">
          <label className="block">
            <span className="mb-2 block text-sm font-semibold text-[#667085]">
              Buscar registro
            </span>
            <input
              className="w-full rounded-2xl border border-[#E7EAEE] bg-[#F8FAFC] px-4 py-3 text-sm text-[#101828] outline-none transition focus:border-[#FF4D2E]"
              placeholder="ID o tipo de QR"
              value={attendanceSearch}
              onChange={(event) => setAttendanceSearch(event.target.value)}
            />
          </label>
          <label className="block">
            <span className="mb-2 block text-sm font-semibold text-[#667085]">
              Tipo de QR
            </span>
            <select
              className="w-full rounded-2xl border border-[#E7EAEE] bg-[#F8FAFC] px-4 py-3 text-sm text-[#101828] outline-none transition focus:border-[#FF4D2E]"
              value={attendanceTypeFilter}
              onChange={(event) => setAttendanceTypeFilter(event.target.value)}
            >
              <option value="ALL">Todos</option>
              <option value="ENTRY">Entrada</option>
              <option value="MACHINE">Maquina</option>
              <option value="CLASS_SESSION">Sesion de clase</option>
            </select>
          </label>
        </div>

        {isLoading ? (
          <ClientRow
            subtitle="Estamos cargando tu historial de asistencias."
            title="Cargando historial"
          />
        ) : filteredAttendances.length > 0 ? (
          filteredAttendances.map((attendance) => (
            <button
              key={attendance.id}
              className={`w-full rounded-[20px] border px-4 py-4 text-left transition ${
                selectedAttendanceId === attendance.id
                  ? 'border-[#FF4D2E] bg-[#FFF3EF]'
                  : 'border-[#EEF2F7] bg-[#F8FAFC]'
              }`}
              onClick={() => setSelectedAttendanceId(attendance.id)}
              type="button"
            >
              <p className="text-base font-semibold text-[#101828]">
                Acceso #{attendance.id}
              </p>
              <p className="mt-2 text-sm text-[#667085]">
                {formatDate(attendance.fechaHora)} - QR {attendance.qrType.toLowerCase()}
              </p>
            </button>
          ))
        ) : (
          <ClientRow
            subtitle="No hay asistencias que coincidan con los filtros actuales."
            title="Sin resultados"
          />
        )}
      </ClientListCard>

      <ClientListCard
        description="La web te ayuda a revisar tu progreso; el escaneo principal sigue estando en la app movil."
        title="Detalle del registro"
      >
        <div className="grid grid-cols-1 gap-5 lg:grid-cols-3">
          <div className="rounded-[22px] border border-[#EEF2F7] bg-[#F8FAFC] p-5">
            <p className="text-sm font-semibold text-[#667085]">Visitas</p>
            <p className="mt-3 font-['Syne'] text-4xl font-extrabold text-[#101828]">
              {isLoading ? '...' : attendances.length}
            </p>
          </div>
          <div className="rounded-[22px] border border-[#EEF2F7] bg-[#F8FAFC] p-5">
            <p className="text-sm font-semibold text-[#667085]">Racha actual</p>
            <p className="mt-3 font-['Syne'] text-4xl font-extrabold text-[#101828]">
              {isLoading ? '...' : streak}
            </p>
          </div>
          <div className="rounded-[22px] border border-[#EEF2F7] bg-[#F8FAFC] p-5">
            <p className="text-sm font-semibold text-[#667085]">Rutinas activas</p>
            <p className="mt-3 font-['Syne'] text-4xl font-extrabold text-[#101828]">
              {isLoading ? '...' : routines.length}
            </p>
          </div>
        </div>

        {selectedAttendance ? (
          <div className="rounded-[22px] bg-[#0D0D14] p-6 text-white">
            <p className="text-lg font-semibold">Registro seleccionado</p>
            <p className="mt-4 text-sm text-white/65">
              Identificador: #{selectedAttendance.id}
            </p>
            <p className="mt-2 text-sm text-white/65">
              Fecha: {formatDate(selectedAttendance.fechaHora)}
            </p>
            <p className="mt-2 text-sm text-white/65">
              Tipo: QR {selectedAttendance.qrType.toLowerCase()}
            </p>
            <p className="mt-4 text-sm text-white/55">
              Este panel esta pensado para consulta y seguimiento. El registro de la asistencia sigue siendo mas natural desde la app movil con QR.
            </p>
          </div>
        ) : (
          <ClientRow
            subtitle="Selecciona un registro del historial para revisar sus datos principales."
            title="Todavia no has elegido una asistencia"
          />
        )}
      </ClientListCard>
    </div>
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
    <div className="grid grid-cols-1 gap-6 2xl:grid-cols-[0.95fr_1.05fr]">
      <ClientListCard
        description="Rutinas asignadas especificamente a tu cuenta."
        title="Rutinas"
      >
        <label className="block">
          <span className="mb-2 block text-sm font-semibold text-[#667085]">
            Buscar rutina
          </span>
          <input
            className="w-full rounded-2xl border border-[#E7EAEE] bg-[#F8FAFC] px-4 py-3 text-sm text-[#101828] outline-none transition focus:border-[#FF4D2E]"
            placeholder="Nombre, entrenador o ID"
            value={routineSearch}
            onChange={(event) => setRoutineSearch(event.target.value)}
          />
        </label>

        {isLoading ? (
          <ClientRow
            subtitle="Estamos cargando tus rutinas asignadas."
            title="Cargando rutinas"
          />
        ) : filteredRoutines.length > 0 ? (
          filteredRoutines.map((routine: ClientRoutine) => (
            <button
              key={routine.id}
              className={`w-full rounded-[20px] border px-4 py-4 text-left transition ${
                selectedRoutineId === routine.id
                  ? 'border-[#FF4D2E] bg-[#FFF3EF]'
                  : 'border-[#EEF2F7] bg-[#F8FAFC]'
              }`}
              onClick={() => setSelectedRoutineId(routine.id)}
              type="button"
            >
              <p className="text-base font-semibold text-[#101828]">{routine.nombre}</p>
              <p className="mt-2 text-sm text-[#667085]">
                {routine.entrenadorNombre
                  ? `Con ${routine.entrenadorNombre}`
                  : 'Rutina disponible en tu cuenta'}
              </p>
            </button>
          ))
        ) : (
          <ClientRow
            subtitle="No hay rutinas que coincidan con la busqueda actual."
            title="Sin resultados"
          />
        )}
      </ClientListCard>

      <ClientListCard
        description="Consulta desde escritorio el contexto principal de cada rutina."
        title="Detalle de rutina"
      >
        <div className="grid grid-cols-1 gap-5 lg:grid-cols-3">
          <div className="rounded-[22px] border border-[#EEF2F7] bg-[#F8FAFC] p-5">
            <p className="text-sm font-semibold text-[#667085]">Rutinas activas</p>
            <p className="mt-3 font-['Syne'] text-4xl font-extrabold text-[#101828]">
              {isLoading ? '...' : routines.length}
            </p>
          </div>
          <div className="rounded-[22px] border border-[#EEF2F7] bg-[#F8FAFC] p-5">
            <p className="text-sm font-semibold text-[#667085]">Asignaciones</p>
            <p className="mt-3 font-['Syne'] text-4xl font-extrabold text-[#101828]">
              {isLoading ? '...' : assignments.length}
            </p>
          </div>
          <div className="rounded-[22px] border border-[#EEF2F7] bg-[#F8FAFC] p-5">
            <p className="text-sm font-semibold text-[#667085]">Clases visibles</p>
            <p className="mt-3 font-['Syne'] text-4xl font-extrabold text-[#101828]">
              {isLoading ? '...' : classes.length}
            </p>
          </div>
        </div>

        {selectedRoutine ? (
          <div className="rounded-[22px] bg-[#0D0D14] p-6 text-white">
            <p className="text-lg font-semibold">{selectedRoutine.nombre}</p>
            <p className="mt-4 text-sm text-white/65">
              ID de rutina: #{selectedRoutine.id}
            </p>
            <p className="mt-2 text-sm text-white/65">
              Entrenador:{' '}
              {selectedRoutine.entrenadorNombre || 'Asignacion disponible en tu cuenta'}
            </p>
            <p className="mt-2 text-sm text-white/65">
              Fecha de alta: {formatDate(selectedRoutine.fechaCreacion)}
            </p>
            <p className="mt-2 text-sm text-white/65">
              Fecha de asignacion:{' '}
              {selectedAssignment
                ? formatDate(selectedAssignment.fechaAsignacion)
                : 'No disponible'}
            </p>
            <p className="mt-4 text-sm text-white/55">
              {selectedRoutine.descripcion ||
                'Esta rutina esta activa en tu cuenta y puedes revisarla desde la web para seguir mejor tu plan.'}
            </p>
          </div>
        ) : (
          <ClientRow
            subtitle="Selecciona una rutina para revisar su entrenador, fechas y descripcion."
            title="Todavia no has elegido una rutina"
          />
        )}
      </ClientListCard>
    </div>
  );

  const renderProfile = () => (
    <div className="grid grid-cols-1 gap-6 2xl:grid-cols-[0.95fr_1.05fr]">
      <ClientListCard
        description="Resumen general de tu cuenta en la web de Hazel Gym."
        title="Perfil"
      >
        <div className="rounded-[22px] bg-[#0D0D14] p-6 text-white">
          <p className="text-sm font-semibold uppercase tracking-[0.18em] text-white/55">
            Cuenta activa
          </p>
          <p className="mt-4 font-['Syne'] text-4xl font-extrabold">{userName}</p>
          <p className="mt-3 text-sm text-white/65">
            Este panel esta pensado para que puedas consultar tu actividad,
            revisar tus rutinas y seguir tu progreso desde escritorio.
          </p>
        </div>

        <div className="grid grid-cols-1 gap-4 lg:grid-cols-2">
          <ClientRow
            subtitle="Acceso a historial, maquinas, rutinas y seguimiento general."
            title="Rol: Cliente"
          />
          <ClientRow
            subtitle="El registro rapido de asistencia mediante QR sigue siendo mas natural desde movil."
            title="Uso recomendado"
          />
          <ClientRow
            subtitle={isLoading ? 'cargando...' : `${attendances.length} visitas registradas`}
            title="Historial disponible"
          />
          <ClientRow
            subtitle={isLoading ? 'cargando...' : `${routines.length} rutinas activas en tu cuenta`}
            title="Plan actual"
          />
        </div>
      </ClientListCard>

      <ClientListCard
        description="Contexto rapido para entender de un vistazo tu estado actual."
        title="Estado actual"
      >
        <div className="grid grid-cols-1 gap-5 lg:grid-cols-3">
          <div className="rounded-[22px] border border-[#EEF2F7] bg-[#F8FAFC] p-5">
            <p className="text-sm font-semibold text-[#667085]">Visitas</p>
            <p className="mt-3 font-['Syne'] text-4xl font-extrabold text-[#101828]">
              {isLoading ? '...' : attendances.length}
            </p>
          </div>
          <div className="rounded-[22px] border border-[#EEF2F7] bg-[#F8FAFC] p-5">
            <p className="text-sm font-semibold text-[#667085]">Racha</p>
            <p className="mt-3 font-['Syne'] text-4xl font-extrabold text-[#101828]">
              {isLoading ? '...' : streak}
            </p>
          </div>
          <div className="rounded-[22px] border border-[#EEF2F7] bg-[#F8FAFC] p-5">
            <p className="text-sm font-semibold text-[#667085]">Asignaciones</p>
            <p className="mt-3 font-['Syne'] text-4xl font-extrabold text-[#101828]">
              {isLoading ? '...' : assignments.length}
            </p>
          </div>
        </div>

        <div className="rounded-[22px] border border-[#EEF2F7] bg-[#F8FAFC] p-5">
          <p className="text-base font-semibold text-[#101828]">Resumen de uso web</p>
          <p className="mt-3 text-sm text-[#667085]">
            La version de escritorio esta orientada a lectura comoda y seguimiento
            de tu plan. Aqui puedes consultar tus rutinas, revisar tu historial de
            asistencia y ver el catalogo del gimnasio sin depender del telefono.
          </p>
        </div>

        <div className="rounded-[22px] border border-[#EEF2F7] bg-[#FFF3EF] p-5">
          <p className="text-base font-semibold text-[#B93815]">Nota sobre QR</p>
          <p className="mt-3 text-sm text-[#B93815]">
            El flujo principal de escaneo se mantiene en la app movil para que la
            entrada al gimnasio siga siendo mas rapida y coherente con el uso real.
          </p>
        </div>
      </ClientListCard>
    </div>
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
    <div className="min-h-screen w-full bg-[radial-gradient(circle_at_top_left,_rgba(255,77,46,0.10),_transparent_18%),linear-gradient(180deg,#F2F5F2_0%,#EEF2EF_100%)] text-[#101828]">
      <div className="grid min-h-screen grid-cols-1 lg:grid-cols-[240px_minmax(0,1fr)]">
        <aside className="border-b border-[#202432] bg-[linear-gradient(180deg,#0D0D14_0%,#161924_100%)] px-4 py-6 sm:px-5 sm:py-7 lg:sticky lg:top-0 lg:flex lg:h-screen lg:flex-col lg:justify-between lg:border-r lg:border-b-0">
          <div>
          <h2 className="font-['Syne'] text-3xl font-extrabold text-white">
            Hazel Gym
          </h2>
          <p className="mt-2 text-sm text-[#98A2B3]">Panel cliente</p>
          <div className="mt-8 grid grid-cols-2 gap-3 sm:grid-cols-3 lg:mt-10 lg:grid-cols-1">
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
          </div>
          <div className="mt-8 hidden rounded-[22px] border border-[#222633] bg-[#151924] p-4 text-sm text-[#98A2B3] lg:block">
            Consulta tu progreso desde escritorio y deja el registro rapido para movil.
          </div>
        </aside>

        <main className="min-w-0 px-4 py-6 sm:px-6 lg:px-8 xl:p-10">
          <div className="mb-8 rounded-[30px] border border-white/70 bg-white/72 p-5 shadow-[0_24px_55px_rgba(16,24,40,0.08)] backdrop-blur-xl sm:p-6 xl:p-7">
          <div className="flex flex-col gap-4 xl:flex-row xl:items-start xl:justify-between">
            <div>
              <p className="text-sm font-medium uppercase tracking-[0.2em] text-[#667085]">
                Hola de nuevo
              </p>
              <h3 className="font-['Syne'] text-3xl font-extrabold text-[#101828] sm:text-4xl xl:text-5xl">
                {userName}
              </h3>
              <p className="mt-3 text-sm text-[#98A2B3]">
                Seccion actual: {sectionTitles[activeSection]}
              </p>
            </div>
            <div className="flex flex-wrap items-center gap-3">
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
          </div>

          {errorMessage ? (
            <div className="mb-6 rounded-[22px] border border-[#FFB4A3] bg-[#FFF3EF] px-5 py-4 text-sm text-[#B93815]">
              {errorMessage}
            </div>
          ) : null}

          <div className="space-y-8">{renderSection()}</div>
        </main>
      </div>
    </div>
  );
}
