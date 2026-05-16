import type {
  AuthenticatedUser,
  AuthResponse,
  LoginRequest,
} from '../types/auth';

const API_BASE_URL = 'http://localhost:8080';
export const SESSION_TOKEN_KEY = 'hazelgym.web.token';

async function readJsonOrThrow<T>(response: Response): Promise<T> {
  if (response.ok) {
    return (await response.json()) as T;
  }

  let message = 'No se pudo completar la solicitud.';

  try {
    const errorBody = (await response.json()) as { message?: string };
    if (errorBody.message) {
      message = errorBody.message;
    }
  } catch {
    if (response.status === 401) {
      message = 'Credenciales incorrectas.';
    } else if (response.status === 403) {
      message = 'No tienes permisos para acceder.';
    } else if (response.status >= 500) {
      message = 'El servidor ha respondido con un error interno.';
    }
  }

  throw new Error(message);
}

export async function login(request: LoginRequest): Promise<AuthResponse> {
  const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
    body: JSON.stringify(request),
    headers: {
      'Content-Type': 'application/json',
    },
    method: 'POST',
  });

  return readJsonOrThrow<AuthResponse>(response);
}

export async function getAuthenticatedUser(
  token: string,
): Promise<AuthenticatedUser> {
  const response = await fetch(`${API_BASE_URL}/api/auth/me`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return readJsonOrThrow<AuthenticatedUser>(response);
}

export function saveSessionToken(token: string) {
  window.sessionStorage.setItem(SESSION_TOKEN_KEY, token);
}

export function readSessionToken() {
  return window.sessionStorage.getItem(SESSION_TOKEN_KEY);
}

export function clearSessionToken() {
  window.sessionStorage.removeItem(SESSION_TOKEN_KEY);
}
