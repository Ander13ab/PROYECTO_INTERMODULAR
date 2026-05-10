-- Hazel Gym - Step 3: seed data for testing
USE hazelgym;

-- Roles
INSERT INTO roles (nombre) VALUES
('CLIENT'),
('TRAINER'),
('ADMIN');

-- Users (password hash corresponds to "admin123")
INSERT INTO usuarios (nombre, email, password, rol_id, activo) VALUES
('Administrador', 'admin@hazelgym.com', '$2a$10$QGPQcQUiVHrYxmmc7tcrdOmDjrnH0ayQdjyzrZzo6DJqtVJN33Bmy', 3, TRUE),
('Carlos Martinez', 'carlos@hazelgym.com', '$2a$10$QGPQcQUiVHrYxmmc7tcrdOmDjrnH0ayQdjyzrZzo6DJqtVJN33Bmy', 1, TRUE),
('Laura Rodriguez', 'laura@hazelgym.com', '$2a$10$QGPQcQUiVHrYxmmc7tcrdOmDjrnH0ayQdjyzrZzo6DJqtVJN33Bmy', 2, TRUE);

-- Machines
INSERT INTO maquinas (nombre, descripcion, grupo_muscular, instrucciones, nivel, advertencia_seguridad, estado) VALUES
(
  'Press de banca',
  'Maquina para trabajo de pectoral, hombros y triceps',
  'Pectoral, Triceps, Hombro',
  '1. Ajusta el banco a posicion horizontal\n2. Agarre a la anchura de hombros, codos a 45 grados\n3. Baja controlado hasta el pecho, empuja sin bloquear codos',
  'Medio',
  'No uses cargas elevadas sin un companero.',
  'ACTIVA'
),
(
  'Sentadilla Smith',
  'Maquina guiada para sentadillas con seguridad',
  'Cuadriceps, Gluteos',
  '1. Coloca la barra sobre los trapecios\n2. Pies a la anchura de hombros\n3. Baja hasta que los muslos esten paralelos al suelo',
  'Principiante',
  'Manten la espalda recta durante todo el movimiento.',
  'ACTIVA'
),
(
  'Remo en polea',
  'Trabajo de espalda y dorsales',
  'Dorsal, Biceps',
  '1. Sientate con los pies apoyados\n2. Agarra el mango con las palmas hacia dentro\n3. Tira hacia el abdomen manteniendo la espalda recta',
  'Medio',
  'No arquees la espalda al tirar.',
  'ACTIVA'
);

-- QR entry and machine QRs
INSERT INTO codigos_qr (tipo, es_entrada_gimnasio) VALUES ('ENTRY', TRUE);
INSERT INTO codigos_qr (tipo, maquina_id) VALUES
('MACHINE', 1),
('MACHINE', 2),
('MACHINE', 3);

-- Class and sessions (trainer id=3)
INSERT INTO clases (nombre, descripcion, duracion, entrenador_id, activa) VALUES
('Spinning', 'Clase de ciclismo indoor de alta intensidad', 50, 3, TRUE);

INSERT INTO sesiones_clase (clase_id, fecha, hora_inicio, hora_fin) VALUES
(1, '2026-05-11', '08:30:00', '09:20:00'),
(1, '2026-05-13', '08:30:00', '09:20:00');

INSERT INTO codigos_qr (tipo, sesion_clase_id) VALUES
('CLASS_SESSION', 1),
('CLASS_SESSION', 2);

-- Routine and assignment
INSERT INTO rutinas (nombre, descripcion, entrenador_id) VALUES
(
  'Rutina Full Body Principiante',
  'Entrenamiento de cuerpo completo 3 veces por semana. Incluye sentadillas, press de banca, remo y peso muerto.',
  3
);

INSERT INTO rutinas_clientes (rutina_id, cliente_id) VALUES (1, 2);

-- Informative quotas
INSERT INTO cuotas (nombre, descripcion, precio) VALUES
('Basica Mensual', 'Acceso al gimnasio de lunes a viernes de 06:00 a 22:00', 29.90),
('Premium Mensual', 'Acceso ilimitado + 2 clases dirigidas por semana', 49.90),
('Elite Anual', 'Acceso ilimitado + clases ilimitadas + entrenador personal 1h/semana', 499.00);
