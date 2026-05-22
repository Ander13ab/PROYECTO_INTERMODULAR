# Generacion de APK Android

## Objetivo

Generar una APK debug instalable de Hazel Gym para poder probar la app fuera del boton `Run` de Android Studio.

## Ruta de salida

La APK debug se genera en:

```text
C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/mobile-android/app/build/outputs/apk/debug/app-debug.apk
```

## Opcion A: Android Studio

1. Abre Android Studio.
2. Abre la carpeta `mobile-android`.
3. Espera a que Gradle sincronice correctamente.
4. En el menu superior, entra en `Build`.
5. Pulsa `Build Bundle(s) / APK(s)`.
6. Pulsa `Build APK(s)`.
7. Cuando termine, Android Studio mostrara una notificacion con la ruta de la APK.

Esta opcion es la mas comoda si Android Studio ya tiene descargadas las dependencias.

## Opcion B: PowerShell con backend local

Esta APK sirve para probar en emulador con el backend arrancado en tu PC:

```powershell
cd C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/mobile-android
.\scripts\build-debug-apk.ps1
```

La URL usada por defecto es:

```text
http://10.0.2.2:8080/
```

`10.0.2.2` es la direccion especial que usa el emulador Android para acceder al `localhost` del ordenador.

## Opcion C: PowerShell con backend desplegado

Esta APK sirve para instalar en un movil fisico o para probar contra AWS:

```powershell
cd C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/mobile-android
.\scripts\build-debug-apk.ps1 -ApiBaseUrl "https://URL_DEL_BACKEND/"
```

La URL debe terminar en `/`.

## Instalacion en emulador

Con el emulador abierto:

```powershell
adb install -r C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/mobile-android/app/build/outputs/apk/debug/app-debug.apk
```

Si `adb` no esta disponible en PowerShell, tambien puedes arrastrar el archivo `.apk` encima del emulador.

## Instalacion en movil fisico

1. Genera la APK contra una URL publica del backend, no contra `10.0.2.2`.
2. Pasa el archivo `app-debug.apk` al movil.
3. Activa la instalacion desde origenes desconocidos si Android lo pide.
4. Instala la APK.

Una APK instalada en movil fisico no puede acceder al backend local mediante `10.0.2.2`. Para movil fisico necesitas backend desplegado o una IP local accesible desde la misma red.

## Validacion minima

Antes de dar la APK por buena:

1. Abrir la app.
2. Entrar como `admin@hazelgym.com`.
3. Entrar como `carlos@hazelgym.com`.
4. Entrar como `laura@hazelgym.com`.
5. Comprobar que el cliente puede abrir la pestana `Maquinas`.
6. Probar QR de maquina con ID manual o camara.
7. Verificar que se muestra la ficha de la maquina.
