import { readSessionToken } from './authService';
import type {
  AdminDashboardData,
  AttendanceSummary,
  GymClassSummary,
  MachineSummary,
  MachineDraft,
  MembershipFeeSummary,
  UserSummary,
} from '../types/admin';

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

    throw new Error('No se han podido cargar los datos del administrador.');
  }

  return (await response.json()) as T;
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

    throw new Error('No se ha podido guardar el cambio en maquinas.');
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return (await response.json()) as T;
}

export async function getAdminDashboardData(): Promise<AdminDashboardData> {
  const [users, machines, classes, attendances, membershipFees] = await Promise.all([
    fetchWithSession<UserSummary[]>('/api/users'),
    fetchWithSession<MachineSummary[]>('/api/machines'),
    fetchWithSession<GymClassSummary[]>('/api/classes'),
    fetchWithSession<AttendanceSummary[]>('/api/attendances'),
    fetchWithSession<MembershipFeeSummary[]>('/api/membership-fees'),
  ]);

  return {
    attendances,
    classes,
    machines,
    membershipFees,
    users,
  };
}

function toMachinePayload(machine: MachineDraft) {
  return {
    advertenciaSeguridad: machine.advertenciaSeguridad.trim() || null,
    descripcion: machine.descripcion.trim() || null,
    estado: machine.estado,
    grupoMuscular: machine.grupoMuscular.trim() || null,
    imagenUrl: machine.imagenUrl.trim() || null,
    instrucciones: machine.instrucciones.trim() || null,
    nivel: machine.nivel.trim() || null,
    nombre: machine.nombre.trim(),
  };
}

export async function createMachine(machine: MachineDraft): Promise<MachineSummary> {
  return mutateWithSession<MachineSummary>(
    '/api/machines',
    'POST',
    toMachinePayload(machine),
  );
}

export async function updateMachine(
  machineId: number,
  machine: MachineDraft,
): Promise<MachineSummary> {
  return mutateWithSession<MachineSummary>(
    `/api/machines/${machineId}`,
    'PUT',
    toMachinePayload(machine),
  );
}

export async function deleteMachine(machineId: number): Promise<void> {
  return mutateWithSession<void>(`/api/machines/${machineId}`, 'DELETE');
}
