export type UserRole = 'ADMIN' | 'TRAINER' | 'CLIENT';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  tokenType: string;
  userId: number;
  nombre: string;
  email: string;
  role: UserRole;
}

export interface AuthenticatedUser {
  id: number;
  nombre: string;
  email: string;
  role: UserRole;
  activo: boolean;
}
