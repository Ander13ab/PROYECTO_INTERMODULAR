# Frontend web: QR admin y pantalla en Figma

## Objetivo

Convertir la seccion `QR` del panel web de administracion en una pantalla interna real, coherente con la logica de negocio definida para separar el uso de QR entre web y movil.

## Logica de negocio aplicada

Se mantiene la siguiente idea:

- la app movil sigue siendo el flujo principal de escaneo
- la web se usa para supervision, organizacion y gestion de codigos QR

Esto permite que el proyecto tenga sentido segun la plataforma:

- movil para escanear rapido con camara
- web para crear, editar, revisar y controlar los codigos disponibles

## Cambios funcionales en React

La seccion `QR` ya permite:

- buscar codigos QR por tipo, recurso vinculado o ID
- seleccionar un QR existente
- crear un QR nuevo
- editar un QR ya registrado
- eliminar un QR seleccionado
- vincular el QR a una maquina o a una sesion de clase segun el tipo
- ver un resumen rapido por tipos de QR

## Archivos modificados

- `frontend/src/app/components/DesktopAdminDashboard.tsx`
- `frontend/src/app/services/adminDashboardService.ts`
- `frontend/src/app/types/admin.ts`

## Nuevos datos usados en web

Se han incorporado tambien:

- listado de codigos QR
- listado de sesiones de clase

Esto hace posible relacionar cada QR con su recurso sin meter logica compleja en el componente.

## Figma

Como esta iteracion crea una nueva pantalla interna con gestion real, tambien se documenta en Figma.

Archivo:

- `Paginas Hazel Gym`

Pagina:

- `WEB ESCRITORIO`

Nueva pantalla / frame:

- `ADMIN QR DESKTOP`

Esta pantalla deja reflejado:

- el listado de codigos QR
- el formulario de alta y edicion
- la explicacion de rol entre web y movil

## Decision de diseno

Se ha mantenido el mismo patron del resto del admin web:

- listado a la izquierda
- formulario a la derecha
- estados faciles de seguir
- codigo sencillo para poder defenderlo en el TFG

## Siguiente paso recomendado

Lo siguiente mas natural seria salir del bloque admin y volver al panel de entrenador para dar mas profundidad a:

1. `Clientes`
2. `Rutinas`
3. `Sesiones`
