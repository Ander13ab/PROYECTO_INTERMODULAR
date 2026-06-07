# Hazel Gym Mobile

Base Android nativa para Hazel Gym usando Kotlin + Jetpack Compose.

## Estado actual

- Proyecto Android creado dentro de `mobile-android`
- Login conectado a `POST /api/auth/login`
- Login sin selector manual de rol: la app detecta automaticamente el rol devuelto por el backend
- Contraseña oculta por defecto con icono de ojo para mostrarla o esconderla
- Sesion guardada con DataStore
- Arranque forzado en login en cada nueva ejecucion para probar los tres roles con facilidad
- Panel admin con consumo real de usuarios, maquinas, sesiones, QR y asistencias
- Panel admin con pestanas internas de inicio, usuarios, QR, actividad y perfil
- Panel admin con pantallas de detalle para usuarios, maquinas, QR y asistencias
- Panel admin con formulario basico para crear y editar maquinas desde la propia app
- Panel admin con formulario basico para crear, editar y eliminar usuarios desde la propia app
- Panel admin con creacion de QR de entrada, maquina y sesion de clase contra `POST /api/qr-codes`
- Panel cliente conectado a rutinas, clases y maquinas
- Panel cliente con pestanas internas de inicio, QR y perfil
- Panel cliente con pantallas de detalle para rutinas, clases, maquinas e historial de asistencias
- El historial del cliente se apoya en un filtrado backend por usuario autenticado para no exponer registros de otros usuarios
- Panel cliente con escaneo QR por camara usando CameraX y ML Kit
- Panel cliente con registro de asistencia manual y automatico al escanear
- Panel cliente con escaneo de QR de maquina para abrir ficha de uso, seguridad y recurso/video asociado
- Dialogo de escaneo adaptado al emulador con campo de prueba por ID de QR
- Panel entrenador conectado a clases, asignaciones y asistencias
- Panel entrenador con pestanas internas de inicio, actividad y perfil
- Panel entrenador con pantallas de detalle para clases, asignaciones y asistencias
- Panel entrenador con gestion real de clases y rutinas propias: crear, editar y eliminar
- Panel entrenador con gestion real de asignaciones de rutinas a clientes
- Estructura visual de los tres paneles acercada al lenguaje de Figma: hero, metricas y accesos rapidos
- Conexion por defecto contra API Gateway para que la APK pueda funcionar fuera del ordenador local
- Configuracion de red preparada para permitir HTTP local si se fuerza una URL de desarrollo

## Conexion con backend

Por defecto la app apunta a la API remota publicada con API Gateway:

```text
https://k7edn14r3k.execute-api.eu-west-1.amazonaws.com/
```

Esta URL es la recomendada para APK instalada en movil fisico, porque es publica y usa HTTPS.

Si ejecutas el backend en tu PC y quieres abrir la app en el emulador de Android Studio, puedes sobrescribir la URL con:

```text
http://10.0.2.2:8080/
```

La URL por defecto esta definida en:

- `app/build.gradle.kts`

Tambien se puede cambiar sin editar codigo usando la variable:

- `HAZELGYM_API_BASE_URL`

Ejemplo para generar APK contra un backend desplegado:

```powershell
$env:HAZELGYM_API_BASE_URL="https://k7edn14r3k.execute-api.eu-west-1.amazonaws.com/"
.\gradlew.bat assembleDebug
```

Tambien puedes usar el script preparado:

```powershell
.\scripts\build-debug-apk.ps1
.\scripts\build-debug-apk.ps1 -ApiBaseUrl "https://k7edn14r3k.execute-api.eu-west-1.amazonaws.com/"
```

Si pruebas la app en un movil fisico contra un backend local, tendras que cambiar esa URL por una IP local accesible desde el movil, por ejemplo:

- `http://192.168.1.34:8080/`

## Como abrirlo

1. Abre Android Studio
2. Selecciona la carpeta `mobile-android`
3. Espera a que sincronice Gradle
4. Ejecuta la app. Por defecto conectara con API Gateway
5. Ejecuta la app en emulador o dispositivo

## Flujo actual

1. El usuario introduce sus credenciales
2. La app autentica contra el backend
3. El backend devuelve el token y el rol real de la cuenta
4. Se guarda la sesion y se entra al panel inicial correspondiente al rol
5. El admin puede generar QR de entrada, maquina y sesion
6. El cliente puede registrar asistencia introduciendo el ID del QR o escaneandolo
7. El cliente puede escanear un QR de maquina desde la pestana `Maquinas` para ver instrucciones y recurso/video
8. Cliente, entrenador y admin pueden revisar asistencias desde sus vistas de detalle

## Estructura principal

- `app/src/main/java/com/hazelgym/mobile/ui`: navegacion Compose y pantallas
- `app/src/main/java/com/hazelgym/mobile/ui/viewmodel`: estado y logica de interfaz
- `app/src/main/java/com/hazelgym/mobile/data/remote`: APIs y cliente HTTP
- `app/src/main/java/com/hazelgym/mobile/data/repository`: acceso a datos
- `app/src/main/java/com/hazelgym/mobile/data/session`: persistencia de sesion

## Siguiente paso recomendado

1. Generar y validar APK debug instalable
2. Validar QR de sesion como flujo demostrable
3. Ejecutar `07_prepare_delivery_demo_data.sql` en RDS para dejar datos finales de demo

