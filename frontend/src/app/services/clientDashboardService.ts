import { readSessionToken } from './authService';
import type {
  ClientAttendance,
  ClientDashboardData,
  ClientGymClass,
  ClientMachine,
  ClientRoutine,
  ClientRoutineAssignment,
} from '../types/client';

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

    throw new Error('No se han podido cargar los datos del cliente.');
  }

  return (await response.json()) as T;
}

export async function getClientDashboardData(): Promise<ClientDashboardData> {
  const [attendances, routines, assignments, machines, classes] = await Promise.all([
    fetchWithSession<ClientAttendance[]>('/api/attendances'),
    fetchWithSession<ClientRoutine[]>('/api/routines'),
    fetchWithSession<ClientRoutineAssignment[]>('/api/routine-assignments'),
    fetchWithSession<ClientMachine[]>('/api/machines'),
    fetchWithSession<ClientGymClass[]>('/api/classes'),
  ]);

  return {
    assignments,
    attendances,
    classes,
    machines,
    routines,
  };
}
