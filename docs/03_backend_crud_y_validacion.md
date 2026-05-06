# Hazel Gym - Backend CRUD y validacion

## Fecha

7 de mayo de 2026

## Objetivo de esta iteracion

Transformar la base inicial del backend Maven en una estructura utilizable para empezar a consumir la API desde cliente web y aplicacion movil.

## Fuentes utilizadas para esta iteracion

La informacion funcional y tecnica aplicada en esta fase se ha extraido de:

- El esquema relacional definido en [database/02_schema.sql](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/database/02_schema.sql).
- Los datos de prueba incluidos en [database/03_seed.sql](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/database/03_seed.sql).
- La documentacion funcional consolidada en Notion, especialmente en la pagina `Proyecto intermodular entrega`.
- La configuracion del proyecto Spring Boot ya creada en [backend/pom.xml](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/pom.xml) y [backend/src/main/resources/application.properties](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/resources/application.properties).

## Cambios tecnicos realizados

### 1. Correccion del wrapper de Maven

Se ha corregido [backend/mvnw.cmd](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/mvnw.cmd) para evitar un error al resolver la ruta local de `.m2` en PowerShell cuando la carpeta no es un enlace simbolico.

Ademas, se ha creado [backend/.mvn/maven.config](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/.mvn/maven.config) con esta configuracion:

```text
-Dmaven.repo.local=.mvn/repository
```

Con esto, Maven intentara guardar el repositorio local dentro del propio proyecto, lo que facilita trabajar en entornos restringidos.

### 2. Entidades JPA incorporadas

Se han creado las entidades que reflejan el modelo actual de base de datos:

- `Role`
- `User`
- `Machine`
- `GymClass`
- `ClassSession`
- `QrCode`
- `Attendance`
- `Routine`
- `RoutineAssignment`
- `MembershipFee`

Tambien se han definido enumeraciones de apoyo:

- `RoleName`
- `MachineStatus`
- `QrType`

Estas clases se apoyan directamente en la estructura definida en el script SQL y mantienen los nombres de tabla y columnas necesarios mediante anotaciones JPA.

### 3. Repositorios Spring Data JPA

Se han creado los repositorios base para acceso a datos:

- `RoleRepository`
- `UserRepository`
- `MachineRepository`
- `GymClassRepository`
- `ClassSessionRepository`
- `QrCodeRepository`
- `AttendanceRepository`
- `RoutineRepository`
- `RoutineAssignmentRepository`
- `MembershipFeeRepository`

En esta fase ya hay soporte especifico para:

- Busqueda de usuario por email.
- Comprobacion de email duplicado.
- Busqueda de rol por nombre.

### 4. DTOs de entrada y salida

Para evitar exponer entidades directamente en los endpoints ya se han creado DTOs para el CRUD inicial:

#### Usuarios

- `UserCreateRequest`
- `UserUpdateRequest`
- `UserResponse`

#### Maquinas

- `MachineRequest`
- `MachineResponse`

Esto permite separar:

- lo que recibe la API
- lo que se persiste
- lo que se devuelve al cliente

### 5. Servicios de negocio

Se han creado:

- `UserService` y `UserServiceImpl`
- `MachineService` y `MachineServiceImpl`

La logica incluida por ahora cubre:

- listado
- consulta por id
- creacion
- actualizacion
- borrado

En usuarios se ha anadido tambien:

- validacion de email unico
- comprobacion de existencia del rol antes de guardar

### 6. Controladores REST

Ya existen dos controladores funcionales:

- [backend/src/main/java/com/hazelgym/controller/UserController.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/controller/UserController.java)
- [backend/src/main/java/com/hazelgym/controller/MachineController.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/controller/MachineController.java)

#### Endpoints disponibles

##### Usuarios

- `GET /api/users`
- `GET /api/users/{id}`
- `POST /api/users`
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`

##### Maquinas

- `GET /api/machines`
- `GET /api/machines/{id}`
- `POST /api/machines`
- `PUT /api/machines/{id}`
- `DELETE /api/machines/{id}`

### 7. Validaciones y manejo global de errores

Se ha incorporado validacion con `jakarta.validation` en los DTOs de entrada:

- campos obligatorios
- tamanos maximos
- formato de email
- longitud minima de password

Tambien se ha creado un manejo global de errores con:

- `ResourceNotFoundException`
- `DuplicateResourceException`
- `ApiErrorResponse`
- `GlobalExceptionHandler`

Con esta base, la API ya puede devolver respuestas estructuradas para:

- `404 Not Found`
- `409 Conflict`
- `400 Bad Request`
- `500 Internal Server Error`

## Estado actual del CRUD

### CRUD ya implementado

- `usuarios`
- `maquinas`
- `clases`
- `rutinas`

### Entidades modeladas pero aun sin CRUD expuesto

- `sesiones_clase`
- `codigos_qr`
- `asistencias`
- `rutinas_clientes`
- `cuotas`

## Validacion del wrapper de Maven

Se han realizado varias comprobaciones sobre el wrapper:

### Problema 1 resuelto

El script fallaba antes de arrancar Maven por una gestion incorrecta de la propiedad `Target` de la carpeta `.m2` en PowerShell. Esto se ha corregido.

### Problema 2 resuelto

Maven intentaba usar una carpeta de repositorio local no escribible en este entorno. Se ha redirigido a:

```text
backend/.mvn/repository
```

### Bloqueo actual

La validacion completa con `mvnw.cmd test` sigue sin completarse en este entorno porque Maven necesita descargar dependencias desde:

- `https://repo.maven.apache.org`

Y la red del entorno de ejecucion esta restringida, por lo que no puede resolver el parent POM de Spring Boot.

## Implicacion practica

En tu equipo, si tienes conexion a internet, el wrapper ya deberia poder continuar mucho mejor que antes. El siguiente paso para comprobarlo en local es:

```powershell
cd backend
.\mvnw.cmd test
```

Si falla en tu maquina, ya no esperaria un fallo del script, sino uno de dependencias, versionado o compilacion real del proyecto.

## Ampliacion posterior en esta misma fecha

Despues de este bloque inicial se ha ampliado la API REST con soporte completo para `clases` y `rutinas`.

### CRUD de clases

Se han anadido los siguientes archivos:

- [backend/src/main/java/com/hazelgym/controller/GymClassController.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/controller/GymClassController.java)
- [backend/src/main/java/com/hazelgym/service/GymClassService.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/service/GymClassService.java)
- [backend/src/main/java/com/hazelgym/service/impl/GymClassServiceImpl.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/service/impl/GymClassServiceImpl.java)
- [backend/src/main/java/com/hazelgym/dto/request/GymClassRequest.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/dto/request/GymClassRequest.java)
- [backend/src/main/java/com/hazelgym/dto/response/GymClassResponse.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/dto/response/GymClassResponse.java)

La logica de negocio anade una restriccion funcional concreta: el campo `entrenadorId` solo se acepta si el usuario asociado tiene rol `TRAINER`.

### CRUD de rutinas

Se han anadido los siguientes archivos:

- [backend/src/main/java/com/hazelgym/controller/RoutineController.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/controller/RoutineController.java)
- [backend/src/main/java/com/hazelgym/service/RoutineService.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/service/RoutineService.java)
- [backend/src/main/java/com/hazelgym/service/impl/RoutineServiceImpl.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/service/impl/RoutineServiceImpl.java)
- [backend/src/main/java/com/hazelgym/dto/request/RoutineRequest.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/dto/request/RoutineRequest.java)
- [backend/src/main/java/com/hazelgym/dto/response/RoutineResponse.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/dto/response/RoutineResponse.java)

Tambien aqui se valida que la rutina solo pueda quedar vinculada a un entrenador real del sistema.

### Nuevos endpoints disponibles

- `GET /api/classes`
- `GET /api/classes/{id}`
- `POST /api/classes`
- `PUT /api/classes/{id}`
- `DELETE /api/classes/{id}`
- `GET /api/routines`
- `GET /api/routines/{id}`
- `POST /api/routines`
- `PUT /api/routines/{id}`
- `DELETE /api/routines/{id}`

## Siguiente bloque recomendado

El siguiente tramo con mejor retorno para la entrega es:

1. Primer bloque de autenticacion y login
2. JWT y proteccion por rol
3. Swagger / OpenAPI
4. Documentacion de pruebas con Postman
