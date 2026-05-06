# Hazel Gym

Proyecto intermodular DAM orientado a la gestion de un gimnasio con backend en Spring Boot, base de datos MySQL y clientes previstos para movil y web.

## Estructura actual

- `backend/`: API REST en Spring Boot con Maven.
- `database/`: scripts SQL de creacion, esquema, seed y verificacion.
- `docs/`: documentacion tecnica y funcional del proyecto.
- `imagenes_Figma/`: recursos visuales del diseno.

## Estado actual

- Base de datos creada y documentada.
- Backend Maven inicial configurado.
- En curso: analisis inicial formal y plan de implementacion del backend.

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
- MySQL Driver
- Bean Validation

La contrasena de MySQL no se guarda en el repositorio. Debe definirse mediante la variable de entorno `MYSQL_PASSWORD`.

Ejemplo en PowerShell:

```powershell
$env:MYSQL_PASSWORD="tu_password"
cd backend
.\mvnw.cmd spring-boot:run
```

## Documentacion

La documentacion viva del proyecto se mantiene en dos sitios:

- `docs/` dentro del repositorio
- Notion, en las paginas `Proyecto intermodular entrega` y `Bitacora del proyecto`

## Siguiente objetivo

Implementar entidades JPA, repositorios, servicios y CRUD basicos para empezar a probar la API REST.
