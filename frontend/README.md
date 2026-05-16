# Hazel Gym Web

## Objetivo

Este frontend sera la version web de Hazel Gym conectada al backend Spring Boot del proyecto.

La idea de trabajo es mantener una estructura sencilla para que el codigo sea facil de entender, mantener y defender en el TFG.

## Base tecnica

- `Vite` para arrancar el proyecto web
- `React` para construir la interfaz
- `TypeScript` para que los datos y componentes queden mas claros
- `pnpm` como gestor de paquetes principal

## Estructura simple que vamos a seguir

- `src/main.tsx`
  Punto de entrada de React.
- `src/app/App.tsx`
  Contenedor principal de la aplicacion.
- `src/app/components`
  Pantallas y bloques visuales reutilizables.
- futuras carpetas `services` o `api`
  Para centralizar llamadas al backend.

## Filosofia del frontend

En esta parte del proyecto no buscamos una arquitectura rebuscada.

Vamos a priorizar:

- componentes pequenos
- nombres claros
- poco acoplamiento
- estados faciles de seguir
- conexion progresiva con el backend

## Orden recomendado de implementacion

1. Login web real
2. Lectura del usuario autenticado
3. Panel base segun rol
4. Dashboard admin conectado
5. Dashboard entrenador conectado
6. Dashboard cliente conectado

## Ejecucion local

1. Instalar dependencias
2. Arrancar el entorno web

En esta carpeta:

- `pnpm install`
- `pnpm dev`

## Documentacion relacionada

- [Introduccion a React y base web](../docs/10_introduccion_react_y_base_web.md)
- [Arranque de la parte web desde Figma](../docs/09_frontend_web_figma_y_arranque.md)
- [Login real y shell por rol](../docs/11_frontend_web_login_y_shell.md)
- [Migracion a pnpm y preparacion de Node](../docs/12_pnpm_y_node_para_frontend.md)
