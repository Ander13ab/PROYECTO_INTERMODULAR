# Hazel Gym Mobile

Base Android nativa para Hazel Gym usando Kotlin + Jetpack Compose.

## Estado actual

- Proyecto Android creado dentro de `mobile-android`
- Login conectado a `POST /api/auth/login`
- Selector de rol funcional en login con validacion contra el rol real devuelto por backend, aceptando aliases como `CLIENT`, `TRAINER` y `ADMIN`
- Sesion guardada con DataStore
- Panel inicial conectado a `GET /api/machines`
- Navegacion inicial diferenciada por rol: cliente, entrenador y admin
- Panel admin con consumo real de `GET /api/users` y `GET /api/machines`
- Panel cliente conectado a `GET /api/routines`, `GET /api/classes` y `GET /api/machines`
- Panel entrenador conectado a `GET /api/classes`, `GET /api/routine-assignments` y `GET /api/attendances`
- Estructura visual de los tres paneles acercada al lenguaje de Figma: hero, métricas y accesos rápidos
- Navegacion interna real en cliente y entrenador con pestañas de inicio, actividad/QR y perfil
- Registro de asistencia desde cliente mediante QR manual contra `POST /api/attendances`
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

## Estructura principal

- `app/src/main/java/com/hazelgym/mobile/ui`: navegacion Compose y pantallas
- `app/src/main/java/com/hazelgym/mobile/ui/viewmodel`: estado y logica de interfaz
- `app/src/main/java/com/hazelgym/mobile/data/remote`: APIs y cliente HTTP
- `app/src/main/java/com/hazelgym/mobile/data/repository`: acceso a datos
- `app/src/main/java/com/hazelgym/mobile/data/session`: persistencia de sesion

## Siguiente paso recomendado

1. Añadir escaneo QR real o lectura de codigo desde camara
2. Extender la navegacion interna al panel admin
3. Ajustar detalles visuales finos del layout y spacing
4. Rematar la fidelidad con los prototipos de Figma pantalla por pantalla
