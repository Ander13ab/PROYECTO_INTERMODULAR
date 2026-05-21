# CI/CD, AWS y APK - cierre de entrega

## Objetivo

Preparar Hazel Gym para:

- validar el proyecto automaticamente con GitHub Actions;
- desplegar la web en AWS S3 + CloudFront;
- desplegar el backend Spring Boot en AWS Elastic Beanstalk;
- generar una APK Android instalable como artifact.

## Workflows creados

### CI general

Archivo: `.github/workflows/ci.yml`

Valida:

- backend con Maven;
- frontend con pnpm, TypeScript y build;
- Android generando APK debug.

### APK Android

Archivo: `.github/workflows/android-apk.yml`

Se lanza manualmente desde GitHub Actions y genera:

- `hazelgym-debug-apk`

La APK queda disponible en los artifacts del workflow.

Parametro importante:

- `api_base_url`: URL del backend que usara la APK.

Para emulador local:

- `http://10.0.2.2:8080/`

Para movil fisico o demo externa:

- URL publica del backend desplegado en AWS.

### Despliegue frontend

Archivo: `.github/workflows/deploy-frontend-s3.yml`

Compila React/Vite y sube `frontend/dist` a S3.

Si se configura CloudFront, invalida cache automaticamente.

### Despliegue backend

Archivo: `.github/workflows/deploy-backend-eb.yml`

Compila el backend, genera un paquete para Elastic Beanstalk y actualiza el entorno.

El paquete incluye:

- `hazelgym.jar`
- `Procfile`

## Secretos necesarios en GitHub

Estos secretos se configuran en:

`GitHub > Settings > Secrets and variables > Actions`

### AWS general

- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `AWS_REGION`

### Frontend

- `AWS_S3_BUCKET`
- `AWS_CLOUDFRONT_DISTRIBUTION_ID`
- `VITE_API_BASE_URL`

`VITE_API_BASE_URL` debe apuntar al backend desplegado, por ejemplo:

- `https://api.hazelgym...`

### Backend Elastic Beanstalk

- `AWS_BACKEND_ARTIFACT_BUCKET`
- `AWS_EB_APPLICATION_NAME`
- `AWS_EB_ENVIRONMENT_NAME`

## Variables necesarias en AWS para backend

En el entorno de Elastic Beanstalk hay que definir:

- `MYSQL_URL`
- `MYSQL_USERNAME`
- `MYSQL_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_MS`
- `APP_CORS_ALLOWED_ORIGINS`

Ejemplo de `MYSQL_URL`:

```text
jdbc:mysql://hazelgym-db.xxxxxx.eu-west-1.rds.amazonaws.com:3306/hazelgym?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=utf8
```

Ejemplo de `APP_CORS_ALLOWED_ORIGINS`:

```text
https://tu-dominio-cloudfront.cloudfront.net,http://localhost:5173
```

## Comandos locales utiles

### Backend

```powershell
cd C:\Users\ander\Documents\2DAM\PROYECTO_INTERMODULAR\backend
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

### Frontend

```powershell
cd C:\Users\ander\Documents\2DAM\PROYECTO_INTERMODULAR\frontend
pnpm install
pnpm build
pnpm dev
```

Para apuntar la web a otro backend:

```powershell
$env:VITE_API_BASE_URL="https://URL_DEL_BACKEND"
pnpm build
```

### Android APK local

```powershell
cd C:\Users\ander\Documents\2DAM\PROYECTO_INTERMODULAR\mobile-android
.\gradlew.bat assembleDebug
```

La APK se genera en:

```text
mobile-android/app/build/outputs/apk/debug/app-debug.apk
```

Para generar APK apuntando al backend desplegado:

```powershell
$env:HAZELGYM_API_BASE_URL="https://URL_DEL_BACKEND/"
.\gradlew.bat assembleDebug
```

## Orden recomendado de despliegue

1. Crear RDS MySQL.
2. Cargar esquema y seed inicial.
3. Crear Elastic Beanstalk para backend.
4. Configurar variables de entorno del backend.
5. Ejecutar workflow `deploy-backend-eb.yml`.
6. Crear bucket S3 para frontend.
7. Crear CloudFront apuntando al bucket.
8. Configurar `VITE_API_BASE_URL` en GitHub.
9. Ejecutar workflow `deploy-frontend-s3.yml`.
10. Generar APK con `android-apk.yml` usando la URL publica del backend.

## Criterio de demo lista

- La web abre desde URL publica.
- Login funciona con admin, entrenador y cliente.
- La APK se instala en un dispositivo o emulador.
- La APK puede iniciar sesion contra el backend desplegado.
- Se puede registrar asistencia mediante QR de maquina o sesion.
