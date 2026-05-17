export interface ClientAttendance {
  id: number;
  usuarioId: number;
  usuarioNombre: string;
  qrCodeId: number;
  qrType: string;
  fechaHora: string;
}

export interface ClientRoutine {
  id: number;
  nombre: string;
  descripcion: string | null;
  entrenadorId: number;
  entrenadorNombre: string;
  fechaCreacion: string;
}

export interface ClientRoutineAssignment {
  id: number;
  routineId: number;
  routineName: string;
  clientId: number;
  clientName: string;
  fechaAsignacion: string;
}

export interface ClientMachine {
  id: number;
  nombre: string;
  descripcion: string | null;
  grupoMuscular: string | null;
  estado: string;
}

export interface ClientGymClass {
  id: number;
  nombre: string;
  descripcion: string | null;
  duracion: number | null;
  entrenadorNombre: string | null;
  activa: boolean;
}

export interface ClientDashboardData {
  attendances: ClientAttendance[];
  routines: ClientRoutine[];
  assignments: ClientRoutineAssignment[];
  machines: ClientMachine[];
  classes: ClientGymClass[];
}
