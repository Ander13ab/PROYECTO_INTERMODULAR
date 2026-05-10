# Hazel Gym

Proyecto intermodular DAM orientado a la gestion de un gimnasio con backend en Spring Boot, base de datos MySQL y clientes previstos para movil y web.

## Estructura actual

- `backend/`: API REST en Spring Boot con Maven.
- `database/`: scripts SQL de creacion, esquema, seed y verificacion.
- `docs/`: documentacion tecnica y funcional del proyecto.
- `imagenes_Figma/`: recursos visuales del diseno.

## Estado actual

- Base de datos MySQL creada y documentada.
- Backend Spring Boot con API REST, JWT, roles y Swagger.
- Pruebas de arranque con Maven funcionando.
- En curso: validacion funcional de la API y preparacion para cliente web/movil.

## Base de datos

Orden de ejecucion de scripts:

1. `database/01_create_database.sql`
2. `database/02_schema.sql`
3. `database/03_seed.sql`
4. `database/04_verify.sql`

## Backend

El backend utiliza:

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- MySQL Driver
- Bean Validation
- JWT
- Swagger / OpenAPI

La contrasena de MySQL no se guarda en el repositorio. Debe definirse mediante la variable de entorno `MYSQL_PASSWORD`.

Variables previstas para desarrollo y despliegue:

- `MYSQL_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_MS`
- `APP_CORS_ALLOWED_ORIGINS`

Ejemplo en PowerShell:

```powershell
$env:MYSQL_PASSWORD="tu_password"
cd backend
.\mvnw.cmd spring-boot:run
```

Con el backend arrancado, se puede ejecutar una prueba rapida de API:

```powershell
.\scripts\smoke-test.ps1
```

La prueba comprueba OpenAPI, registro, JWT, permisos basicos, asistencia y CRUD de maquinas con usuario admin.

## Documentacion

La documentacion viva del proyecto se mantiene en dos sitios:

- `docs/` dentro del repositorio
- Notion, en las paginas `Proyecto intermodular entrega` y `Bitacora del proyecto`

## Siguiente objetivo

Validar los flujos funcionales principales y ampliar tests automatizados antes de empezar el frontend o despliegue.
