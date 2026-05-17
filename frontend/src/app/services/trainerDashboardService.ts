import { readSessionToken } from './authService';
import type {
  TrainerAssignment,
  TrainerClassSession,
  TrainerClient,
  TrainerDashboardData,
  TrainerGymClass,
  TrainerRoutine,
} from '../types/trainer';

const API_BASE_URL = 'http://localhost:8080';

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
    clients,
    routines: trainerRoutines,
    sessions: sessions.filter((session) => trainerClassIds.has(session.gymClassId)),
  };
}
