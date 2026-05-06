# Hazel Gym - Plan de backend y entrega

## Objetivo del siguiente bloque de trabajo

Completar el backend hasta disponer de una API REST documentada, con autenticacion por JWT, gestion de roles y operaciones CRUD suficientes para conectar la aplicacion movil y la aplicacion web.

## Fases de implementacion

### Fase 1. Base tecnica del backend

- Configurar proyecto Spring Boot con Maven.
- Mantener conexion a MySQL mediante variables de entorno.
- Definir estructura por capas.
- Crear paquetes base del proyecto.

### Fase 2. Persistencia y dominio

- Implementar entidades JPA a partir del esquema SQL.
- Declarar enumeraciones y relaciones.
- Crear repositorios Spring Data JPA.

### Fase 3. API REST basica

- Crear DTOs de entrada y salida.
- Implementar servicios.
- Exponer CRUD de:
  - usuarios
  - maquinas
  - clases
  - rutinas
- Probar endpoints con Postman o similar.

### Fase 4. Robustez

- Añadir validaciones.
- Añadir manejo global de errores.
- Revisar respuestas HTTP coherentes.
- Preparar documentacion Swagger/OpenAPI.

### Fase 5. Seguridad

- Registro y login.
- JWT.
- Roles.
- Proteccion de endpoints segun permisos.

### Fase 6. Integracion con clientes

- Cliente web conectado a la API.
- App movil conectada a la API.
- Persistencia local basica para sesion.

### Fase 7. Despliegue y entrega

- Variables de entorno.
- Base de datos remota.
- Despliegue del backend.
- Swagger accesible.
- README final.

## Entregables documentales vinculados a la rubrica

### Analisis y diseno inicial

- Descripcion del proyecto.
- Roles de usuario.
- Casos de uso.
- Entidades y relaciones.
- Diagrama general del sistema.

### Base de datos

- Modelo entidad-relacion.
- Tablas con PK y FK.
- Scripts SQL.
- Seed.

### Backend

- Proyecto Maven.
- Estructura por capas.
- Conexion a base de datos.
- CRUD.
- Validaciones.
- Errores globales.
- Relaciones.
- Swagger.
- Seguridad con JWT y roles.

### Cliente movil y web

- Login.
- Navegacion.
- Visualizacion y creacion de datos.
- Funcionalidad por rol.
- Persistencia local basica.

## Estado real al inicio de esta fase

- `database/` ya contiene scripts funcionales.
- `backend/` tiene solo la base del proyecto y la configuracion.
- No hay aun entidades, repositorios, servicios ni controladores implementados.

## Criterio de trabajo a partir de ahora

Cada avance importante debe dejar:

- Cambio en el repositorio.
- Actualizacion en Notion de la entrega.
- Actualizacion en Notion de la bitacora.
- Commit en Git cuando el bloque tenga entidad propia.
