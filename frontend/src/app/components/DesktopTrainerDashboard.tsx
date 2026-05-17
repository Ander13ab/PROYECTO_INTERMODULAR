import { useEffect, useState } from 'react';
import type { ReactNode } from 'react';
import { getTrainerDashboardData } from '../services/trainerDashboardService';
import type {
  TrainerAssignment,
  TrainerClassSession,
  TrainerClient,
  TrainerDashboardData,
  TrainerRoutine,
} from '../types/trainer';

type TrainerSection = 'resumen' | 'sesiones' | 'clientes' | 'rutinas' | 'perfil';

function TrainerSidebarItem({
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
      className={`w-full rounded-2xl px-4 py-3 text-left text-sm ${
        active
          ? 'bg-[#2266FF] font-semibold text-white'
          : 'bg-[#10245E] text-[#D8E4FF]'
      }`}
      onClick={onClick}
      type="button"
    >
      {label}
    </button>
  );
}

function TrainerInfoCard({
  title,
  value,
  helper,
}: {
  title: string;
  value: string;
  helper: string;
}) {
  return (
    <section className="rounded-[28px] border border-[#DCE3F6] bg-white p-7">
      <h4 className="font-['Syne'] text-2xl font-bold text-[#0A1A4A]">{title}</h4>
      <div className="mt-6 flex items-end gap-3">
        <span className="font-['Syne'] text-6xl font-extrabold text-[#0A1A4A]">
          {value}
        </span>
        <span className="pb-2 text-lg text-[#98A2B3]">{helper}</span>
      </div>
    </section>
  );
}

function TrainerListCard({
  title,
  description,
  children,
}: {
  title: string;
  description: string;
  children: ReactNode;
}) {
  return (
    <section className="rounded-[28px] border border-[#DCE3F6] bg-white p-7">
      <h4 className="font-['Syne'] text-2xl font-bold text-[#0A1A4A]">{title}</h4>
      <p className="mt-2 text-sm text-[#98A2B3]">{description}</p>
      <div className="mt-6 space-y-4">{children}</div>
    </section>
  );
}

function TrainerRow({
  title,
  subtitle,
}: {
  title: string;
  subtitle: string;
}) {
  return (
    <div className="rounded-2xl border border-[#EEF2F7] bg-[#F8FAFC] px-5 py-4">
      <p className="text-base font-semibold text-[#0A1A4A]">{title}</p>
      <p className="mt-2 text-sm text-[#667085]">{subtitle}</p>
    </div>
  );
}

interface DesktopTrainerDashboardProps {
  userId: number;
  userName: string;
  onLogout: () => void;
}

function isToday(dateValue: string) {
  const today = new Date();
  const date = new Date(dateValue);

  return (
    date.getFullYear() === today.getFullYear() &&
    date.getMonth() === today.getMonth() &&
    date.getDate() === today.getDate()
  );
}

function isCurrentMonth(dateValue: string) {
  const today = new Date();
  const date = new Date(dateValue);

  return (
    date.getFullYear() === today.getFullYear() &&
    date.getMonth() === today.getMonth()
  );
}

function formatHour(hour: string) {
  return hour.slice(0, 5);
}

function buildTodaySessions(sessions: TrainerClassSession[]) {
  return sessions
    .filter((session) => isToday(session.fecha))
    .sort((first, second) => first.horaInicio.localeCompare(second.horaInicio));
}

function buildHighlightedClients(
  clients: TrainerClient[],
  assignments: TrainerAssignment[],
) {
  const activeClientIds = new Set(assignments.map((assignment) => assignment.clientId));

  return clients.filter((client) => activeClientIds.has(client.id)).slice(0, 4);
}

function countAssignedClients(assignments: TrainerAssignment[]) {
  return new Set(assignments.map((assignment) => assignment.clientId)).size;
}

function countMonthlySessions(sessions: TrainerClassSession[]) {
  return sessions.filter((session) => isCurrentMonth(session.fecha)).length;
}

function formatSessionSubtitle(session: TrainerClassSession) {
  return `${formatHour(session.horaInicio)} - ${formatHour(session.horaFin)} - ${session.fecha}`;
}

export function DesktopTrainerDashboard({
  userId,
  userName,
  onLogout,
}: DesktopTrainerDashboardProps) {
  const [activeSection, setActiveSection] = useState<TrainerSection>('resumen');
  const [data, setData] = useState<TrainerDashboardData | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  useEffect(() => {
    const loadDashboard = async () => {
      try {
        setIsLoading(true);
        setErrorMessage(null);
        const dashboardData = await getTrainerDashboardData(userId);
        setData(dashboardData);
      } catch (error) {
        setErrorMessage(
          error instanceof Error
            ? error.message
            : 'No se ha podido cargar el panel de entrenador.',
        );
      } finally {
        setIsLoading(false);
      }
    };

    void loadDashboard();
  }, [userId]);

  const todaySessions = data ? buildTodaySessions(data.sessions) : [];
  const highlightedClients = data
    ? buildHighlightedClients(data.clients, data.assignments)
    : [];
  const assignedClients = data ? countAssignedClients(data.assignments) : 0;
  const monthlySessions = data ? countMonthlySessions(data.sessions) : 0;

  const renderSummary = () => (
    <div className="grid grid-cols-[1.1fr_0.9fr] gap-6">
      <section className="rounded-[28px] border border-[#DCE3F6] bg-white p-7">
        <div className="mb-6 flex items-center justify-between">
          <h4 className="font-['Syne'] text-2xl font-bold text-[#0A1A4A]">
            Clases de hoy
          </h4>
          <div className="rounded-full bg-[#EEF2FF] px-4 py-2 text-sm font-semibold text-[#2266FF]">
            {isLoading ? '...' : `${todaySessions.length} sesiones`}
          </div>
        </div>

        <div className="space-y-4">
          {isLoading ? (
            <TrainerRow
              subtitle="Estamos leyendo las sesiones del entrenador."
              title="Cargando sesiones"
            />
          ) : todaySessions.length > 0 ? (
            todaySessions.map((session) => (
              <TrainerRow
                key={session.id}
                subtitle={formatSessionSubtitle(session)}
                title={session.gymClassName}
              />
            ))
          ) : (
            <TrainerRow
              subtitle="No tienes sesiones programadas para hoy."
              title="Sin sesiones hoy"
            />
          )}
        </div>

        <button className="mt-6 w-full rounded-[20px] bg-[#2266FF] px-5 py-4 font-['Syne'] text-lg font-bold text-white">
          Escanear QR de sesion
        </button>
      </section>

      <div className="space-y-6">
        <section className="rounded-[28px] border border-[#DCE3F6] bg-white p-7">
          <div className="mb-6 flex items-center justify-between">
            <h4 className="font-['Syne'] text-2xl font-bold text-[#0A1A4A]">
              Mis clientes
            </h4>
            <div className="rounded-full bg-[#FFF0EC] px-4 py-2 text-sm font-semibold text-[#FF4D2E]">
              {isLoading ? '...' : `${assignedClients} activos`}
            </div>
          </div>
          <div className="flex flex-wrap gap-3">
            {isLoading ? (
              <div className="rounded-full bg-[#EEF2FF] px-4 py-2 text-sm text-[#2266FF]">
                Cargando clientes
              </div>
            ) : highlightedClients.length > 0 ? (
              highlightedClients.map((client) => (
                <div
                  key={client.id}
                  className="rounded-full bg-[#EEF2FF] px-4 py-2 text-sm text-[#2266FF]"
                >
                  {client.nombre}
                </div>
              ))
            ) : (
              <div className="rounded-full bg-[#F5F5F5] px-4 py-2 text-sm text-[#667085]">
                Aun no hay clientes asignados
              </div>
            )}
          </div>
        </section>

        <TrainerInfoCard
          helper={isLoading ? 'cargando...' : '/ mes actual'}
          title="Clases este mes"
          value={isLoading ? '...' : String(monthlySessions)}
        />

        <TrainerInfoCard
          helper={isLoading ? 'cargando...' : 'asignadas a clientes'}
          title="Rutinas activas"
          value={isLoading ? '...' : String(data?.routines.length ?? 0)}
        />
      </div>
    </div>
  );

  const renderSessions = () => (
    <TrainerListCard
      description="Sesiones de clase vinculadas a las clases que diriges."
      title="Sesiones"
    >
      {isLoading ? (
        <TrainerRow
          subtitle="Estamos cargando las sesiones disponibles."
          title="Cargando sesiones"
        />
      ) : data && data.sessions.length > 0 ? (
        data.sessions.map((session) => (
          <TrainerRow
            key={session.id}
            subtitle={formatSessionSubtitle(session)}
            title={session.gymClassName}
          />
        ))
      ) : (
        <TrainerRow
          subtitle="Todavia no hay sesiones registradas para tus clases."
          title="Sin sesiones"
        />
      )}
    </TrainerListCard>
  );

  const renderClients = () => (
    <TrainerListCard
      description="Clientes que tienen rutinas activas contigo."
      title="Clientes"
    >
      {isLoading ? (
        <TrainerRow
          subtitle="Estamos cargando tus clientes."
          title="Cargando clientes"
        />
      ) : highlightedClients.length > 0 ? (
        highlightedClients.map((client) => (
          <TrainerRow
            key={client.id}
            subtitle={`${client.email} - ${client.activo ? 'activo' : 'inactivo'}`}
            title={client.nombre}
          />
        ))
      ) : (
        <TrainerRow
          subtitle="Aun no tienes clientes asociados a tus rutinas."
          title="Sin clientes"
        />
      )}
    </TrainerListCard>
  );

  const renderRoutines = () => (
    <TrainerListCard
      description="Rutinas creadas y mantenidas por este entrenador."
      title="Rutinas"
    >
      {isLoading ? (
        <TrainerRow
          subtitle="Estamos cargando tus rutinas."
          title="Cargando rutinas"
        />
      ) : data && data.routines.length > 0 ? (
        data.routines.map((routine: TrainerRoutine) => (
          <TrainerRow
            key={routine.id}
            subtitle={routine.descripcion || 'Rutina sin descripcion detallada'}
            title={routine.nombre}
          />
        ))
      ) : (
        <TrainerRow
          subtitle="Todavia no hay rutinas asignadas a este entrenador."
          title="Sin rutinas"
        />
      )}
    </TrainerListCard>
  );

  const renderProfile = () => (
    <TrainerListCard
      description="Resumen sencillo del rol y del alcance del panel web."
      title="Perfil"
    >
      <TrainerRow
        subtitle="Acceso a sesiones, clientes y rutinas relacionadas contigo."
        title={userName}
      />
      <TrainerRow
        subtitle="El escaneo QR puede existir en web como apoyo, pero el uso principal sigue teniendo mas sentido en la app movil."
        title="Rol: Entrenador"
      />
    </TrainerListCard>
  );

  const renderSection = () => {
    switch (activeSection) {
      case 'sesiones':
        return renderSessions();
      case 'clientes':
        return renderClients();
      case 'rutinas':
        return renderRoutines();
      case 'perfil':
        return renderProfile();
      case 'resumen':
      default:
        return renderSummary();
    }
  };

  const sectionTitles: Record<TrainerSection, string> = {
    clientes: 'Clientes',
    perfil: 'Perfil',
    resumen: 'Resumen',
    rutinas: 'Rutinas',
    sesiones: 'Sesiones',
  };

  return (
    <div className="overflow-hidden rounded-[32px] border border-[#DCE3F6] bg-[#EEF2FF] shadow-[0_24px_60px_rgba(15,23,42,0.10)]">
      <div className="grid min-h-[760px] grid-cols-[240px_1fr]">
        <aside className="bg-[#0A1A4A] px-5 py-7">
          <h2 className="font-['Syne'] text-3xl font-extrabold text-white">
            Hazel Gym
          </h2>
          <p className="mt-2 text-sm text-[#C7D7FE]">Panel entrenador</p>
          <div className="mt-10 space-y-3">
            <TrainerSidebarItem
              active={activeSection === 'resumen'}
              label="Resumen"
              onClick={() => setActiveSection('resumen')}
            />
            <TrainerSidebarItem
              active={activeSection === 'sesiones'}
              label="Sesiones"
              onClick={() => setActiveSection('sesiones')}
            />
            <TrainerSidebarItem
              active={activeSection === 'clientes'}
              label="Clientes"
              onClick={() => setActiveSection('clientes')}
            />
            <TrainerSidebarItem
              active={activeSection === 'rutinas'}
              label="Rutinas"
              onClick={() => setActiveSection('rutinas')}
            />
            <TrainerSidebarItem
              active={activeSection === 'perfil'}
              label="Perfil"
              onClick={() => setActiveSection('perfil')}
            />
          </div>
        </aside>

        <main className="p-10">
          <div className="mb-9 flex items-center justify-between gap-6">
            <div>
              <p className="text-sm text-[#667085]">Panel de entrenador</p>
              <h3 className="font-['Syne'] text-5xl font-extrabold text-[#0A1A4A]">
                {userName}
              </h3>
              <p className="mt-3 text-sm text-[#98A2B3]">
                Seccion actual: {sectionTitles[activeSection]}
              </p>
            </div>
            <div className="flex items-center gap-3">
              <div className="rounded-full bg-[#DBEAFE] px-5 py-3 text-sm font-semibold text-[#1D4ED8]">
                Entrenador
              </div>
              <button
                className="rounded-full bg-[#0A1A4A] px-5 py-3 text-sm font-semibold text-white"
                onClick={onLogout}
              >
                Cerrar sesion
              </button>
            </div>
          </div>

          {errorMessage ? (
            <div className="mb-6 rounded-[22px] border border-[#AFC8FF] bg-[#F4F8FF] px-5 py-4 text-sm text-[#1D4ED8]">
              {errorMessage}
            </div>
          ) : null}

          {renderSection()}
        </main>
      </div>
    </div>
  );
}
