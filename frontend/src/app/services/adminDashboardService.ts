import { readSessionToken } from './authService';
import type {
  AdminDashboardData,
  AttendanceSummary,
  ClassSessionSummary,
  GymClassDraft,
  GymClassSummary,
  MachineSummary,
  MachineDraft,
  MembershipFeeDraft,
  MembershipFeeSummary,
  QrCodeDraft,
  QrCodeSummary,
  UserDraft,
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

    throw new Error('No se ha podido guardar el cambio solicitado.');
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return (await response.json()) as T;
}

export async function getAdminDashboardData(): Promise<AdminDashboardData> {
  const [users, machines, classes, classSessions, qrCodes, attendances, membershipFees] = await Promise.all([
    fetchWithSession<UserSummary[]>('/api/users'),
    fetchWithSession<MachineSummary[]>('/api/machines'),
    fetchWithSession<GymClassSummary[]>('/api/classes'),
    fetchWithSession<ClassSessionSummary[]>('/api/class-sessions'),
    fetchWithSession<QrCodeSummary[]>('/api/qr-codes'),
    fetchWithSession<AttendanceSummary[]>('/api/attendances'),
    fetchWithSession<MembershipFeeSummary[]>('/api/membership-fees'),
  ]);

  return {
    attendances,
    classSessions,
    classes,
    machines,
    membershipFees,
    qrCodes,
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

function toUserPayload(user: UserDraft, includePassword: boolean) {
  return {
    activo: user.activo,
    email: user.email.trim(),
    nombre: user.nombre.trim(),
    password: includePassword ? user.password : undefined,
    roleName: user.roleName,
  };
}

export async function createUser(user: UserDraft): Promise<UserSummary> {
  return mutateWithSession<UserSummary>(
    '/api/users',
    'POST',
    toUserPayload(user, true),
  );
}

export async function updateUser(
  userId: number,
  user: UserDraft,
): Promise<UserSummary> {
  return mutateWithSession<UserSummary>(
    `/api/users/${userId}`,
    'PUT',
    {
      ...toUserPayload(user, false),
      password: user.password.trim() ? user.password : null,
    },
  );
}

export async function deleteUser(userId: number): Promise<void> {
  return mutateWithSession<void>(`/api/users/${userId}`, 'DELETE');
}

function toGymClassPayload(gymClass: GymClassDraft) {
  return {
    activa: gymClass.activa,
    descripcion: gymClass.descripcion.trim() || null,
    duracion: Number(gymClass.duracion),
    entrenadorId: Number(gymClass.entrenadorId),
    nombre: gymClass.nombre.trim(),
  };
}

export async function createGymClass(
  gymClass: GymClassDraft,
): Promise<GymClassSummary> {
  return mutateWithSession<GymClassSummary>(
    '/api/classes',
    'POST',
    toGymClassPayload(gymClass),
  );
}

export async function updateGymClass(
  gymClassId: number,
  gymClass: GymClassDraft,
): Promise<GymClassSummary> {
  return mutateWithSession<GymClassSummary>(
    `/api/classes/${gymClassId}`,
    'PUT',
    toGymClassPayload(gymClass),
  );
}

export async function deleteGymClass(gymClassId: number): Promise<void> {
  return mutateWithSession<void>(`/api/classes/${gymClassId}`, 'DELETE');
}

function toMembershipFeePayload(fee: MembershipFeeDraft) {
  return {
    descripcion: fee.descripcion.trim() || null,
    nombre: fee.nombre.trim(),
    precio: Number(fee.precio),
  };
}

export async function createMembershipFee(
  fee: MembershipFeeDraft,
): Promise<MembershipFeeSummary> {
  return mutateWithSession<MembershipFeeSummary>(
    '/api/membership-fees',
    'POST',
    toMembershipFeePayload(fee),
  );
}

export async function updateMembershipFee(
  feeId: number,
  fee: MembershipFeeDraft,
): Promise<MembershipFeeSummary> {
  return mutateWithSession<MembershipFeeSummary>(
    `/api/membership-fees/${feeId}`,
    'PUT',
    toMembershipFeePayload(fee),
  );
}

export async function deleteMembershipFee(feeId: number): Promise<void> {
  return mutateWithSession<void>(`/api/membership-fees/${feeId}`, 'DELETE');
}

function toQrPayload(qrCode: QrCodeDraft) {
  return {
    esEntradaGimnasio: qrCode.esEntradaGimnasio,
    maquinaId:
      qrCode.tipo === 'MACHINE' && qrCode.maquinaId ? Number(qrCode.maquinaId) : null,
    sesionClaseId:
      qrCode.tipo === 'CLASS_SESSION' && qrCode.sesionClaseId
        ? Number(qrCode.sesionClaseId)
        : null,
    tipo: qrCode.tipo,
  };
}

export async function createQrCode(qrCode: QrCodeDraft): Promise<QrCodeSummary> {
  return mutateWithSession<QrCodeSummary>('/api/qr-codes', 'POST', toQrPayload(qrCode));
}

export async function updateQrCode(
  qrCodeId: number,
  qrCode: QrCodeDraft,
): Promise<QrCodeSummary> {
  return mutateWithSession<QrCodeSummary>(
    `/api/qr-codes/${qrCodeId}`,
    'PUT',
    toQrPayload(qrCode),
  );
}

export async function deleteQrCode(qrCodeId: number): Promise<void> {
  return mutateWithSession<void>(`/api/qr-codes/${qrCodeId}`, 'DELETE');
}
