# Frontend web: panel entrenador conectado

## Objetivo de este bloque

Convertir el panel web del entrenador en una vista conectada al backend y no solo en una maqueta visual.

## Datos conectados

El panel entrenador ya consulta:

- `/api/users`
- `/api/routines`
- `/api/routine-assignments`
- `/api/classes`
- `/api/class-sessions`

## Como se construye el panel

La informacion se carga y despues se filtra usando el `id` del entrenador autenticado.

Con eso, el panel muestra solo:

- rutinas del entrenador actual
- clases del entrenador actual
- sesiones asociadas a sus clases
- clientes relacionados con sus asignaciones

## Resultado visible

Ahora el panel del entrenador puede mostrar:

- clases del dia reales
- numero de sesiones del mes
- rutinas activas del entrenador
- clientes destacados vinculados a asignaciones reales

## Estructura aplicada

### `src/app/services/trainerDashboardService.ts`

Servicio encargado de pedir y filtrar los datos del panel.

### `src/app/types/trainer.ts`

Tipos de datos para:

- clientes
- rutinas
- asignaciones
- clases
- sesiones

### `src/app/components/DesktopTrainerDashboard.tsx`

Componente del dashboard con:

- carga inicial
- estado de carga
- estado de error
- resumen y bloques conectados a backend

## Decision tecnica

Se mantiene la misma filosofia del proyecto web:

- servicio pequeno
- tipos claros
- logica de pantalla localizada
- nada de estado global todavia

## Siguiente paso recomendado

Las rutas mas logicas ahora son:

1. conectar el panel cliente a datos reales
2. anadir acciones reales dentro de admin y entrenador
