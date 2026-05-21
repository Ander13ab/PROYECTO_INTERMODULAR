export const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL?.replace(/\/$/, '') ?? 'http://localhost:8080';

export function getBackendConnectionMessage() {
  return `No se ha podido conectar con el backend. Comprueba que la API esta disponible en ${API_BASE_URL}.`;
}
