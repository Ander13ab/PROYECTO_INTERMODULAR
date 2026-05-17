# Frontend web: gestion real de maquinas y titulo de la app

## Objetivo

Dar el primer paso de gestion real dentro de la aplicacion web y corregir el nombre mostrado en la pestaña del navegador.

## Cambios visuales

Se ha actualizado:

- `frontend/index.html`

Resultado:

- la pestaña del navegador ya muestra `Hazel Gym`
- desaparece el texto heredado `Convert HTML to Figma format`

## Gestion real implementada

Se ha elegido empezar por `Maquinas` dentro del panel de administracion porque:

1. es una funcionalidad facil de entender
2. tiene valor real de negocio
3. conecta bien con el backend ya disponible
4. permite demostrar alta, edicion y borrado desde la web

## Cambios tecnicos

### Frontend

- `frontend/src/app/components/DesktopAdminDashboard.tsx`
- `frontend/src/app/services/adminDashboardService.ts`
- `frontend/src/app/types/admin.ts`

Se ha añadido:

- listado seleccionable de maquinas
- formulario de alta y edicion
- accion de borrado
- refresco del panel tras guardar o eliminar
- soporte para mas campos del modelo de maquina

### Backend

No ha sido necesario crear endpoints nuevos porque el backend ya disponia de:

- `GET /api/machines`
- `POST /api/machines`
- `PUT /api/machines/{id}`
- `DELETE /api/machines/{id}`

## Resultado funcional

Desde la web, el administrador ya puede:

- ver el catalogo de maquinas
- seleccionar una maquina para editarla
- crear una maquina nueva
- actualizar una existente
- eliminar una seleccionada

## Valor para el TFG

Este bloque marca la transicion de panel informativo a aplicacion web con gestion real.

Es facil de defender porque:

- la funcionalidad es visible y demostrable
- el flujo es sencillo de explicar
- reutiliza la API REST del backend ya construida
