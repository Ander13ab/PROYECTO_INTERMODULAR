export interface UserSummary {
  id: number;
  nombre: string;
  email: string;
  role: string;
  activo: boolean;
  fechaCreacion: string;
}

export interface MachineSummary {
  id: number;
  nombre: string;
  descripcion: string | null;
  grupoMuscular: string | null;
  instrucciones: string | null;
  nivel: string | null;
  advertenciaSeguridad: string | null;
  imagenUrl: string | null;
  estado: string;
}

export interface MachineDraft {
  nombre: string;
  descripcion: string;
  grupoMuscular: string;
  instrucciones: string;
  nivel: string;
  advertenciaSeguridad: string;
  imagenUrl: string;
  estado: string;
}

export interface GymClassSummary {
  id: number;
  nombre: string;
  descripcion: string | null;
  duracion: number | null;
  entrenadorNombre: string | null;
  activa: boolean;
}

export interface AttendanceSummary {
  id: number;
  usuarioId: number;
  usuarioNombre: string;
  qrCodeId: number;
  qrType: string;
  fechaHora: string;
}

export interface MembershipFeeSummary {
  id: number;
  nombre: string;
  descripcion: string | null;
  precio: number;
}

export interface AdminDashboardData {
  users: UserSummary[];
  machines: MachineSummary[];
  classes: GymClassSummary[];
  attendances: AttendanceSummary[];
  membershipFees: MembershipFeeSummary[];
}
