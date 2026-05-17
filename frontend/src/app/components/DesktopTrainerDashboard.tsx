import { useEffect, useState } from 'react';
import type { ReactNode } from 'react';
import {
  createClassSession,
  createRoutineAssignment,
  createTrainerRoutine,
  deleteClassSession,
  deleteRoutineAssignment,
  deleteTrainerRoutine,
  getTrainerDashboardData,
  updateClassSession,
  updateTrainerRoutine,
} from '../services/trainerDashboardService';
import type {
  TrainerAssignmentDraft,
  TrainerAssignment,
  TrainerClassSessionDraft,
  TrainerClassSession,
  TrainerClient,
  TrainerDashboardData,
  TrainerRoutineDraft,
  TrainerRoutine,
} from '../types/trainer';

type TrainerSection = 'resumen' | 'sesiones' | 'clientes' | 'rutinas' | 'perfil';

const EMPTY_ASSIGNMENT_DRAFT: TrainerAssignmentDraft = {
  clientId: '',
  routineId: '',
};

const EMPTY_ROUTINE_DRAFT: TrainerRoutineDraft = {
  descripcion: '',
  nombre: '',
};

const EMPTY_CLASS_SESSION_DRAFT: TrainerClassSessionDraft = {
  fecha: '',
  gymClassId: '',
  horaFin: '',
  horaInicio: '',
};

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
      className={`w-full rounded-2xl border px-4 py-3 text-left text-sm transition duration-200 ${
        active
          ? 'border-[#79A1FF] bg-[#2266FF] font-semibold text-white shadow-[0_12px_24px_rgba(34,102,255,0.30)]'
          : 'border-[#213067] bg-[#10245E] text-[#D8E4FF] hover:bg-[#173276]'
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
    <section className="rounded-[28px] border border-white/70 bg-white/88 p-5 shadow-[0_24px_50px_rgba(29,78,216,0.10)] backdrop-blur-sm sm:p-6 xl:p-7">
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
    <section className="rounded-[28px] border border-white/70 bg-white/88 p-5 shadow-[0_24px_50px_rgba(29,78,216,0.10)] backdrop-blur-sm sm:p-6 xl:p-7">
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
    <div className="rounded-2xl border border-[#E3E9F8] bg-[#F8FAFC] px-5 py-4 shadow-[0_10px_24px_rgba(29,78,216,0.05)]">
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

function formatAssignmentDate(value: string) {
  return new Intl.DateTimeFormat('es-ES', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  }).format(new Date(value));
}

function sessionToDraft(session: TrainerClassSession): TrainerClassSessionDraft {
  return {
    fecha: session.fecha,
    gymClassId: String(session.gymClassId),
    horaFin: session.horaFin.slice(0, 5),
    horaInicio: session.horaInicio.slice(0, 5),
  };
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
  const [clientSearch, setClientSearch] = useState('');
  const [selectedClientId, setSelectedClientId] = useState<number | null>(null);
  const [selectedAssignmentId, setSelectedAssignmentId] = useState<number | null>(null);
  const [assignmentDraft, setAssignmentDraft] =
    useState<TrainerAssignmentDraft>(EMPTY_ASSIGNMENT_DRAFT);
  const [assignmentMessage, setAssignmentMessage] = useState<string | null>(null);
  const [isSavingAssignment, setIsSavingAssignment] = useState(false);
  const [routineSearch, setRoutineSearch] = useState('');
  const [selectedRoutineId, setSelectedRoutineId] = useState<number | null>(null);
  const [routineDraft, setRoutineDraft] = useState<TrainerRoutineDraft>(EMPTY_ROUTINE_DRAFT);
  const [routineMessage, setRoutineMessage] = useState<string | null>(null);
  const [isSavingRoutine, setIsSavingRoutine] = useState(false);
  const [sessionSearch, setSessionSearch] = useState('');
  const [selectedSessionId, setSelectedSessionId] = useState<number | null>(null);
  const [sessionDraft, setSessionDraft] =
    useState<TrainerClassSessionDraft>(EMPTY_CLASS_SESSION_DRAFT);
  const [sessionMessage, setSessionMessage] = useState<string | null>(null);
  const [isSavingSession, setIsSavingSession] = useState(false);

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

  useEffect(() => {
    void loadDashboard();
  }, [userId]);

  const todaySessions = data ? buildTodaySessions(data.sessions) : [];
  const highlightedClients = data
    ? buildHighlightedClients(data.clients, data.assignments)
    : [];
  const assignedClients = data ? countAssignedClients(data.assignments) : 0;
  const monthlySessions = data ? countMonthlySessions(data.sessions) : 0;
  const filteredClients = data
    ? data.clients.filter((client) =>
        [client.nombre, client.email, String(client.id)]
          .join(' ')
          .toLowerCase()
          .includes(clientSearch.trim().toLowerCase()),
      )
    : [];
  const filteredRoutines = data
    ? data.routines.filter((routine) =>
        [routine.nombre, routine.descripcion ?? '', String(routine.id)]
          .join(' ')
          .toLowerCase()
          .includes(routineSearch.trim().toLowerCase()),
      )
    : [];
  const filteredSessions = data
    ? data.sessions.filter((session) =>
        [session.gymClassName, session.fecha, session.horaInicio, session.horaFin, String(session.id)]
          .join(' ')
          .toLowerCase()
          .includes(sessionSearch.trim().toLowerCase()),
      )
    : [];
  const selectedClient =
    data?.clients.find((client) => client.id === selectedClientId) ?? null;
  const selectedClientAssignments = data
    ? data.assignments.filter((assignment) => assignment.clientId === selectedClientId)
    : [];

  const selectRoutine = (routine: TrainerRoutine) => {
    setSelectedRoutineId(routine.id);
    setRoutineDraft({
      descripcion: routine.descripcion ?? '',
      nombre: routine.nombre,
    });
    setRoutineMessage(`Editando la rutina ${routine.nombre}.`);
  };

  const selectSession = (session: TrainerClassSession) => {
    setSelectedSessionId(session.id);
    setSessionDraft(sessionToDraft(session));
    setSessionMessage(`Editando la sesion de ${session.gymClassName}.`);
  };

  const resetRoutineEditor = () => {
    setSelectedRoutineId(null);
    setRoutineDraft(EMPTY_ROUTINE_DRAFT);
    setRoutineMessage(null);
    setRoutineSearch('');
  };

  const resetSessionEditor = () => {
    setSelectedSessionId(null);
    setSessionDraft(EMPTY_CLASS_SESSION_DRAFT);
    setSessionMessage(null);
    setSessionSearch('');
  };

  const selectClient = (client: TrainerClient) => {
    setSelectedClientId(client.id);
    setAssignmentDraft((current) => ({ ...current, clientId: String(client.id) }));
    setSelectedAssignmentId(null);
    setAssignmentMessage(`Cliente seleccionado: ${client.nombre}.`);
  };

  const resetClientEditor = () => {
    setSelectedClientId(null);
    setSelectedAssignmentId(null);
    setAssignmentDraft(EMPTY_ASSIGNMENT_DRAFT);
    setAssignmentMessage(null);
    setClientSearch('');
  };

  const handleAssignmentSave = async () => {
    if (!assignmentDraft.clientId) {
      setAssignmentMessage('Selecciona un cliente antes de asignar una rutina.');
      return;
    }

    if (!assignmentDraft.routineId) {
      setAssignmentMessage('Selecciona una rutina antes de guardar.');
      return;
    }

    try {
      setIsSavingAssignment(true);
      setAssignmentMessage(null);
      await createRoutineAssignment(assignmentDraft);
      await loadDashboard();
      setSelectedAssignmentId(null);
      setAssignmentDraft((current) => ({
        ...current,
        routineId: '',
      }));
      setAssignmentMessage('Rutina asignada correctamente.');
    } catch (error) {
      setAssignmentMessage(
        error instanceof Error
          ? error.message
          : 'No se ha podido asignar la rutina.',
      );
    } finally {
      setIsSavingAssignment(false);
    }
  };

  const handleAssignmentDelete = async () => {
    if (!selectedAssignmentId) {
      setAssignmentMessage('Selecciona una asignacion antes de intentar eliminarla.');
      return;
    }

    try {
      setIsSavingAssignment(true);
      setAssignmentMessage(null);
      await deleteRoutineAssignment(selectedAssignmentId);
      await loadDashboard();
      setSelectedAssignmentId(null);
      setAssignmentMessage('Asignacion eliminada correctamente.');
    } catch (error) {
      setAssignmentMessage(
        error instanceof Error
          ? error.message
          : 'No se ha podido eliminar la asignacion.',
      );
    } finally {
      setIsSavingAssignment(false);
    }
  };

  const handleRoutineSave = async () => {
    if (!routineDraft.nombre.trim()) {
      setRoutineMessage('El nombre de la rutina es obligatorio.');
      return;
    }

    try {
      setIsSavingRoutine(true);
      setRoutineMessage(null);

      if (selectedRoutineId) {
        await updateTrainerRoutine(selectedRoutineId, userId, routineDraft);
        setRoutineMessage('Rutina actualizada correctamente.');
      } else {
        await createTrainerRoutine(userId, routineDraft);
        setRoutineMessage('Rutina creada correctamente.');
      }

      await loadDashboard();
      setSelectedRoutineId(null);
      setRoutineDraft(EMPTY_ROUTINE_DRAFT);
      setRoutineSearch('');
    } catch (error) {
      setRoutineMessage(
        error instanceof Error ? error.message : 'No se ha podido guardar la rutina.',
      );
    } finally {
      setIsSavingRoutine(false);
    }
  };

  const handleRoutineDelete = async () => {
    if (!selectedRoutineId) {
      setRoutineMessage('Selecciona una rutina antes de intentar eliminarla.');
      return;
    }

    try {
      setIsSavingRoutine(true);
      setRoutineMessage(null);
      await deleteTrainerRoutine(selectedRoutineId);
      await loadDashboard();
      resetRoutineEditor();
      setRoutineMessage('Rutina eliminada correctamente.');
    } catch (error) {
      setRoutineMessage(
        error instanceof Error ? error.message : 'No se ha podido eliminar la rutina.',
      );
    } finally {
      setIsSavingRoutine(false);
    }
  };

  const handleSessionSave = async () => {
    if (!sessionDraft.gymClassId) {
      setSessionMessage('Selecciona una clase antes de guardar la sesion.');
      return;
    }

    if (!sessionDraft.fecha || !sessionDraft.horaInicio || !sessionDraft.horaFin) {
      setSessionMessage('Fecha y horas son obligatorias para la sesion.');
      return;
    }

    if (sessionDraft.horaFin <= sessionDraft.horaInicio) {
      setSessionMessage('La hora de fin debe ser posterior a la de inicio.');
      return;
    }

    try {
      setIsSavingSession(true);
      setSessionMessage(null);

      if (selectedSessionId) {
        await updateClassSession(selectedSessionId, sessionDraft);
        setSessionMessage('Sesion actualizada correctamente.');
      } else {
        await createClassSession(sessionDraft);
        setSessionMessage('Sesion creada correctamente.');
      }

      await loadDashboard();
      setSelectedSessionId(null);
      setSessionDraft(EMPTY_CLASS_SESSION_DRAFT);
      setSessionSearch('');
    } catch (error) {
      setSessionMessage(
        error instanceof Error ? error.message : 'No se ha podido guardar la sesion.',
      );
    } finally {
      setIsSavingSession(false);
    }
  };

  const handleSessionDelete = async () => {
    if (!selectedSessionId) {
      setSessionMessage('Selecciona una sesion antes de intentar eliminarla.');
      return;
    }

    try {
      setIsSavingSession(true);
      setSessionMessage(null);
      await deleteClassSession(selectedSessionId);
      await loadDashboard();
      resetSessionEditor();
      setSessionMessage('Sesion eliminada correctamente.');
    } catch (error) {
      setSessionMessage(
        error instanceof Error ? error.message : 'No se ha podido eliminar la sesion.',
      );
    } finally {
      setIsSavingSession(false);
    }
  };

  const renderSummary = () => (
    <div className="grid grid-cols-1 gap-6 2xl:grid-cols-[1.1fr_0.9fr]">
      <section className="rounded-[28px] border border-[#DCE3F6] bg-white p-5 sm:p-6 xl:p-7">
        <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
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
        <section className="rounded-[28px] border border-[#DCE3F6] bg-white p-5 sm:p-6 xl:p-7">
          <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
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
    <div className="grid grid-cols-1 gap-6 2xl:grid-cols-[0.95fr_1.05fr]">
      <TrainerListCard
        description="Busca una sesion existente y selecciona la que quieras revisar o editar."
        title="Sesiones de clase"
      >
        <label className="block">
          <span className="mb-2 block text-sm font-semibold text-[#667085]">
            Buscar sesion
          </span>
          <input
            className="w-full rounded-2xl border border-[#DCE3F6] bg-[#F8FAFC] px-4 py-3 text-sm text-[#0A1A4A] outline-none transition focus:border-[#2266FF]"
            placeholder="Clase, fecha, hora o ID"
            value={sessionSearch}
            onChange={(event) => setSessionSearch(event.target.value)}
          />
        </label>

        {isLoading ? (
          <TrainerRow
            subtitle="Estamos cargando las sesiones disponibles."
            title="Cargando sesiones"
          />
        ) : filteredSessions.length > 0 ? (
          filteredSessions.map((session) => (
            <button
              key={session.id}
              className={`w-full rounded-[20px] border px-5 py-4 text-left transition ${
                selectedSessionId === session.id
                  ? 'border-[#2266FF] bg-[#EEF2FF]'
                  : 'border-[#EEF2F7] bg-[#F8FAFC]'
              }`}
              onClick={() => selectSession(session)}
              type="button"
            >
              <p className="text-base font-semibold text-[#0A1A4A]">
                {session.gymClassName}
              </p>
              <p className="mt-2 text-sm text-[#667085]">
                {formatSessionSubtitle(session)}
              </p>
            </button>
          ))
        ) : (
          <TrainerRow
            subtitle="Todavia no hay sesiones registradas para tus clases."
            title="Sin sesiones"
          />
        )}
      </TrainerListCard>

      <TrainerListCard
        description="Programa nuevas sesiones o actualiza las ya creadas para tus clases."
        title={selectedSessionId ? 'Editar sesion' : 'Nueva sesion'}
      >
        <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          <div className="rounded-full bg-[#EEF2FF] px-4 py-2 text-sm font-semibold text-[#1D4ED8]">
            {selectedSessionId ? 'Modo edicion' : 'Modo alta'}
          </div>
          <button
            className="rounded-full bg-[#F3F4F6] px-4 py-2 text-sm font-semibold text-[#374151]"
            onClick={resetSessionEditor}
            type="button"
          >
            Limpiar
          </button>
        </div>

        <label className="block">
          <span className="mb-2 block text-sm font-semibold text-[#667085]">Clase</span>
          <select
            className="w-full rounded-2xl border border-[#DCE3F6] bg-[#F8FAFC] px-4 py-3 text-sm text-[#0A1A4A] outline-none transition focus:border-[#2266FF]"
            value={sessionDraft.gymClassId}
            onChange={(event) =>
              setSessionDraft((current) => ({
                ...current,
                gymClassId: event.target.value,
              }))
            }
          >
            <option value="">Selecciona una clase</option>
            {data?.classes.map((gymClass) => (
              <option key={gymClass.id} value={gymClass.id}>
                {gymClass.nombre} · ID {gymClass.id}
              </option>
            ))}
          </select>
        </label>

        <div className="grid grid-cols-1 gap-4 lg:grid-cols-3">
          <label className="block">
            <span className="mb-2 block text-sm font-semibold text-[#667085]">Fecha</span>
            <input
              className="w-full rounded-2xl border border-[#DCE3F6] bg-[#F8FAFC] px-4 py-3 text-sm text-[#0A1A4A] outline-none transition focus:border-[#2266FF]"
              type="date"
              value={sessionDraft.fecha}
              onChange={(event) =>
                setSessionDraft((current) => ({
                  ...current,
                  fecha: event.target.value,
                }))
              }
            />
          </label>
          <label className="block">
            <span className="mb-2 block text-sm font-semibold text-[#667085]">
              Hora inicio
            </span>
            <input
              className="w-full rounded-2xl border border-[#DCE3F6] bg-[#F8FAFC] px-4 py-3 text-sm text-[#0A1A4A] outline-none transition focus:border-[#2266FF]"
              type="time"
              value={sessionDraft.horaInicio}
              onChange={(event) =>
                setSessionDraft((current) => ({
                  ...current,
                  horaInicio: event.target.value,
                }))
              }
            />
          </label>
          <label className="block">
            <span className="mb-2 block text-sm font-semibold text-[#667085]">
              Hora fin
            </span>
            <input
              className="w-full rounded-2xl border border-[#DCE3F6] bg-[#F8FAFC] px-4 py-3 text-sm text-[#0A1A4A] outline-none transition focus:border-[#2266FF]"
              type="time"
              value={sessionDraft.horaFin}
              onChange={(event) =>
                setSessionDraft((current) => ({
                  ...current,
                  horaFin: event.target.value,
                }))
              }
            />
          </label>
        </div>

        {sessionMessage ? (
          <div className="rounded-[20px] border border-[#C7D7FE] bg-[#F4F8FF] px-4 py-3 text-sm text-[#1D4ED8]">
            {sessionMessage}
          </div>
        ) : null}

        <div className="flex flex-wrap gap-3">
          <button
            className="rounded-full bg-[#2266FF] px-5 py-3 text-sm font-semibold text-white disabled:cursor-not-allowed disabled:opacity-60"
            disabled={isSavingSession}
            onClick={() => void handleSessionSave()}
            type="button"
          >
            {isSavingSession
              ? 'Guardando...'
              : selectedSessionId
                ? 'Guardar cambios'
                : 'Crear sesion'}
          </button>
          <button
            className="rounded-full bg-[#0A1A4A] px-5 py-3 text-sm font-semibold text-white disabled:cursor-not-allowed disabled:opacity-60"
            disabled={isSavingSession || !selectedSessionId}
            onClick={() => void handleSessionDelete()}
            type="button"
          >
            Eliminar sesion
          </button>
        </div>
      </TrainerListCard>
    </div>
  );

  const renderClients = () => (
    <div className="grid grid-cols-1 gap-6 2xl:grid-cols-[0.95fr_1.05fr]">
      <TrainerListCard
        description="Selecciona un cliente para revisar sus rutinas asignadas o anadir una nueva."
        title="Clientes"
      >
        <label className="block">
          <span className="mb-2 block text-sm font-semibold text-[#667085]">
            Buscar cliente
          </span>
          <input
            className="w-full rounded-2xl border border-[#DCE3F6] bg-[#F8FAFC] px-4 py-3 text-sm text-[#0A1A4A] outline-none transition focus:border-[#2266FF]"
            placeholder="Nombre, email o ID"
            value={clientSearch}
            onChange={(event) => setClientSearch(event.target.value)}
          />
        </label>

        {isLoading ? (
          <TrainerRow
            subtitle="Estamos cargando tus clientes."
            title="Cargando clientes"
          />
        ) : filteredClients.length > 0 ? (
          filteredClients.map((client) => {
            const totalAssignments = data?.assignments.filter(
              (assignment) => assignment.clientId === client.id,
            ).length;

            return (
              <button
                key={client.id}
                className={`w-full rounded-[20px] border px-5 py-4 text-left transition ${
                  selectedClientId === client.id
                    ? 'border-[#2266FF] bg-[#EEF2FF]'
                    : 'border-[#EEF2F7] bg-[#F8FAFC]'
                }`}
                onClick={() => selectClient(client)}
                type="button"
              >
                <div className="flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between">
                  <div>
                    <p className="text-base font-semibold text-[#0A1A4A]">{client.nombre}</p>
                    <p className="mt-2 text-sm text-[#667085]">{client.email}</p>
                  </div>
                  <div className="flex flex-wrap gap-2">
                    <span className="rounded-full bg-[#DBEAFE] px-3 py-1 text-xs font-semibold text-[#1D4ED8]">
                      {totalAssignments} rutinas
                    </span>
                    <span
                      className={`rounded-full px-3 py-1 text-xs font-semibold ${
                        client.activo
                          ? 'bg-[#DCFCE7] text-[#166534]'
                          : 'bg-[#F3F4F6] text-[#6B7280]'
                      }`}
                    >
                      {client.activo ? 'Activo' : 'Inactivo'}
                    </span>
                  </div>
                </div>
              </button>
            );
          })
        ) : (
          <TrainerRow
            subtitle="No hay clientes que coincidan con la busqueda actual."
            title="Sin resultados"
          />
        )}
      </TrainerListCard>

      <TrainerListCard
        description="Asigna una rutina al cliente seleccionado o elimina una asignacion activa."
        title={selectedClient ? `Rutinas de ${selectedClient.nombre}` : 'Gestion de rutinas'}
      >
        <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          <div className="rounded-full bg-[#EEF2FF] px-4 py-2 text-sm font-semibold text-[#1D4ED8]">
            {selectedClient ? 'Cliente seleccionado' : 'Sin cliente seleccionado'}
          </div>
          <button
            className="rounded-full bg-[#F3F4F6] px-4 py-2 text-sm font-semibold text-[#374151]"
            onClick={resetClientEditor}
            type="button"
          >
            Limpiar
          </button>
        </div>

        {selectedClient ? (
          <div className="rounded-2xl border border-[#DCE3F6] bg-[#F8FAFC] px-5 py-4">
            <p className="text-base font-semibold text-[#0A1A4A]">{selectedClient.nombre}</p>
            <p className="mt-2 text-sm text-[#667085]">{selectedClient.email}</p>
          </div>
        ) : (
          <TrainerRow
            subtitle="Selecciona un cliente del listado para empezar a trabajar."
            title="Todavia no has elegido un cliente"
          />
        )}

        <label className="block">
          <span className="mb-2 block text-sm font-semibold text-[#667085]">
            Rutina a asignar
          </span>
          <select
            className="w-full rounded-2xl border border-[#DCE3F6] bg-[#F8FAFC] px-4 py-3 text-sm text-[#0A1A4A] outline-none transition focus:border-[#2266FF]"
            value={assignmentDraft.routineId}
            onChange={(event) =>
              setAssignmentDraft((current) => ({
                ...current,
                routineId: event.target.value,
              }))
            }
          >
            <option value="">Selecciona una rutina</option>
            {data?.routines.map((routine) => (
              <option key={routine.id} value={routine.id}>
                {routine.nombre} · ID {routine.id}
              </option>
            ))}
          </select>
        </label>

        <div className="space-y-4">
          {selectedClientAssignments.length > 0 ? (
            selectedClientAssignments.map((assignment) => (
              <button
                key={assignment.id}
                className={`w-full rounded-2xl border px-5 py-4 text-left transition ${
                  selectedAssignmentId === assignment.id
                    ? 'border-[#2266FF] bg-[#EEF2FF]'
                    : 'border-[#EEF2F7] bg-[#F8FAFC]'
                }`}
                onClick={() => {
                  setSelectedAssignmentId(assignment.id);
                  setAssignmentMessage(
                    `Asignacion seleccionada: ${assignment.routineName}.`,
                  );
                }}
                type="button"
              >
                <p className="text-base font-semibold text-[#0A1A4A]">
                  {assignment.routineName}
                </p>
                <p className="mt-2 text-sm text-[#667085]">
                  Asignada el {formatAssignmentDate(assignment.fechaAsignacion)}
                </p>
              </button>
            ))
          ) : (
            <TrainerRow
              subtitle="Este cliente todavia no tiene rutinas activas contigo."
              title="Sin asignaciones"
            />
          )}
        </div>

        {assignmentMessage ? (
          <div className="rounded-[20px] border border-[#C7D7FE] bg-[#F4F8FF] px-4 py-3 text-sm text-[#1D4ED8]">
            {assignmentMessage}
          </div>
        ) : null}

        <div className="flex flex-wrap gap-3">
          <button
            className="rounded-full bg-[#2266FF] px-5 py-3 text-sm font-semibold text-white disabled:cursor-not-allowed disabled:opacity-60"
            disabled={isSavingAssignment}
            onClick={() => void handleAssignmentSave()}
            type="button"
          >
            {isSavingAssignment ? 'Guardando...' : 'Asignar rutina'}
          </button>
          <button
            className="rounded-full bg-[#0A1A4A] px-5 py-3 text-sm font-semibold text-white disabled:cursor-not-allowed disabled:opacity-60"
            disabled={isSavingAssignment || !selectedAssignmentId}
            onClick={() => void handleAssignmentDelete()}
            type="button"
          >
            Eliminar asignacion
          </button>
        </div>
      </TrainerListCard>
    </div>
  );

  const renderRoutines = () => (
    <div className="grid grid-cols-1 gap-6 2xl:grid-cols-[0.95fr_1.05fr]">
      <TrainerListCard
        description="Busca una rutina existente o selecciona una para editarla."
        title="Rutinas del entrenador"
      >
        <label className="block">
          <span className="mb-2 block text-sm font-semibold text-[#667085]">
            Buscar rutina
          </span>
          <input
            className="w-full rounded-2xl border border-[#DCE3F6] bg-[#F8FAFC] px-4 py-3 text-sm text-[#0A1A4A] outline-none transition focus:border-[#2266FF]"
            placeholder="Nombre, descripcion o ID"
            value={routineSearch}
            onChange={(event) => setRoutineSearch(event.target.value)}
          />
        </label>

        {isLoading ? (
          <TrainerRow
            subtitle="Estamos cargando tus rutinas."
            title="Cargando rutinas"
          />
        ) : filteredRoutines.length > 0 ? (
          filteredRoutines.map((routine: TrainerRoutine) => (
            <button
              key={routine.id}
              className={`w-full rounded-[20px] border px-5 py-4 text-left transition ${
                selectedRoutineId === routine.id
                  ? 'border-[#2266FF] bg-[#EEF2FF]'
                  : 'border-[#EEF2F7] bg-[#F8FAFC]'
              }`}
              onClick={() => selectRoutine(routine)}
              type="button"
            >
              <p className="text-base font-semibold text-[#0A1A4A]">{routine.nombre}</p>
              <p className="mt-2 text-sm text-[#667085]">
                {routine.descripcion || 'Rutina sin descripcion detallada'}
              </p>
            </button>
          ))
        ) : (
          <TrainerRow
            subtitle="No hay rutinas que coincidan con la busqueda actual."
            title="Sin resultados"
          />
        )}
      </TrainerListCard>

      <TrainerListCard
        description="Crea nuevas rutinas o actualiza las que ya usas con tus clientes."
        title={selectedRoutineId ? 'Editar rutina' : 'Nueva rutina'}
      >
        <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          <div className="rounded-full bg-[#EEF2FF] px-4 py-2 text-sm font-semibold text-[#1D4ED8]">
            {selectedRoutineId ? 'Modo edicion' : 'Modo alta'}
          </div>
          <button
            className="rounded-full bg-[#F3F4F6] px-4 py-2 text-sm font-semibold text-[#374151]"
            onClick={resetRoutineEditor}
            type="button"
          >
            Limpiar
          </button>
        </div>

        <label className="block">
          <span className="mb-2 block text-sm font-semibold text-[#667085]">
            Nombre
          </span>
          <input
            className="w-full rounded-2xl border border-[#DCE3F6] bg-[#F8FAFC] px-4 py-3 text-sm text-[#0A1A4A] outline-none transition focus:border-[#2266FF]"
            placeholder="Full body, fuerza o recuperacion"
            value={routineDraft.nombre}
            onChange={(event) =>
              setRoutineDraft((current) => ({
                ...current,
                nombre: event.target.value,
              }))
            }
          />
        </label>

        <label className="block">
          <span className="mb-2 block text-sm font-semibold text-[#667085]">
            Descripcion
          </span>
          <textarea
            className="min-h-[180px] w-full rounded-2xl border border-[#DCE3F6] bg-[#F8FAFC] px-4 py-3 text-sm text-[#0A1A4A] outline-none transition focus:border-[#2266FF]"
            placeholder="Objetivo de la rutina, estructura o enfoque principal"
            value={routineDraft.descripcion}
            onChange={(event) =>
              setRoutineDraft((current) => ({
                ...current,
                descripcion: event.target.value,
              }))
            }
          />
        </label>

        {routineMessage ? (
          <div className="rounded-[20px] border border-[#C7D7FE] bg-[#F4F8FF] px-4 py-3 text-sm text-[#1D4ED8]">
            {routineMessage}
          </div>
        ) : null}

        <div className="flex flex-wrap gap-3">
          <button
            className="rounded-full bg-[#2266FF] px-5 py-3 text-sm font-semibold text-white disabled:cursor-not-allowed disabled:opacity-60"
            disabled={isSavingRoutine}
            onClick={() => void handleRoutineSave()}
            type="button"
          >
            {isSavingRoutine
              ? 'Guardando...'
              : selectedRoutineId
                ? 'Guardar cambios'
                : 'Crear rutina'}
          </button>
          <button
            className="rounded-full bg-[#0A1A4A] px-5 py-3 text-sm font-semibold text-white disabled:cursor-not-allowed disabled:opacity-60"
            disabled={isSavingRoutine || !selectedRoutineId}
            onClick={() => void handleRoutineDelete()}
            type="button"
          >
            Eliminar rutina
          </button>
        </div>
      </TrainerListCard>
    </div>
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
    <div className="min-h-screen w-full bg-[radial-gradient(circle_at_top_left,_rgba(34,102,255,0.12),_transparent_22%),linear-gradient(180deg,#EEF2FF_0%,#E6EDFF_100%)] text-[#0A1A4A]">
      <div className="grid min-h-screen grid-cols-1 lg:grid-cols-[240px_minmax(0,1fr)]">
        <aside className="border-b border-[#213067] bg-[linear-gradient(180deg,#0A1A4A_0%,#122A70_100%)] px-4 py-6 sm:px-5 sm:py-7 lg:sticky lg:top-0 lg:flex lg:h-screen lg:flex-col lg:justify-between lg:border-r lg:border-b-0">
          <div>
          <h2 className="font-['Syne'] text-3xl font-extrabold text-white">
            Hazel Gym
          </h2>
          <p className="mt-2 text-sm text-[#C7D7FE]">Panel entrenador</p>
          <div className="mt-8 grid grid-cols-2 gap-3 sm:grid-cols-3 lg:mt-10 lg:grid-cols-1">
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
          </div>
          <div className="mt-8 hidden rounded-[22px] border border-[#213067] bg-[#0F235E] p-4 text-sm text-[#C7D7FE] lg:block">
            Organiza sesiones, clientes y rutinas desde una vista amplia de escritorio.
          </div>
        </aside>

        <main className="min-w-0 px-4 py-6 sm:px-6 lg:px-8 xl:p-10">
          <div className="mb-8 rounded-[30px] border border-white/70 bg-white/74 p-5 shadow-[0_24px_55px_rgba(29,78,216,0.10)] backdrop-blur-xl sm:p-6 xl:p-7">
          <div className="flex flex-col gap-4 xl:flex-row xl:items-start xl:justify-between">
            <div>
              <p className="text-sm font-medium uppercase tracking-[0.2em] text-[#667085]">
                Panel de entrenador
              </p>
              <h3 className="font-['Syne'] text-3xl font-extrabold text-[#0A1A4A] sm:text-4xl xl:text-5xl">
                {userName}
              </h3>
              <p className="mt-3 text-sm text-[#98A2B3]">
                Seccion actual: {sectionTitles[activeSection]}
              </p>
            </div>
            <div className="flex flex-wrap items-center gap-3">
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
          </div>

          {errorMessage ? (
            <div className="mb-6 rounded-[22px] border border-[#AFC8FF] bg-[#F4F8FF] px-5 py-4 text-sm text-[#1D4ED8]">
              {errorMessage}
            </div>
          ) : null}

          <div className="space-y-8">{renderSection()}</div>
        </main>
      </div>
    </div>
  );
}
