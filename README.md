# Hazel Gym

Hazel Gym es un proyecto intermodular de DAM para la gestion de un gimnasio. El sistema esta formado por una API REST con Spring Boot, una aplicacion web en React, una aplicacion movil Android en Kotlin/Jetpack Compose y una base de datos MySQL desplegada en AWS RDS.

El objetivo del proyecto es centralizar la gestion de usuarios, entrenadores, clientes, maquinas, clases, rutinas, cuotas, asistencias y codigos QR desde una plataforma completa y demostrable fuera del entorno local.

Acontinuación comparto toda la información técnica relacionada con la creación del proyecto, despliegues locales y en la nube, tecnologías implementadas y cómo acceder tanto a la app como a sus datos. 

## Acceso rapido

| Recurso | Enlace |
| --- | --- |
| Aplicacion web | https://main.d1mithns8dqv1b.amplifyapp.com/ |
| API publica HTTPS | https://k7edn14r3k.execute-api.eu-west-1.amazonaws.com |
| Swagger UI | https://k7edn14r3k.execute-api.eu-west-1.amazonaws.com/swagger-ui.html |
| OpenAPI JSON | https://k7edn14r3k.execute-api.eu-west-1.amazonaws.com/api-docs |
| API por proxy desde Amplify | https://main.d1mithns8dqv1b.amplifyapp.com/api-proxy/api-docs |
| Backend Elastic Beanstalk | http://hazelgym-backend.eu-west-1.elasticbeanstalk.com |
| Repositorio GitHub | https://github.com/Ander13ab/PROYECTO_INTERMODULAR |

Las credenciales de demostracion no se publican en el README por seguridad. Para la defensa se preparan usuarios de los tres roles: administrador, entrenador y cliente.

## Estado actual

- Backend desplegado en AWS Elastic Beanstalk y conectado a Amazon RDS MySQL.
- API expuesta por HTTPS mediante Amazon API Gateway.
- Frontend web desplegado en AWS Amplify.
- Aplicacion Android preparada para conectarse a la API remota de AWS.
- Autenticacion con JWT y control de permisos por rol.
- Smoke test remoto ejecutado correctamente contra API Gateway.
- Workflows de GitHub Actions para CI, backend, frontend y APK Android.

## Roles principales

| Rol | Funcionalidades |
| --- | --- |
| Administrador | Gestion de usuarios, maquinas, clases, sesiones, cuotas, codigos QR y actividad general. |
| Entrenador | Consulta de clientes, gestion de rutinas, asignaciones, sesiones y seguimiento. |
| Cliente | Consulta de perfil, maquinas, rutinas asignadas, historial de asistencia y uso de codigos QR desde la app movil. |

## Arquitectura

```text
Web usuario
  -> AWS Amplify HTTPS
  -> /api-proxy
  -> API Gateway HTTPS
  -> Elastic Beanstalk HTTP
  -> Amazon RDS MySQL

App Android
  -> API Gateway HTTPS
  -> Elastic Beanstalk HTTP
  -> Amazon RDS MySQL
```

La aplicacion web usa un proxy en Amplify para evitar problemas de contenido mixto entre HTTPS y HTTP. La aplicacion movil consume directamente la URL HTTPS de API Gateway.

## Tecnologias utilizadas

### Backend

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA / Hibernate
- Spring Security
- JWT
- Bean Validation
- MySQL
- Flyway
- SpringDoc OpenAPI / Swagger
- Maven

### Frontend web

- React
- TypeScript
- Vite
- pnpm
- CSS/Tailwind-style utility classes
- AWS Amplify Hosting

### Aplicacion movil

- Kotlin
- Jetpack Compose
- Retrofit / OkHttp
- DataStore
- CameraX
- ML Kit Barcode Scanning
- Gradle

### Cloud, despliegue y automatizacion

- Amazon RDS
- AWS Elastic Beanstalk
- Amazon API Gateway
- AWS Amplify
- GitHub
- GitHub Actions

## Estructura del proyecto

```text
PROYECTO_INTERMODULAR/
  backend/          API REST Spring Boot
  database/         Scripts SQL de esquema, seed y datos de demo
  frontend/         Aplicacion web React
  mobile-android/   Aplicacion Android Kotlin/Jetpack Compose
  docs/             Documentacion tecnica, funcional y de despliegue
  .github/          Workflows de GitHub Actions
```

## Ejecucion local del backend

Desde PowerShell:

```powershell
cd C:\Users\ander\Documents\2DAM\PROYECTO_INTERMODULAR\backend
$env:MYSQL_URL="jdbc:mysql://localhost:3306/hazelgym?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=utf8"
$env:MYSQL_USERNAME="root"
$env:MYSQL_PASSWORD="tu_password"
$env:JWT_SECRET="clave-local-segura-de-minimo-256-bits-para-jwt"
$env:JWT_EXPIRATION_MS="86400000"
.\mvnw.cmd spring-boot:run
```

Swagger local:

```text
http://localhost:8080/swagger-ui.html
http://localhost:8080/api-docs
```

## Ejecucion local del frontend

```powershell
cd C:\Users\ander\Documents\2DAM\PROYECTO_INTERMODULAR\frontend
corepack enable pnpm
pnpm install
pnpm dev
```

Para consumir la API remota desde local:

```powershell
$env:VITE_API_BASE_URL="https://k7edn14r3k.execute-api.eu-west-1.amazonaws.com"
pnpm dev
```

En AWS Amplify la variable usada es:

```text
VITE_API_BASE_URL=/api-proxy
```

## Aplicacion Android

La app movil esta en:

```text
mobile-android/
```

Puede abrirse desde Android Studio. La APK de demostracion se genera desde:

```powershell
cd C:\Users\ander\Documents\2DAM\PROYECTO_INTERMODULAR\mobile-android
.\scripts\build-debug-apk.ps1
```

La API remota usada por defecto es:

```text
https://k7edn14r3k.execute-api.eu-west-1.amazonaws.com/
```

## Pruebas

Smoke test remoto contra AWS:

```powershell
cd C:\Users\ander\Documents\2DAM\PROYECTO_INTERMODULAR\backend
.\scripts\smoke-test.ps1 -BaseUrl "https://k7edn14r3k.execute-api.eu-west-1.amazonaws.com"
```

Resultado esperado:

```text
Smoke test completed successfully
```

El smoke test comprueba OpenAPI, registro/login, JWT, permisos por rol, maquinas, clases, sesiones, rutinas, asignaciones, cuotas, codigos QR y asistencias.

## GitHub Actions

El proyecto incluye workflows en:

```text
.github/workflows/
```

| Workflow | Uso |
| --- | --- |
| `ci.yml` | Valida backend, frontend y APK debug. |
| `android-apk.yml` | Genera una APK debug como artefacto descargable. |
| `deploy-backend-eb.yml` | Compila y despliega el backend en Elastic Beanstalk. |
| `deploy-frontend-s3.yml` | Alternativa de despliegue frontend en S3/CloudFront. |

## Documentacion

La documentacion principal esta en:

```text
docs/
```