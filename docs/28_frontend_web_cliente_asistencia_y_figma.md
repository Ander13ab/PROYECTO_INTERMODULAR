# Frontend web: asistencia del cliente y pantalla en Figma

## Objetivo

Dar mas profundidad a la seccion `Asistencia` del panel web de cliente para que la web aporte valor real en consulta y seguimiento, manteniendo el escaneo principal del QR en la app movil.

## Logica de negocio aplicada

Se mantiene esta separacion:

- movil para registrar asistencia mediante QR
- web para consultar historial, filtrar registros y revisar progreso

## Cambios funcionales en React

La seccion `Asistencia` ya permite:

- buscar registros por ID o tipo de QR
- filtrar por tipo de QR
- seleccionar un registro concreto
- ver resumen rapido de visitas, racha y rutinas activas
- revisar el detalle de una asistencia seleccionada

## Archivos modificados

- `frontend/src/app/components/DesktopClientDashboard.tsx`

## Decision de implementacion

No se ha forzado una accion artificial en web que no encaja con el proyecto. En vez de eso, se ha reforzado la utilidad real de escritorio:

- seguimiento
- lectura de historial
- filtro y detalle

Esto hace que la logica entre plataformas siga siendo coherente.

## Figma

Como esta iteracion convierte `Asistencia` en una pantalla interna con mas entidad, tambien se documenta en Figma.

Archivo:

- `Paginas Hazel Gym`

Pagina:

- `WEB ESCRITORIO`

Nueva pantalla / frame:

- `CLIENT ATTENDANCE DESKTOP`

Esta pantalla sirve para reflejar:

- el listado filtrable de asistencias
- el detalle del registro seleccionado
- la separacion entre uso movil y consulta web

## Siguiente paso recomendado

Lo siguiente mas natural seria:

1. reforzar `Rutinas` del cliente en web
2. o hacer una ronda final de pulido global entre admin, entrenador y cliente
