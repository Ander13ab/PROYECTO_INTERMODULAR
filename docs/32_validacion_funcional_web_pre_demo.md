# Validacion funcional web pre-demo - 18 mayo 2026

## Objetivo

Validar que la aplicacion web de Hazel Gym esta preparada para una demo funcional conectada al backend local.

## Entorno validado

- Backend Spring Boot activo en `http://localhost:8080`.
- Frontend React/Vite activo en `http://localhost:5173`.
- Base de datos local con datos de prueba.
- Cuentas usadas:
  - Administrador: `admin@hazelgym.com`
  - Entrenadora: `laura@hazelgym.com`
  - Cliente: `carlos@hazelgym.com`
  - Contrasena de prueba: `admin123`

## Resultado general

La validacion funcional principal ha sido correcta. Los tres roles pueden iniciar sesion, cargar sus datos y ejecutar las operaciones principales previstas para la demo.

## Validaciones superadas

- Login correcto con rol `ADMIN`.
- Login correcto con rol `TRAINER`.
- Login correcto con rol `CLIENT`.
- Lectura de usuario autenticado con `/api/auth/me`.
- Panel de administrador con carga de usuarios, maquinas, clases, sesiones, QR, asistencias y cuotas.
- Panel de entrenador con carga de clientes, rutinas, asignaciones, clases y sesiones.
- Panel de cliente con carga de asistencias, rutinas, asignaciones, maquinas y clases.
- Administrador puede crear, editar y borrar maquinas.
- Administrador puede crear, editar y borrar usuarios.
- Entrenador puede crear, editar y borrar rutinas.
- Entrenador puede crear y borrar asignaciones de rutina a cliente.
- Entrenador puede crear, editar y borrar sesiones de clase.
- Cliente puede consultar `/api/users` y el backend devuelve solo su propio usuario, lo cual encaja con la logica de seguridad actual.
- El frontend web compila correctamente con `pnpm build`.

## Incidencias encontradas

### Resolucion del build de produccion

El build de Vite quedaba bloqueado al intentar cargar automaticamente el archivo de configuracion en Windows.

Se ha dejado resuelto con un script de compilacion programatica en `frontend/scripts/build.mjs`, de forma que `pnpm build` ya genera `dist` correctamente y no queda dependiente de ese punto fragil.

### Formato de hora en sesiones de clase

Durante una prueba manual se envio una hora con formato `HH:mm:ss` y el backend devolvio error 500. El formulario real de la web usa formato `HH:mm`, y con ese formato la creacion y edicion de sesiones funciona correctamente.

Recomendacion posterior: endurecer el backend para que si llega un formato no esperado devuelva un 400 claro en vez de un 500.

## Recomendaciones antes de demo

- Ejecutar `pnpm build` antes de la entrega final para regenerar `dist` con la version exacta que se vaya a presentar.
- Mantener backend y frontend abiertos antes de la demo.
- Probar manualmente en navegador los tres usuarios.
- Evitar crear datos permanentes durante la demo salvo que se explique que son pruebas.
- Si se muestran sesiones de clase, usar horas en formato de formulario web: `10:00`, `11:00`, etc.

## Comando de apoyo

Se ha preparado el script `frontend/scripts/web-functional-smoke.ps1` para repetir esta validacion funcional desde PowerShell.
