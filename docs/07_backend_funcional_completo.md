# Hazel Gym - Backend funcional completo

## Fecha

9 de mayo de 2026

## Alcance cubierto en el backend

En este punto el backend ya cuenta con soporte para las entidades principales definidas en el esquema relacional del proyecto:

- roles
- usuarios
- maquinas
- clases
- sesiones de clase
- codigos QR
- asistencias
- rutinas
- asignaciones de rutina
- cuotas

## Capas implementadas

La estructura del backend ya dispone de:

- entidades JPA
- repositorios Spring Data JPA
- DTOs de entrada y salida
- servicios de negocio
- controladores REST
- validacion de datos
- manejo global de errores
- autenticacion JWT
- proteccion por roles
- base de documentacion Swagger

## Bloques funcionales ya expuestos por API

### Autenticacion

- registro
- login
- consulta de usuario autenticado

### Gestion general

- usuarios
- maquinas
- clases
- sesiones de clase
- rutinas
- asignaciones de rutina
- cuotas

### Flujo QR

- gestion de codigos QR
- registro de asistencias

## Validaciones de negocio incorporadas

Entre las validaciones funcionales ya anadidas se encuentran:

- email de usuario unico
- codificacion segura de contrasenas con BCrypt
- `entrenadorId` valido solo para usuarios con rol `TRAINER`
- `clientId` valido solo para usuarios con rol `CLIENT`
- reglas de coherencia para codigos QR segun su tipo
- validacion horaria en sesiones de clase

## Seguridad por roles

La API ya diferencia entre:

- endpoints publicos de autenticacion
- endpoints autenticados para consulta general
- endpoints restringidos a `ADMIN`
- endpoints restringidos a `ADMIN` y `TRAINER`

## Pendientes que ya no pertenecen al nucleo del backend

El siguiente trabajo ya no consiste en “terminar el backend”, sino en consolidar y desplegar:

- pruebas reales en Postman
- validacion completa con Maven en una maquina con internet
- despliegue en entorno real
- conexion de clientes web y movil
