# Frontend web: clases admin y pantalla en Figma

## Objetivo

Convertir la seccion `Clases` del panel web de administracion en una pantalla interna real, manteniendo una implementacion facil de entender y alineada con la documentacion visual del proyecto.

## Cambios funcionales en React

La seccion `Clases` ya no es solo un listado. Ahora permite:

- buscar clases por nombre, descripcion, entrenador o ID
- seleccionar una clase existente
- editar una clase ya registrada
- crear una clase nueva
- eliminar una clase seleccionada
- asignar el entrenador desde un selector simple

## Archivos modificados

- `frontend/src/app/components/DesktopAdminDashboard.tsx`
- `frontend/src/app/services/adminDashboardService.ts`
- `frontend/src/app/types/admin.ts`

## Modelo usado en web

Se ha anadido un borrador de clase para trabajar con el formulario desde React:

- `nombre`
- `descripcion`
- `duracion`
- `entrenadorId`
- `activa`

Esto mantiene una logica sencilla:

- el formulario trabaja siempre con texto facil de manejar
- el servicio transforma despues esos datos al formato que espera el backend

## Figma

Como esta iteracion crea una nueva pantalla interna con mas peso funcional, tambien se documenta en Figma.

Archivo:

- `Paginas Hazel Gym`

Pagina:

- `WEB ESCRITORIO`

Nueva pantalla / frame:

- `ADMIN CLASSES DESKTOP`

Esta pantalla sirve para reflejar:

- la navegacion interna ya implementada
- la estructura visual de la gestion de clases
- la relacion entre listado y formulario lateral

## Decision de diseno

Se ha seguido el mismo criterio del resto del frontend web:

- estructura simple
- poco estado compartido
- CRUD claro
- nombres faciles de defender en el TFG

## Siguiente paso recomendado

Lo siguiente mas natural seria seguir con otra seccion interna real del admin:

1. `Cuotas`
2. `QR` como supervision y control
3. una vista de `Clientes` o `Rutinas` mas completa en entrenador
