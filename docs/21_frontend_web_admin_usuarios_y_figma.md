# Frontend web: usuarios admin y pantalla en Figma

## Objetivo

Convertir la seccion `Usuarios` del panel web de administracion en una pantalla interna con mas entidad, tanto a nivel funcional como a nivel documental.

## Cambios funcionales en React

La seccion `Usuarios` ya no es solo un listado simple. Ahora permite:

- buscar usuarios por nombre, email, rol o ID
- seleccionar un usuario existente
- editar sus datos principales
- crear un usuario nuevo
- eliminar un usuario seleccionado

## Archivos modificados

- `frontend/src/app/components/DesktopAdminDashboard.tsx`
- `frontend/src/app/services/adminDashboardService.ts`
- `frontend/src/app/types/admin.ts`

## Modelo usado en web

Se ha añadido un borrador de usuario para trabajar con el formulario desde React:

- `nombre`
- `email`
- `password`
- `roleName`
- `activo`

Esto mantiene el codigo sencillo y entendible, sin introducir formularios complejos ni librerias extra.

## Figma

Como esta iteracion si ha convertido una seccion interna en una pantalla con mas entidad, se ha documentado tambien en Figma.

Archivo:

- `Páginas Hazel Gym`

Pagina:

- `WEB ESCRITORIO`

Nueva pantalla / frame:

- `ADMIN USERS DESKTOP`

Esta pantalla sirve como referencia visual del estado actual de la gestion web de usuarios y mantiene alineados:

- el layout de escritorio
- la navegacion interna
- la logica CRUD ya conectada al backend

## Decision de diseño

Se ha seguido el mismo criterio que en el resto de la web:

- estructura facil de defender
- componentes simples
- pocos estados
- interfaz clara antes que arquitectura compleja

## Siguiente paso recomendado

Lo siguiente mas natural seria seguir con otra seccion interna de gestion real, por ejemplo:

1. `Clases` en admin web
2. `Cuotas` en admin web
3. `Rutinas` o `Clientes` del entrenador con una vista mas completa
