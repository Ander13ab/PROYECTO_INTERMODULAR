-- Hazel Gym - Step 2: schema (no rewards/ranking)
USE hazelgym;

CREATE TABLE IF NOT EXISTS roles (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(50) UNIQUE NOT NULL COMMENT 'CLIENT, TRAINER, ADMIN'
);

CREATE TABLE IF NOT EXISTS usuarios (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL COMMENT 'Password hashed with BCrypt',
  rol_id INT NOT NULL,
  activo BOOLEAN DEFAULT TRUE,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS maquinas (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  descripcion TEXT,
  grupo_muscular VARCHAR(100) COMMENT 'Ej: Pectoral, Triceps',
  instrucciones TEXT COMMENT 'Pasos para usar la maquina',
  nivel VARCHAR(20) DEFAULT 'Medio' COMMENT 'Principiante, Medio, Avanzado',
  advertencia_seguridad TEXT,
  imagen_url VARCHAR(255),
  estado VARCHAR(20) DEFAULT 'ACTIVA' COMMENT 'ACTIVA o FUERA_DE_SERVICIO'
);

CREATE TABLE IF NOT EXISTS clases (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL COMMENT 'Ej: Spinning, Yoga, HIIT',
  descripcion TEXT,
  duracion INT COMMENT 'Duracion en minutos',
  entrenador_id INT NOT NULL,
  activa BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS sesiones_clase (
  id INT PRIMARY KEY AUTO_INCREMENT,
  clase_id INT NOT NULL,
  fecha DATE NOT NULL,
  hora_inicio TIME NOT NULL,
  hora_fin TIME NOT NULL
);

CREATE TABLE IF NOT EXISTS codigos_qr (
  id INT PRIMARY KEY AUTO_INCREMENT,
  tipo VARCHAR(20) NOT NULL COMMENT 'ENTRY, MACHINE, CLASS_SESSION',
  es_entrada_gimnasio BOOLEAN DEFAULT FALSE,
  maquina_id INT NULL COMMENT 'Solo si tipo=MACHINE',
  sesion_clase_id INT NULL COMMENT 'Solo si tipo=CLASS_SESSION'
);

CREATE TABLE IF NOT EXISTS asistencias (
  id INT PRIMARY KEY AUTO_INCREMENT,
  usuario_id INT NOT NULL,
  qr_id INT NOT NULL,
  fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rutinas (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  descripcion TEXT,
  entrenador_id INT NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rutinas_clientes (
  id INT PRIMARY KEY AUTO_INCREMENT,
  rutina_id INT NOT NULL,
  cliente_id INT NOT NULL,
  fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cuotas (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL COMMENT 'Ej: Mensual Premium',
  descripcion TEXT,
  precio DECIMAL(8,2) COMMENT 'Precio mensual'
);

ALTER TABLE usuarios
  ADD CONSTRAINT fk_usuarios_rol
  FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE RESTRICT;

ALTER TABLE clases
  ADD CONSTRAINT fk_clases_entrenador
  FOREIGN KEY (entrenador_id) REFERENCES usuarios(id) ON DELETE CASCADE;

ALTER TABLE sesiones_clase
  ADD CONSTRAINT fk_sesiones_clase
  FOREIGN KEY (clase_id) REFERENCES clases(id) ON DELETE CASCADE;

ALTER TABLE codigos_qr
  ADD CONSTRAINT fk_qr_maquina
  FOREIGN KEY (maquina_id) REFERENCES maquinas(id) ON DELETE CASCADE;

ALTER TABLE codigos_qr
  ADD CONSTRAINT fk_qr_sesion
  FOREIGN KEY (sesion_clase_id) REFERENCES sesiones_clase(id) ON DELETE CASCADE;

ALTER TABLE asistencias
  ADD CONSTRAINT fk_asistencias_usuario
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE;

ALTER TABLE asistencias
  ADD CONSTRAINT fk_asistencias_qr
  FOREIGN KEY (qr_id) REFERENCES codigos_qr(id) ON DELETE CASCADE;

ALTER TABLE rutinas
  ADD CONSTRAINT fk_rutinas_entrenador
  FOREIGN KEY (entrenador_id) REFERENCES usuarios(id) ON DELETE CASCADE;

ALTER TABLE rutinas_clientes
  ADD CONSTRAINT fk_rutina_cliente_rutina
  FOREIGN KEY (rutina_id) REFERENCES rutinas(id) ON DELETE CASCADE;

ALTER TABLE rutinas_clientes
  ADD CONSTRAINT fk_rutina_cliente_usuario
  FOREIGN KEY (cliente_id) REFERENCES usuarios(id) ON DELETE CASCADE;
