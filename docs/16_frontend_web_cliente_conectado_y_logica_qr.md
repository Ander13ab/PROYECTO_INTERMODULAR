# Frontend web: cliente conectado y logica de QR por plataforma

## Objetivo

Completar el panel web del cliente con datos reales del backend y ajustar la logica de negocio del QR para que cada plataforma use esta funcionalidad donde tiene mas sentido.

## Decision funcional

Se define esta distribucion:

- `App movil`
  - escaneo principal de codigos QR
  - registro rapido de asistencia
  - uso natural con camara del telefono

- `App web`
  - consulta del historial de asistencias
  - consulta de rutinas asignadas
  - consulta de maquinas y clases
  - soporte informativo sobre el uso del QR desde movil

Esta decision evita forzar el escaneo de QR en un PC cuando el caso de uso mas realista es el telefono movil.

## Cambios en backend

Se ajusto la seguridad y el filtrado de datos para que el cliente solo pueda ver sus propias asignaciones y sus propias rutinas:

- `backend/src/main/java/com/hazelgym/security/SecurityConfig.java`
- `backend/src/main/java/com/hazelgym/service/impl/RoutineAssignmentServiceImpl.java`
- `backend/src/main/java/com/hazelgym/service/impl/RoutineServiceImpl.java`
- `backend/src/main/java/com/hazelgym/repository/RoutineAssignmentRepository.java`

Resultado:

- `ADMIN` y `TRAINER` siguen viendo todas las asignaciones necesarias para gestion
- `CLIENT` solo puede leer sus asignaciones
- `CLIENT` solo puede leer sus rutinas asignadas

## Cambios en frontend web

Se creo la capa de datos del panel cliente:

- `frontend/src/app/types/client.ts`
- `frontend/src/app/services/clientDashboardService.ts`

Y se conecto el panel visual:

- `frontend/src/app/components/DesktopClientDashboard.tsx`
- `frontend/src/app/App.tsx`

## Datos mostrados en el panel cliente

El cliente web ahora muestra:

- visitas totales reales
- racha actual calculada a partir del historial
- rutinas activas asignadas
- historial reciente de asistencias
- clases activas visibles
- rutinas asignadas visibles
- catalogo de maquinas como referencia

## Ajuste de QR en la interfaz web

En lugar de presentar el QR como accion principal de escaneo dentro de la web, el dashboard del cliente deja claro que:

- el escaneo principal se hace en la app movil
- la web sirve para consultar y seguir la actividad registrada

## Valor para la defensa del TFG

Esta decision es facil de justificar:

1. Se aprovecha cada plataforma segun su contexto real de uso.
2. Se evita duplicar una experiencia peor en escritorio.
3. Se mantiene la coherencia entre negocio, tecnologia y experiencia de usuario.
