# Frontend web y arranque en Figma

## Decision tomada

La app movil de Hazel Gym ya tiene un nivel funcional suficiente para dejar de ser el cuello de botella del proyecto. A partir de este punto, lo mas logico es abrir el frente de aplicacion web y usar Figma como base visual de escritorio.

## Estado real de la app movil

### Ya cubierto

- Login real por rol
- Cliente con rutinas, clases, maquinas, QR e historial de asistencias
- Entrenador con clases, rutinas, asignaciones y asistencias
- Admin con usuarios, maquinas, QR y actividad
- Navegacion interna y formularios funcionales
- Selector buscable reutilizable en formularios con muchas opciones

### Mejoras pendientes, pero no bloqueantes

- Pulido fino de espaciados y tipografia pantalla por pantalla
- Mantener sesion opcional entre arranques
- Algunas acciones de detalle todavia son mas operativas que “producto final”
- Reforzar pruebas de flujo completo por rol

## Conclusion funcional

No hace falta detener el avance del proyecto para seguir ampliando la app movil antes de empezar la web. La recomendacion es:

1. Mantener movil en modo mejora incremental
2. Empezar ya la aplicacion web
3. Alinear escritorio en Figma con las pantallas que ya existen como base en `frontend`

## Figma

En el archivo principal de Figma se ha creado una nueva pagina:

- `WEB DESKTOP`

Dentro de esa pagina se han dejado preparadas estas pantallas base:

- `WEB LOGIN`
- `WEB CLIENT HOME`
- `WEB TRAINER HOME`
- `WEB ADMIN HOME`

Estas pantallas sirven como punto de partida para la version escritorio y estan alineadas con la base visual ya presente en el proyecto React de `frontend`.

## Siguiente paso recomendado

El siguiente bloque logico es convertir el frontend actual en una aplicacion web funcional por fases:

1. Login web real conectado al backend
2. Shell comun de escritorio con layout y navegacion por rol
3. Dashboard admin funcional
4. Dashboard entrenador funcional
5. Dashboard cliente funcional

## Nota

La estrategia recomendada no es rehacer web desde cero, sino reutilizar la base ya existente en `frontend`, conectarla al backend y luego seguir afinando la fidelidad con Figma.
