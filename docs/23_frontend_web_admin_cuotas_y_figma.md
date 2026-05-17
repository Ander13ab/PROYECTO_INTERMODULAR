# Frontend web: cuotas admin y pantalla en Figma

## Objetivo

Convertir la seccion `Cuotas` del panel web de administracion en una pantalla interna real, con una implementacion sencilla y coherente con el resto del admin web.

## Cambios funcionales en React

La seccion `Cuotas` ya no es solo un listado. Ahora permite:

- buscar cuotas por nombre, descripcion, precio o ID
- seleccionar una cuota existente
- editar una cuota ya registrada
- crear una cuota nueva desde la web
- eliminar una cuota seleccionada

## Archivos modificados

- `frontend/src/app/components/DesktopAdminDashboard.tsx`
- `frontend/src/app/services/adminDashboardService.ts`
- `frontend/src/app/types/admin.ts`

## Modelo usado en web

Se ha anadido un borrador de cuota para trabajar con el formulario desde React:

- `nombre`
- `descripcion`
- `precio`

La idea sigue siendo la misma que en el resto del proyecto:

- estados faciles de seguir
- poco acoplamiento
- transformacion al formato del backend solo en el servicio

## Figma

Como esta iteracion crea otra pantalla interna con gestion real, tambien se documenta en Figma.

Archivo:

- `Paginas Hazel Gym`

Pagina:

- `WEB ESCRITORIO`

Nueva pantalla / frame:

- `ADMIN FEES DESKTOP`

Esta pantalla sirve para dejar trazado visualmente:

- el listado de cuotas
- el formulario de alta y edicion
- la relacion entre seleccion y acciones CRUD

## Decision de diseno

Se ha mantenido el patron ya asentado en `Usuarios`, `Maquinas` y `Clases`:

- listado a la izquierda
- formulario a la derecha
- botones claros
- arquitectura facil de defender

## Siguiente paso recomendado

Lo siguiente mas natural seria seguir con:

1. una seccion interna mas profunda para `QR`
2. una vista mas rica de `Rutinas` o `Clientes` en entrenador
3. acciones reales adicionales en el panel cliente
