# Hazel Gym - Pruebas iniciales de API

## Estado

Documento preparado para registrar pruebas reales en cuanto el backend quede ejecutandose en local con Maven.

## Endpoints ya preparados para prueba

### Autenticacion

#### Registro

```http
POST /api/auth/register
Content-Type: application/json

{
  "nombre": "Nuevo cliente",
  "email": "nuevo@hazelgym.com",
  "password": "hazel123"
}
```

#### Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "admin@hazelgym.com",
  "password": "admin123"
}
```

#### Usuario autenticado

```http
GET /api/auth/me
Authorization: Bearer TU_TOKEN
```

### Usuarios

#### Listar usuarios

```http
GET /api/users
```

#### Obtener usuario por id

```http
GET /api/users/1
```

#### Crear usuario

```http
POST /api/users
Content-Type: application/json

{
  "nombre": "Usuario de prueba",
  "email": "prueba@hazelgym.com",
  "password": "hazel123",
  "roleId": 1,
  "activo": true
}
```

#### Actualizar usuario

```http
PUT /api/users/2
Content-Type: application/json

{
  "nombre": "Carlos Martinez actualizado",
  "email": "carlos@hazelgym.com",
  "password": "admin123",
  "roleId": 1,
  "activo": true
}
```

#### Eliminar usuario

```http
DELETE /api/users/2
```

### Maquinas

#### Listar maquinas

```http
GET /api/machines
```

#### Obtener maquina por id

```http
GET /api/machines/1
```

#### Crear maquina

```http
POST /api/machines
Content-Type: application/json

{
  "nombre": "Prensa de piernas",
  "descripcion": "Maquina guiada para tren inferior",
  "grupoMuscular": "Cuadriceps, gluteos",
  "instrucciones": "Ajusta el asiento, coloca los pies en la plataforma y empuja sin bloquear rodillas.",
  "nivel": "Medio",
  "advertenciaSeguridad": "No usar con exceso de carga si hay molestias lumbares.",
  "imagenUrl": "https://example.com/prensa.jpg",
  "estado": "ACTIVA"
}
```

#### Actualizar maquina

```http
PUT /api/machines/1
Content-Type: application/json

{
  "nombre": "Press de banca",
  "descripcion": "Maquina para trabajo de pectoral, hombros y triceps",
  "grupoMuscular": "Pectoral, Triceps, Hombro",
  "instrucciones": "Ajusta el banco, manten el control del movimiento y evita bloquear los codos.",
  "nivel": "Medio",
  "advertenciaSeguridad": "No uses cargas elevadas sin supervision.",
  "imagenUrl": null,
  "estado": "ACTIVA"
}
```

#### Eliminar maquina

```http
DELETE /api/machines/3
```

## Respuestas de error esperadas

### Validacion incorrecta

Debe devolver `400 Bad Request` con detalle por campo cuando falten datos obligatorios o el email sea invalido.

### Recurso no encontrado

Debe devolver `404 Not Found` con mensaje descriptivo cuando el `id` no exista.

### Conflicto por email duplicado

Debe devolver `409 Conflict` si se intenta crear o actualizar un usuario con un email ya registrado.

### Clases

#### Listar clases

```http
GET /api/classes
```

#### Obtener clase por id

```http
GET /api/classes/1
```

#### Crear clase

```http
POST /api/classes
Content-Type: application/json

{
  "nombre": "Pilates",
  "descripcion": "Clase dirigida orientada a movilidad y control corporal.",
  "duracion": 45,
  "entrenadorId": 3,
  "activa": true
}
```

#### Actualizar clase

```http
PUT /api/classes/1
Content-Type: application/json

{
  "nombre": "Spinning avanzado",
  "descripcion": "Sesion de mayor intensidad para usuarios con experiencia.",
  "duracion": 50,
  "entrenadorId": 3,
  "activa": true
}
```

#### Eliminar clase

```http
DELETE /api/classes/2
```

### Rutinas

#### Listar rutinas

```http
GET /api/routines
```

#### Obtener rutina por id

```http
GET /api/routines/1
```

#### Crear rutina

```http
POST /api/routines
Content-Type: application/json

{
  "nombre": "Rutina tren superior",
  "descripcion": "Trabajo de empuje y traccion en tres dias semanales.",
  "entrenadorId": 3
}
```

#### Actualizar rutina

```http
PUT /api/routines/1
Content-Type: application/json

{
  "nombre": "Rutina full body intermedia",
  "descripcion": "Plan de tres dias con progresion de cargas.",
  "entrenadorId": 3
}
```

#### Eliminar rutina

```http
DELETE /api/routines/1
```

### Validacion de entrenador

En `clases` y `rutinas`, si el `entrenadorId` pertenece a un usuario que no tiene rol `TRAINER`, la API debe rechazar la operacion.
