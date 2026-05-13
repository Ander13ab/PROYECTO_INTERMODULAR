# Frontend movil Android

## Resumen

Durante esta fase se ha levantado la primera base funcional de la app movil nativa de Hazel Gym con Kotlin y Jetpack Compose, conectada contra el backend Spring Boot existente.

## Lo que ya esta hecho

- Proyecto Android inicial creado en `mobile-android`
- Login real conectado al backend con validacion del rol elegido frente al rol devuelto por la API
- Persistencia de sesion local con DataStore
- Navegacion separada por rol: cliente, entrenador y administrador
- Panel cliente conectado a rutinas, clases y maquinas
- Panel entrenador conectado a clases, asignaciones y asistencias
- Panel administrador conectado a usuarios, maquinas, sesiones y codigos QR
- Pantallas de detalle para rutinas, clases y maquinas en cliente
- Pantallas de detalle para clases, asignaciones y asistencias en entrenador
- Pantallas de detalle para usuarios, maquinas, QR y asistencias en administrador
- Generacion de QR de entrada, maquina y sesion de clase desde el panel admin
- Escaneo QR desde la app cliente usando CameraX y ML Kit
- Registro de asistencia desde cliente con flujo manual y flujo automatico al escanear
- Dialogo de escaneo adaptado al emulador con campo de prueba por ID de QR
- Nueva vista de actividad para admin con resumen y listado de asistencias
- Navegacion interna real desde las tarjetas rapidas hacia vistas de detalle
- Historial de asistencias visible en cliente, entrenador y administrador
- La app arranca siempre en login en cada nueva ejecucion para facilitar pruebas de los tres roles
- Backend de asistencias ajustado para que el cliente vea solo sus propios registros autenticados
- Gestion basica de maquinas desde la app admin: alta y edicion simple con formulario
- Gestion basica de usuarios desde la app admin: alta, edicion y borrado simple con formulario
- Gestion real de clases y rutinas desde la app del entrenador: alta, edicion y borrado
- Gestion real de asignaciones de rutinas a clientes desde la app del entrenador

## Decisiones tecnicas

- En emulador se usa `http://10.0.2.2:8080/` como URL base hacia el backend local
- Se permite trafico HTTP local solo para desarrollo Android
- El backend filtra `GET /api/attendances` por rol: admin y trainer ven todo, client solo ve sus propios registros
- Para pruebas en emulador se mantiene un modo de simulacion por ID de QR porque la camara virtual no siempre ofrece un QR util

## Estado funcional actual por rol

### Cliente

- Inicio con resumen de rutinas, clases y maquinas
- Ajuste visual del login y del home siguiendo la referencia de Figma
- Pestana propia de maquinas
- Accesos rapidos conectados a QR, maquinas y perfil
- Perfil basico
- Registro de asistencia por QR manual
- Registro de asistencia por escaneo
- Historial propio de asistencias desde la app

### Entrenador

- Inicio con metricas de clases, asignaciones y asistencias
- Ajuste visual del home siguiendo la referencia de Figma
- Pestana de actividad con rutinas asignadas y asistencias recientes
- Accesos rapidos conectados a actividad y perfil
- Gestion real de sus clases desde la vista de detalle
- Gestion real de sus rutinas desde la vista de detalle
- Gestion real de asignaciones de rutina a clientes desde la vista de detalle
- Perfil basico

### Administrador

- Inicio con metricas de socios, entrenadores, maquinas y asistencias
- Ajuste visual del home siguiendo la referencia de Figma
- Gestion visual de usuarios
- Gestion real de usuarios con formulario de alta, edicion y borrado
- Gestion de QR con creacion de los tres tipos
- Nueva pestana de actividad para revisar registros de asistencia
- Accesos rapidos conectados a usuarios, QR y actividad
- Perfil basico

## Pendiente inmediato recomendado

- Seguir afinando la fidelidad visual respecto a Figma en detalles finos de layout y espaciado
- Sustituir el reinicio de sesion al arrancar por un flujo mas fino con opcion de recordar sesion cuando el proyecto lo necesite
- Valorar nuevos endpoints filtrados o validaciones de backend para dejar mas claro el alcance del historial de asistencias por rol

## Nota de seguimiento

Esta documentacion debe crecer junto al desarrollo del frontend movil y mantenerse alineada con la bitacora del proyecto en Notion.
