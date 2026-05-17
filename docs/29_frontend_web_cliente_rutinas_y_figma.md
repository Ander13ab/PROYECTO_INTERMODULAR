# Frontend web: rutinas del cliente y pantalla en Figma

## Objetivo

Dar mas profundidad a la seccion `Rutinas` del panel web de cliente para que la version de escritorio sirva de apoyo real en el seguimiento del plan asignado.

## Logica de negocio aplicada

Se mantiene esta separacion:

- movil para acciones rapidas y uso diario en el gimnasio
- web para consulta, seguimiento y lectura mas comoda de la informacion

En cliente no se ha forzado una edicion de rutinas que no corresponde a su rol. En su lugar, la web se centra en mostrar mejor el plan que ya tiene asignado.

## Cambios funcionales en React

La seccion `Rutinas` ya permite:

- buscar rutinas por nombre, entrenador o ID
- seleccionar una rutina concreta
- ver resumen rapido de rutinas, asignaciones y clases visibles
- revisar entrenador, fecha de alta, fecha de asignacion y descripcion

## Archivos modificados

- `frontend/src/app/components/DesktopClientDashboard.tsx`

## Decision de implementacion

La mejora sigue la misma linea que `Asistencia`:

- mas utilidad real en escritorio
- mas claridad visual
- mejor navegacion interna
- sin mezclar permisos que no corresponden al cliente

## Figma

Como esta iteracion convierte `Rutinas` en una pantalla interna mas completa, tambien se documenta en Figma.

Archivo:

- `Paginas Hazel Gym`

Pagina:

- `WEB ESCRITORIO`

Nueva pantalla / frame:

- `CLIENT ROUTINES DESKTOP`

Esta pantalla sirve para reflejar:

- el buscador de rutinas
- el listado seleccionable
- el bloque de detalle de la rutina activa

## Siguiente paso recomendado

Lo siguiente mas natural seria:

1. reforzar `Perfil` del cliente en web
2. o hacer una ronda final de pulido global entre admin, entrenador y cliente
