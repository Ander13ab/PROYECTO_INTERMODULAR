# Frontend web: panel admin conectado

## Objetivo de este bloque

Convertir el panel de administrador web en una vista conectada al backend en vez de una maqueta estatica.

## Datos conectados

El panel administrador ya consulta directamente:

- `/api/users`
- `/api/machines`
- `/api/classes`
- `/api/attendances`

## Resultado visible

Ahora el panel admin muestra:

- numero real de socios activos
- numero real de entrenadores
- numero total de maquinas
- numero de clases activas
- resumen de usuarios, maquinas, clases y asistencias
- actividad reciente generada a partir de las asistencias registradas

## Estructura aplicada

### `src/app/services/adminDashboardService.ts`

Servicio encargado de pedir los datos del panel usando el token de sesion del navegador.

### `src/app/types/admin.ts`

Tipos de datos usados por el dashboard:

- usuarios
- maquinas
- clases
- asistencias

### `src/app/components/DesktopAdminDashboard.tsx`

El propio componente del panel:

- carga los datos al entrar
- muestra estado de carga
- muestra error si el backend falla
- pinta metricas y actividad reciente

## Decision tecnica

Se ha mantenido un enfoque simple:

- un servicio pequeno para llamadas
- tipos separados para claridad
- la logica de carga dentro del propio dashboard

No se ha introducido estado global ni routing complejo porque todavia no hace falta para este punto del proyecto.

## Proximo paso recomendado

El siguiente paso mas logico es continuar por una de estas dos rutas:

1. conectar acciones reales del panel admin como usuarios y maquinas
2. conectar el panel entrenador con datos reales del backend
