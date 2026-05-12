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
- Generacion de QR de entrada, maquina y sesion de clase desde el panel admin
- Escaneo QR desde la app cliente usando CameraX y ML Kit
- Registro de asistencia desde cliente con flujo manual y flujo automatico al escanear
- Dialogo de escaneo adaptado al emulador con campo de prueba por ID de QR
- Nueva vista de actividad para admin con resumen y listado de asistencias

## Decisiones tecnicas

- En emulador se usa `http://10.0.2.2:8080/` como URL base hacia el backend local
- Se permite trafico HTTP local solo para desarrollo Android
- El cliente puede crear asistencias, pero no consultar el listado completo porque el backend reserva `GET /api/attendances` para admin y trainer
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

### Entrenador

- Inicio con metricas de clases, asignaciones y asistencias
- Ajuste visual del home siguiendo la referencia de Figma
- Pestana de actividad con rutinas asignadas y asistencias recientes
- Accesos rapidos conectados a actividad y perfil
- Perfil basico

### Administrador

- Inicio con metricas de socios, entrenadores, maquinas y asistencias
- Ajuste visual del home siguiendo la referencia de Figma
- Gestion visual de usuarios
- Gestion de QR con creacion de los tres tipos
- Nueva pestana de actividad para revisar registros de asistencia
- Accesos rapidos conectados a usuarios, QR y actividad
- Perfil basico

## Pendiente inmediato recomendado

- Seguir afinando la fidelidad visual respecto a Figma en detalles finos de layout y espaciado
- Ampliar la navegacion interna real con pantallas de detalle dedicadas para rutinas, clases y maquinas
- Mostrar informacion temporal mas rica en asistencias, por ejemplo fecha y hora formateadas
- Valorar nuevos endpoints filtrados para que cliente pueda consultar su propio historial de asistencias sin exponer el listado global

## Nota de seguimiento

Esta documentacion debe crecer junto al desarrollo del frontend movil y mantenerse alineada con la bitacora del proyecto en Notion.
