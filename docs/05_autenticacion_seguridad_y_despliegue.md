# Hazel Gym - Autenticacion, seguridad y despliegue

## Fecha

7 de mayo de 2026

## Objetivo de este bloque

Incorporar autenticacion basada en JWT y preparar el backend para ser consumido desde clientes diferentes, no solo desde una app movil, sino tambien desde una futura aplicacion web.

## Decisiones tecnicas aplicadas

### 1. Autenticacion stateless

Se ha optado por un modelo stateless con JWT en lugar de sesiones de servidor. Esta decision encaja mejor con el planteamiento actual del proyecto por tres motivos:

- el backend sera consumido por varios clientes
- la app final no se quedara solo en movil
- el despliegue previsto fuera de local requiere una autenticacion facil de escalar

Este enfoque es compatible con:

- cliente movil nativo
- cliente web
- cliente Flutter, si finalmente ese es el stack elegido

### 2. Seguridad por roles

La API parte de tres roles ya definidos en base de datos:

- `CLIENT`
- `TRAINER`
- `ADMIN`

Sobre esa base se ha configurado la proteccion inicial de endpoints:

- `POST /api/auth/register` y `POST /api/auth/login` son publicos
- `GET /api/auth/me` requiere autenticacion
- `GET` de maquinas, clases y rutinas requiere token valido
- `POST`, `PUT` y `DELETE` sobre maquinas, clases y rutinas quedan restringidos a `ADMIN` o `TRAINER`
- todo el CRUD de usuarios queda restringido a `ADMIN`

### 3. CORS preparado para clientes web

Se ha incorporado configuracion CORS basada en variable de entorno para no dejar el backend atado a `localhost`.

Propiedad utilizada:

```text
app.cors.allowed-origins
```

Valor por defecto actual:

```text
http://localhost:3000,http://localhost:5173,http://localhost:8081
```

Esto cubre escenarios tipicos de:

- frontend web en desarrollo
- pruebas locales desde cliente movil o emulador

Cuando el proyecto se despliegue, esta lista debera ajustarse al dominio real del frontend.

## Dependencias anadidas en el backend

Se han incorporado dependencias nuevas en [backend/pom.xml](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/pom.xml):

- `spring-boot-starter-security`
- `jjwt-api`
- `jjwt-impl`
- `jjwt-jackson`
- `springdoc-openapi-starter-webmvc-ui`

Estas cubren:

- autenticacion
- autorizacion
- generacion y validacion de tokens JWT
- documentacion Swagger / OpenAPI

## Archivos principales incorporados

### Controlador de autenticacion

- [backend/src/main/java/com/hazelgym/controller/AuthController.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/controller/AuthController.java)

### DTOs de autenticacion

- [backend/src/main/java/com/hazelgym/dto/request/LoginRequest.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/dto/request/LoginRequest.java)
- [backend/src/main/java/com/hazelgym/dto/request/RegisterRequest.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/dto/request/RegisterRequest.java)
- [backend/src/main/java/com/hazelgym/dto/response/AuthResponse.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/dto/response/AuthResponse.java)
- [backend/src/main/java/com/hazelgym/dto/response/AuthenticatedUserResponse.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/dto/response/AuthenticatedUserResponse.java)

### Capa de servicio

- [backend/src/main/java/com/hazelgym/service/AuthService.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/service/AuthService.java)
- [backend/src/main/java/com/hazelgym/service/impl/AuthServiceImpl.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/service/impl/AuthServiceImpl.java)

### Seguridad

- [backend/src/main/java/com/hazelgym/security/SecurityConfig.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/security/SecurityConfig.java)
- [backend/src/main/java/com/hazelgym/security/JwtService.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/security/JwtService.java)
- [backend/src/main/java/com/hazelgym/security/JwtAuthenticationFilter.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/security/JwtAuthenticationFilter.java)
- [backend/src/main/java/com/hazelgym/security/CustomUserDetailsService.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/security/CustomUserDetailsService.java)
- [backend/src/main/java/com/hazelgym/security/AuthUserDetails.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/security/AuthUserDetails.java)
- [backend/src/main/java/com/hazelgym/security/OpenApiConfig.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/security/OpenApiConfig.java)

## Endpoints de autenticacion disponibles

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/me`

## Ajuste importante en el CRUD de usuarios

Se ha modificado [backend/src/main/java/com/hazelgym/service/impl/UserServiceImpl.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/service/impl/UserServiceImpl.java) para que las contrasenas creadas o actualizadas desde el CRUD se codifiquen tambien con `BCrypt`.

Esto evita que el sistema quede dividido entre:

- usuarios registrados con password cifrada
- usuarios creados manualmente con password en claro

## Variables de entorno previstas

Para dejar el backend preparado para salir de local, se han definido estas variables:

- `MYSQL_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_MS`
- `APP_CORS_ALLOWED_ORIGINS`

## Enfoque de despliegue futuro

Aunque de momento el desarrollo sigue local, el backend se esta estructurando para desplegarse posteriormente en un entorno real. Si finalmente usas AWS, esta base encaja bien con un flujo como este:

- backend desplegado como aplicacion Spring Boot empaquetada
- base de datos MySQL remota
- variables sensibles gestionadas fuera del codigo
- frontend web y cliente movil consumiendo la misma API publica

## Limitacion actual de validacion

No se ha podido ejecutar una validacion completa del proyecto en este entorno porque Maven no puede descargar dependencias desde internet aqui. El wrapper ya esta corregido a nivel de script y de repositorio local, por lo que la siguiente comprobacion real debe hacerse en tu equipo con conexion:

```powershell
cd backend
.\mvnw.cmd test
```
