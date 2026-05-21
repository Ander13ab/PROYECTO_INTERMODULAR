import { readSessionToken } from './authService';
import { API_BASE_URL } from './apiConfig';
import type {
  TrainerAssignmentDraft,
  TrainerAssignment,
  TrainerClassSessionDraft,
  TrainerClassSession,
  TrainerClient,
  TrainerDashboardData,
  TrainerGymClass,
  TrainerRoutineDraft,
  TrainerRoutine,
} from '../types/trainer';

async function fetchWithSession<T>(path: string): Promise<T> {
  const token = readSessionToken();

  if (!token) {
    throw new Error('No hay una sesion activa en el navegador.');
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    if (response.status === 401) {
      throw new Error('La sesion ya no es valida. Inicia sesion de nuevo.');
    }

    if (response.status === 403) {
      throw new Error('Tu usuario no tiene permiso para ver este panel.');
    }

    throw new Error('No se han podido cargar los datos del entrenador.');
  }

  return (await response.json()) as T;
}

export async function getTrainerDashboardData(
  trainerId: number,
): Promise<TrainerDashboardData> {
  const [clients, routines, assignments, classes, sessions] = await Promise.all([
    fetchWithSession<TrainerClient[]>('/api/users'),
    fetchWithSession<TrainerRoutine[]>('/api/routines'),
    fetchWithSession<TrainerAssignment[]>('/api/routine-assignments'),
    fetchWithSession<TrainerGymClass[]>('/api/classes'),
    fetchWithSession<TrainerClassSession[]>('/api/class-sessions'),
  ]);

  const trainerRoutines = routines.filter((routine) => routine.entrenadorId === trainerId);
  const trainerClasses = classes.filter((gymClass) => gymClass.entrenadorId === trainerId);
  const trainerRoutineIds = new Set(trainerRoutines.map((routine) => routine.id));
  const trainerClassIds = new Set(trainerClasses.map((gymClass) => gymClass.id));

  return {
    assignments: assignments.filter((assignment) =>
      trainerRoutineIds.has(assignment.routineId),
    ),
    classes: trainerClasses,
    clients: clients.filter((client) => client.role === 'CLIENT'),
    routines: trainerRoutines,
    sessions: sessions.filter((session) => trainerClassIds.has(session.gymClassId)),
  };
}

async function mutateWithSession<T>(
  path: string,
  method: 'POST' | 'PUT' | 'DELETE',
  body?: unknown,
): Promise<T> {
  const token = readSessionToken();

  if (!token) {
    throw new Error('No hay una sesion activa en el navegador.');
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    body: body ? JSON.stringify(body) : undefined,
    headers: {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    method,
  });

  if (!response.ok) {
    if (response.status === 401) {
      throw new Error('La sesion ya no es valida. Inicia sesion de nuevo.');
    }

    if (response.status === 403) {
      throw new Error('Tu usuario no tiene permiso para realizar esta accion.');
    }

    throw new Error('No se ha podido guardar el cambio solicitado.');
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return (await response.json()) as T;
}

export async function createRoutineAssignment(
  assignment: TrainerAssignmentDraft,
): Promise<TrainerAssignment> {
  return mutateWithSession<TrainerAssignment>('/api/routine-assignments', 'POST', {
    clientId: Number(assignment.clientId),
    routineId: Number(assignment.routineId),
  });
}

export async function deleteRoutineAssignment(assignmentId: number): Promise<void> {
  return mutateWithSession<void>(`/api/routine-assignments/${assignmentId}`, 'DELETE');
}

export async function createTrainerRoutine(
  trainerId: number,
  routine: TrainerRoutineDraft,
): Promise<TrainerRoutine> {
  return mutateWithSession<TrainerRoutine>('/api/routines', 'POST', {
    descripcion: routine.descripcion.trim() || null,
    entrenadorId: trainerId,
    nombre: routine.nombre.trim(),
  });
}

export async function updateTrainerRoutine(
  routineId: number,
  trainerId: number,
  routine: TrainerRoutineDraft,
): Promise<TrainerRoutine> {
  return mutateWithSession<TrainerRoutine>(`/api/routines/${routineId}`, 'PUT', {
      descripcion: routine.descripcion.trim() || null,
      entrenadorId: trainerId,
      nombre: routine.nombre.trim(),
  });
}

export async function deleteTrainerRoutine(routineId: number): Promise<void> {
  return mutateWithSession<void>(`/api/routines/${routineId}`, 'DELETE');
}

export async function createClassSession(
  session: TrainerClassSessionDraft,
): Promise<TrainerClassSession> {
  return mutateWithSession<TrainerClassSession>('/api/class-sessions', 'POST', {
    fecha: session.fecha,
    gymClassId: Number(session.gymClassId),
    horaFin: session.horaFin,
    horaInicio: session.horaInicio,
  });
}

export async function updateClassSession(
  sessionId: number,
  session: TrainerClassSessionDraft,
): Promise<TrainerClassSession> {
  return mutateWithSession<TrainerClassSession>(`/api/class-sessions/${sessionId}`, 'PUT', {
    fecha: session.fecha,
    gymClassId: Number(session.gymClassId),
    horaFin: session.horaFin,
    horaInicio: session.horaInicio,
  });
}

export async function deleteClassSession(sessionId: number): Promise<void> {
  return mutateWithSession<void>(`/api/class-sessions/${sessionId}`, 'DELETE');
}
