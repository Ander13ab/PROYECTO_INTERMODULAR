# Frontend web: navegacion real por roles

## Objetivo

Dar el paso desde paneles visuales estaticos a una navegacion real dentro de cada rol web, manteniendo una estructura sencilla y facil de entender en React.

## Enfoque aplicado

En lugar de introducir un sistema de rutas complejo desde el principio, cada dashboard web gestiona una `seccion activa` con estado local.

Esto permite:

- que el menu lateral sea funcional de verdad
- cambiar el contenido principal segun la opcion pulsada
- mantener el codigo facil de seguir para aprendizaje y defensa del TFG

## Roles cubiertos

### Administrador

Secciones navegables:

- Dashboard
- Usuarios
- QR
- Maquinas
- Clases
- Cuotas

Ademas, el admin ya consulta tambien las cuotas desde backend para que la seccion no quede vacia.

### Entrenador

Secciones navegables:

- Resumen
- Sesiones
- Clientes
- Rutinas
- Perfil

### Cliente

Secciones navegables:

- Inicio
- Asistencia
- Maquinas
- Rutinas
- Perfil

## Archivos principales

- `frontend/src/app/components/DesktopAdminDashboard.tsx`
- `frontend/src/app/components/DesktopTrainerDashboard.tsx`
- `frontend/src/app/components/DesktopClientDashboard.tsx`
- `frontend/src/app/services/adminDashboardService.ts`
- `frontend/src/app/types/admin.ts`

## Valor tecnico

Esta solucion mantiene la app web simple:

1. un componente por dashboard
2. una variable de estado para la seccion activa
3. una funcion `renderSection()` que decide el contenido central

## Valor para la defensa

Es facil de justificar porque:

- demuestra navegacion real y no solo maquetas
- evita complejidad prematura
- encaja con un proyecto academico donde la claridad del codigo es importante
