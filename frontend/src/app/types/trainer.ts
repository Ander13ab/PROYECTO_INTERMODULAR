export interface TrainerClient {
  id: number;
  nombre: string;
  email: string;
  activo: boolean;
  role: string;
}

export interface TrainerRoutine {
  id: number;
  nombre: string;
  descripcion: string | null;
  entrenadorId: number;
  entrenadorNombre: string;
  fechaCreacion: string;
}

export interface TrainerRoutineDraft {
  nombre: string;
  descripcion: string;
}

export interface TrainerAssignment {
  id: number;
  routineId: number;
  routineName: string;
  clientId: number;
  clientName: string;
  fechaAsignacion: string;
}

export interface TrainerAssignmentDraft {
  clientId: string;
  routineId: string;
}

export interface TrainerGymClass {
  id: number;
  nombre: string;
  descripcion: string | null;
  duracion: number | null;
  entrenadorId: number;
  entrenadorNombre: string;
  activa: boolean;
}

export interface TrainerClassSession {
  id: number;
  gymClassId: number;
  gymClassName: string;
  fecha: string;
  horaInicio: string;
  horaFin: string;
}

export interface TrainerClassSessionDraft {
  gymClassId: string;
  fecha: string;
  horaInicio: string;
  horaFin: string;
}

export interface TrainerDashboardData {
  clients: TrainerClient[];
  routines: TrainerRoutine[];
  assignments: TrainerAssignment[];
  classes: TrainerGymClass[];
  sessions: TrainerClassSession[];
}
