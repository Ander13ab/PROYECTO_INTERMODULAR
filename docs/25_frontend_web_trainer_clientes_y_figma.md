# Frontend web: clientes del entrenador y pantalla en Figma

## Objetivo

Dar mas profundidad a la seccion `Clientes` del panel web de entrenador para que deje de ser solo un listado y permita trabajar con asignaciones reales de rutinas.

## Cambios funcionales en React

La seccion `Clientes` ya permite:

- buscar clientes por nombre, email o ID
- seleccionar un cliente concreto
- ver las rutinas que tiene asignadas con ese entrenador
- asignar una nueva rutina desde la web
- eliminar una asignacion existente

## Archivos modificados

- `frontend/src/app/components/DesktopTrainerDashboard.tsx`
- `frontend/src/app/services/trainerDashboardService.ts`
- `frontend/src/app/types/trainer.ts`

## Decision funcional

Se ha mantenido una implementacion sencilla:

- listado de clientes a la izquierda
- gestion de rutinas asignadas a la derecha
- sin introducir librerias nuevas
- reusando la API ya existente de asignaciones de rutina

## Ajuste de datos

El panel del entrenador ahora filtra mejor los usuarios para quedarse solo con clientes, y la gestion de asignaciones se apoya en:

- `GET /api/routine-assignments`
- `POST /api/routine-assignments`
- `DELETE /api/routine-assignments/{id}`

## Figma

Como esta iteracion convierte una seccion interna del entrenador en una pantalla mas completa, tambien se documenta en Figma.

Archivo:

- `Paginas Hazel Gym`

Pagina:

- `WEB ESCRITORIO`

Nueva pantalla / frame:

- `TRAINER CLIENTS DESKTOP`

Esta pantalla sirve para reflejar:

- la relacion entre clientes y rutinas
- la seleccion de cliente
- la asignacion y eliminacion de rutinas

## Siguiente paso recomendado

Lo siguiente mas natural seria seguir con otra seccion fuerte del entrenador:

1. `Rutinas`
2. `Sesiones`
3. o volver al cliente web para pulir acciones reales adicionales
