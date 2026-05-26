# Validacion final de entrega - 26 mayo 2026

## Objetivo

Dejar Hazel Gym preparado para defensa y entrega final, evitando abrir funcionalidades nuevas salvo errores criticos.

## Estado validado desde el entorno de trabajo

### Backend

- `mvnw test` ejecutado correctamente.
- Tests pasados: 2.
- Fallos: 0.
- Errores: 0.

Nota: `mvnw clean package -DskipTests` y `mvnw package -DskipTests` no pudieron completarse desde este entorno porque Maven intento descargar plugins no cacheados y la red del sandbox esta restringida. No se considera un fallo del proyecto.

### Frontend web

- `pnpm build` ejecutado correctamente.
- TypeScript validado con `tsc --noEmit`.
- La configuracion local ignorada por Git apunta a API Gateway:

```text
VITE_API_BASE_URL=https://k7edn14r3k.execute-api.eu-west-1.amazonaws.com
```

- En Amplify, la configuracion de produccion debe mantenerse como:

```text
VITE_API_BASE_URL=/api-proxy
```

### Android

- La app Android apunta por defecto a API Gateway:

```text
https://k7edn14r3k.execute-api.eu-west-1.amazonaws.com/
```

- El script de APK queda preparado para generar una APK instalable contra AWS:

```powershell
cd C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/mobile-android
.\scripts\build-debug-apk.ps1
```

Nota: desde este entorno no se pudo generar la APK porque Gradle intento descargar su distribucion y la red del sandbox esta restringida. Desde el equipo real se genero correctamente la APK debug.

APK generada inicialmente:

```text
C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/mobile-android/app/build/outputs/apk/debug/app-debug.apk
```

## Arquitectura final para defensa

```text
Web usuario
  -> AWS Amplify HTTPS
  -> /api-proxy
  -> API Gateway HTTPS
  -> Elastic Beanstalk HTTP
  -> RDS MySQL

App Android
  -> API Gateway HTTPS
  -> Elastic Beanstalk HTTP
  -> RDS MySQL
```

## Checklist tecnica final

### 1. Base de datos

En MySQL Workbench, conectado a RDS:

```sql
USE hazelgym;
SOURCE C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/database/07_prepare_delivery_demo_data.sql;
```

Si Workbench bloquea por Safe Updates:

```sql
SET SQL_SAFE_UPDATES = 0;
SOURCE C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/database/07_prepare_delivery_demo_data.sql;
SET SQL_SAFE_UPDATES = 1;
```

### 2. Backend remoto

Ejecutar desde PowerShell real:

```powershell
cd C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend
.\scripts\smoke-test.ps1 -BaseUrl "https://k7edn14r3k.execute-api.eu-west-1.amazonaws.com"
```

Resultado esperado:

```text
Smoke test completed successfully
```

Resultado obtenido desde PowerShell real:

```text
Smoke test completed successfully
```

### 3. Web desplegada

Abrir:

```text
https://main.d1mithns8dqv1b.amplifyapp.com/
```

Validar:

- Login admin: `admin@hazelgym.com`
- Login cliente: `carlos@hazelgym.com`
- Login entrenador: `laura@hazelgym.com`
- Listados cargan sin errores.
- No aparecen errores CORS ni Mixed Content en consola.

### 4. Android APK

Generar APK desde Android Studio:

```text
Build > Build Bundle(s) / APK(s) > Build APK(s)
```

O desde PowerShell real:

```powershell
cd C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/mobile-android
.\scripts\build-debug-apk.ps1
```

APK esperada:

```text
mobile-android/app/build/outputs/apk/debug/HazelGym-1.0-debug.apk
```

Validar en movil fisico:

- La app abre correctamente.
- Login admin, cliente y entrenador funciona.
- Cliente puede abrir maquinas.
- Cliente puede consultar instrucciones/recurso de una maquina.
- QR de maquina funciona por camara o por ID de prueba.
- QR de sesion registra asistencia.

## Evidencias recomendadas para el informe

- Captura de Amplify con deploy correcto.
- Captura de API Gateway con ruta `ANY /{proxy+}`.
- Captura de Elastic Beanstalk en estado OK.
- Captura de RDS activo.
- Captura de Swagger remoto.
- Captura de smoke test remoto correcto.
- Capturas de web para los tres roles.
- Capturas de app Android para los tres roles.
- Captura o video corto del flujo QR de maquina.
- Captura o video corto del flujo QR de sesion/asistencia.

## Criterio de congelacion

A partir de esta validacion solo se deben hacer:

- correcciones de errores;
- ajustes visuales pequenos;
- mejoras de documentacion;
- preparacion del informe y guion de defensa.

No se recomienda anadir nuevas funcionalidades grandes antes de la entrega.
