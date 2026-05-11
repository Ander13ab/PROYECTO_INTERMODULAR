# Hazel Gym Mobile

Base Android nativa para Hazel Gym usando Kotlin + Jetpack Compose.

## Estado actual

- Proyecto Android creado dentro de `mobile-android`
- Login conectado a `POST /api/auth/login`
- Sesion guardada con DataStore
- Dashboard cliente base conectado a `GET /api/machines`

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
4. Ejecuta la app en emulador o dispositivo

## Siguiente paso recomendado

1. Probar login real con el backend levantado
2. Añadir navegacion por rol
3. Conectar QR, rutinas y perfil
