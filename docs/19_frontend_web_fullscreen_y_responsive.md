# Frontend web fullscreen y responsive

## Objetivo

En esta iteracion se ha ajustado la interfaz web para que los paneles de `admin`, `entrenador` y `cliente` ocupen toda la pantalla y no parezcan un bloque suelto dentro de la pagina.

La idea principal ha sido que el layout tenga sentido:

- en resoluciones grandes
- al cambiar el zoom del navegador
- al reducir el ancho disponible
- al pasar de un rol a otro sin romper la estructura

## Cambios realizados

### Base global

- el `body` y `#root` se fuerzan a ocupar todo el ancho y alto disponibles
- se evita el desbordamiento horizontal
- el fondo oscuro base se mantiene como superficie real de la aplicacion

Archivo implicado:

- `frontend/src/styles/index.css`

### Login web

- la pantalla de acceso ya no depende de una composicion fija de dos columnas
- en tamaños mas estrechos pasa a una sola columna
- se han suavizado paddings y tamaños para que el zoom no deforme el conjunto

Archivo implicado:

- `frontend/src/app/components/DesktopLoginScreen.tsx`

### Paneles por rol

Se ha unificado el mismo criterio en los tres roles:

- wrapper principal a pantalla completa
- sidebar integrada en la pantalla, no dentro de una tarjeta externa
- contenido principal con `min-h-screen`
- cabeceras con salto natural cuando falta espacio
- rejillas adaptativas en tarjetas, metricas y secciones

Archivos implicados:

- `frontend/src/app/components/DesktopAdminDashboard.tsx`
- `frontend/src/app/components/DesktopTrainerDashboard.tsx`
- `frontend/src/app/components/DesktopClientDashboard.tsx`

## Criterio visual adoptado

En vez de tratar cada panel como una tarjeta flotante con sombra dentro de otra pagina, ahora cada panel se comporta como una aplicacion completa:

- el color de fondo pertenece al rol
- la barra lateral forma parte del layout principal
- el contenido se redistribuye en filas y columnas segun el ancho real
- el zoom del navegador no deja grandes vacios blancos alrededor

## Beneficio para el TFG

Este ajuste es importante de cara a la defensa porque hace mas coherente la propuesta multiplataforma:

- la web se percibe como una aplicacion de escritorio real
- los tres roles comparten una misma logica de interfaz
- el comportamiento responsive esta pensado, no improvisado
- la decision de separar `movil` y `web` se entiende mejor al ver cada plataforma adaptada a su contexto

## Siguiente paso recomendado

Con esta base ya tiene sentido seguir con:

1. pulido fino visual respecto a Figma
2. gestion real de mas bloques dentro del panel web
3. mejoras de navegacion interna y formularios responsive
