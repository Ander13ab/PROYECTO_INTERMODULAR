# Frontend web: perfil del cliente y pantalla en Figma

## Objetivo

Reforzar la seccion `Perfil` del panel web de cliente para que no sea solo un bloque informativo minimo, sino una vista de contexto util dentro de la experiencia de escritorio.

## Logica de negocio aplicada

Se mantiene la misma coherencia por plataforma:

- movil para acciones rapidas del dia a dia
- web para seguimiento, consulta y lectura comoda

El cliente no gestiona datos sensibles ni modifica configuracion avanzada desde esta pantalla. La utilidad principal del perfil web es entender mejor el estado actual de su cuenta.

## Cambios funcionales en React

La seccion `Perfil` ya permite:

- ver un resumen de la cuenta activa
- revisar el alcance real del rol cliente dentro de la web
- consultar metricas rapidas de visitas, racha y asignaciones
- entender mejor la separacion entre uso web y uso movil

## Archivos modificados

- `frontend/src/app/components/DesktopClientDashboard.tsx`

## Decision de implementacion

No se ha convertido el perfil en una zona artificial de configuraciones vacias. En lugar de eso, se ha planteado como una pantalla de apoyo a la consulta:

- mas claridad sobre el estado actual
- mejor lectura del rol
- mejor coherencia con la logica general del proyecto

## Figma

Como esta iteracion convierte `Perfil` en una pantalla interna con mas entidad, tambien se documenta en Figma.

Archivo:

- `Paginas Hazel Gym`

Pagina:

- `WEB ESCRITORIO`

Nueva pantalla / frame:

- `CLIENT PROFILE DESKTOP`

Esta pantalla sirve para reflejar:

- la cuenta activa
- el bloque de metricas
- la explicacion de uso web frente a movil

## Siguiente paso recomendado

Lo siguiente mas natural seria:

1. una ronda final de pulido global entre admin, entrenador y cliente
2. o revisar si queda alguna seccion interna web todavia demasiado simple
