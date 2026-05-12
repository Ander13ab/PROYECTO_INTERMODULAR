# Hazel Gym Mobile

Base Android nativa para Hazel Gym usando Kotlin + Jetpack Compose.

## Estado actual

- Proyecto Android creado dentro de `mobile-android`
- Login conectado a `POST /api/auth/login`
- Selector de rol funcional en login con validacion contra el rol real devuelto por backend, aceptando aliases como `CLIENT`, `TRAINER` y `ADMIN`
- Contrasena oculta por defecto con icono de ojo para mostrarla o esconderla
- Sesion guardada con DataStore
- Navegacion inicial diferenciada por rol: cliente, entrenador y admin
- Panel admin con consumo real de usuarios, maquinas, sesiones, QR y asistencias
- Panel admin con pestanas internas de inicio, usuarios, QR, actividad y perfil
- Panel admin con creacion de QR de entrada, maquina y sesion de clase contra `POST /api/qr-codes`
- Panel cliente conectado a rutinas, clases y maquinas
- Panel cliente con pestanas internas de inicio, QR y perfil
- Panel cliente con escaneo QR por camara usando CameraX y ML Kit
- Panel cliente con registro de asistencia manual y automatico al escanear
- Dialogo de escaneo adaptado al emulador con campo de prueba por ID de QR
- Panel entrenador conectado a clases, asignaciones y asistencias
- Panel entrenador con pestanas internas de inicio, actividad y perfil
- Estructura visual de los tres paneles acercada al lenguaje de Figma: hero, metricas y accesos rapidos
- Conexion local del emulador usando `http://10.0.2.2:8080/`
- Configuracion de red preparada para permitir HTTP local en desarrollo

## Importante para el backend local

Si ejecutas el backend en tu PC y abres la app en el emulador de Android Studio:

- usa `http://10.0.2.2:8080/`

Ese valor ya esta puesto en:

- `app/build.gradle.kts`

Si pruebas la app en un movil fisico, tendras que cambiar esa URL por la IP local de tu ordenador, por ejemplo:

- `http://192.168.1.34:8080/`

## Como abrirlo

1. Abre Android Studio
2. Selecciona la carpeta `mobile-android`
3. Espera a que sincronice Gradle
4. Arranca el backend en `localhost:8080`
5. Ejecuta la app en emulador o dispositivo

## Flujo actual

1. El usuario elige un rol en login
2. Introduce sus credenciales
3. La app autentica contra el backend
4. Si el rol elegido no coincide con el rol real de la cuenta, la app muestra error y no guarda la sesion
5. Si coincide, se guarda la sesion y se entra al panel inicial correspondiente al rol
6. El admin puede generar QR de entrada, maquina y sesion
7. El cliente puede registrar asistencia introduciendo el ID del QR o escaneandolo
8. El admin y el entrenador pueden revisar asistencias desde sus paneles

## Estructura principal

- `app/src/main/java/com/hazelgym/mobile/ui`: navegacion Compose y pantallas
- `app/src/main/java/com/hazelgym/mobile/ui/viewmodel`: estado y logica de interfaz
- `app/src/main/java/com/hazelgym/mobile/data/remote`: APIs y cliente HTTP
- `app/src/main/java/com/hazelgym/mobile/data/repository`: acceso a datos
- `app/src/main/java/com/hazelgym/mobile/data/session`: persistencia de sesion

## Siguiente paso recomendado

1. Ajustar detalles visuales finos del layout y spacing
2. Rematar la fidelidad con los prototipos de Figma pantalla por pantalla
3. Anadir navegacion interna real desde las tarjetas hacia vistas de detalle
4. Valorar endpoints filtrados para historial propio de asistencias del cliente
