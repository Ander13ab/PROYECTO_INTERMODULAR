# Frontend web: login a pantalla completa y CORS local

## Objetivo

Corregir dos problemas detectados durante las pruebas del frontend web:

1. El login se mostraba como un bloque aislado sobre un fondo claro, en lugar de ocupar visualmente toda la pantalla como una aplicacion real.
2. El acceso fallaba con el mensaje `Failed to fetch` cuando el frontend se abría en un puerto distinto de `5173`, por ejemplo `5174`.

## Cambios realizados

### 1. Fondo y composicion a pantalla completa

Se ajusto la capa global de estilos para que `html`, `body` y `#root` ocupen toda la altura disponible y usen un fondo oscuro estable.

Archivos implicados:

- `frontend/src/styles/index.css`
- `frontend/src/app/App.tsx`
- `frontend/src/app/components/DesktopLoginScreen.tsx`

Resultado:

- la pantalla de login ocupa todo el viewport
- desaparece el fondo blanco inferior
- el acceso se percibe como una aplicacion completa y no como una tarjeta flotante

### 2. Compatibilidad local con distintos puertos del frontend

El backend tenia configurado CORS solo para algunos orígenes concretos (`3000`, `5173`, `8081`). Si Vite arrancaba en `5174`, el navegador bloqueaba la peticion antes de llegar al login.

Se corrigio en:

- `backend/src/main/java/com/hazelgym/security/SecurityConfig.java`
- `backend/src/main/resources/application.properties`

Cambios clave:

- se pasa de `setAllowedOrigins(...)` a `setAllowedOriginPatterns(...)`
- se amplian los origenes por defecto para desarrollo local
- se permiten patrones de `localhost` y `127.0.0.1` con puertos variables en entorno local

## Mejora del mensaje de error

Tambien se actualizo:

- `frontend/src/app/services/authService.ts`

Ahora, si el frontend no puede conectar con el backend, en lugar de mostrar `Failed to fetch`, se enseña un mensaje entendible:

- comprobar que Spring Boot esta arrancado
- comprobar que responde en `http://localhost:8080`

## Como probarlo

1. Reiniciar el backend Spring Boot.
2. Reiniciar el frontend Vite.
3. Abrir la web en `localhost:5173`, `localhost:5174` o cualquier puerto local equivalente.
4. Intentar iniciar sesion con cuentas reales de `ADMIN`, `TRAINER` o `CLIENT`.

## Resultado esperado

- el login se ve a pantalla completa
- no aparece fondo blanco por debajo
- el acceso funciona tambien si Vite cambia de puerto local
- si el backend no esta disponible, el mensaje de error es claro
