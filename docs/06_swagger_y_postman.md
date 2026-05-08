# Hazel Gym - Swagger y Postman

## Fecha

8 de mayo de 2026

## Objetivo de este bloque

Mejorar la documentacion operativa de la API para poder:

- inspeccionar endpoints desde Swagger
- probar flujos reales desde Postman
- facilitar la futura conexion con cliente movil o web

## Swagger / OpenAPI

### Dependencia utilizada

La integracion se apoya en:

- `springdoc-openapi-starter-webmvc-ui`

Configurada en [backend/pom.xml](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/pom.xml).

### Configuracion base

Se ha dejado la configuracion general en:

- [backend/src/main/java/com/hazelgym/security/OpenApiConfig.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/security/OpenApiConfig.java)

Y las rutas de acceso previstas en:

- [backend/src/main/resources/application.properties](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/resources/application.properties)

Rutas configuradas:

- `GET /api-docs`
- `GET /swagger-ui.html`

### Anotaciones anadidas a controladores

Se ha mejorado la documentacion de los controladores principales con:

- `@Tag`
- `@Operation`
- `@SecurityRequirement`

Controladores actualizados:

- [AuthController.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/controller/AuthController.java)
- [UserController.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/controller/UserController.java)
- [MachineController.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/controller/MachineController.java)
- [GymClassController.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/controller/GymClassController.java)
- [RoutineController.java](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/backend/src/main/java/com/hazelgym/controller/RoutineController.java)

Con esto, Swagger deberia mostrar:

- agrupacion por bloques funcionales
- descripcion basica de cada endpoint
- necesidad de token bearer en endpoints protegidos

## Postman

### Coleccion creada

Se ha creado una coleccion lista para importar en:

- [docs/postman/HazelGym.postman_collection.json](/C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/docs/postman/HazelGym.postman_collection.json)

### Contenido de la coleccion

La coleccion incluye carpetas para:

- autenticacion
- usuarios
- maquinas
- clases
- rutinas

### Variables utilizadas

- `baseUrl`
- `token`

La peticion de login guarda automaticamente el JWT en la variable `token` para reutilizarlo en el resto de peticiones.

## Prueba recomendada en tu maquina

Orden recomendado:

1. Ejecutar `.\mvnw.cmd test`
2. Ejecutar `.\mvnw.cmd spring-boot:run`
3. Abrir Swagger en `http://localhost:8080/swagger-ui.html`
4. Importar la coleccion de Postman
5. Ejecutar `login`
6. Comprobar que Postman rellena `{{token}}`
7. Probar `GET /api/auth/me`
8. Probar despues los CRUD protegidos

## Estado actual

Swagger queda preparado a nivel de configuracion y anotaciones, pero su validacion real sigue pendiente de ejecutar el backend con Maven en una maquina con acceso a internet para descargar dependencias.
