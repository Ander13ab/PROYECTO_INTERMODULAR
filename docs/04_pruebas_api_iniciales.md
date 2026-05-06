# Hazel Gym - Pruebas iniciales de API

## Estado

Documento preparado para registrar pruebas reales en cuanto el backend quede ejecutandose en local con Maven.

## Endpoints ya preparados para prueba

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
