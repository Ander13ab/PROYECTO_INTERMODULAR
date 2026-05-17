# Frontend web: rutinas del entrenador y pantalla en Figma

## Objetivo

Dar profundidad real a la seccion `Rutinas` del panel web de entrenador para que no sea solo de consulta y permita gestionar el catalogo propio de rutinas.

## Cambios funcionales en React

La seccion `Rutinas` ya permite:

- buscar rutinas por nombre, descripcion o ID
- seleccionar una rutina existente
- crear una rutina nueva desde la web
- editar una rutina ya registrada
- eliminar una rutina seleccionada

## Archivos modificados

- `frontend/src/app/components/DesktopTrainerDashboard.tsx`
- `frontend/src/app/services/trainerDashboardService.ts`
- `frontend/src/app/types/trainer.ts`

## Decision de implementacion

Se ha mantenido el mismo criterio visual y funcional usado en otras secciones del proyecto:

- listado a la izquierda
- formulario a la derecha
- pocos estados
- sin librerias adicionales
- logica facil de defender en el TFG

## Figma

Como esta iteracion convierte otra seccion interna en una pantalla mas completa, tambien se documenta en Figma.

Archivo:

- `Paginas Hazel Gym`

Pagina:

- `WEB ESCRITORIO`

Nueva pantalla / frame:

- `TRAINER ROUTINES DESKTOP`

Esta pantalla deja documentado:

- el listado de rutinas del entrenador
- la seleccion de rutina
- el formulario de alta y edicion
- las acciones CRUD principales

## Siguiente paso recomendado

Lo siguiente mas natural seria seguir con:

1. `Sesiones` del entrenador
2. o volver al panel cliente para anadir mas acciones reales
