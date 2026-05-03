CREATE TABLE `roles` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(50) UNIQUE NOT NULL COMMENT 'CLIENT, TRAINER, ADMIN'
);

CREATE TABLE `usuarios` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(100) UNIQUE NOT NULL,
  `password` varchar(255) NOT NULL COMMENT 'Contraseña encriptada con BCrypt',
  `rol_id` int NOT NULL,
  `activo` boolean DEFAULT true,
  `fecha_creacion` timestamp DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `maquinas` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text,
  `grupo_muscular` varchar(100) COMMENT 'Ej: Pectoral, Tríceps',
  `instrucciones` text COMMENT 'Pasos para usar la máquina',
  `nivel` varchar(20) DEFAULT 'Medio' COMMENT 'Principiante, Medio, Avanzado',
  `advertencia_seguridad` text,
  `imagen_url` varchar(255),
  `estado` varchar(20) DEFAULT 'ACTIVA' COMMENT 'ACTIVA o FUERA_DE_SERVICIO'
);

CREATE TABLE `clases` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL COMMENT 'Ej: Spinning, Yoga, HIIT',
  `descripcion` text,
  `duracion` int COMMENT 'Duración en minutos',
  `entrenador_id` int NOT NULL,
  `activa` boolean DEFAULT true
);

CREATE TABLE `sesiones_clase` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `clase_id` int NOT NULL,
  `fecha` date NOT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fin` time NOT NULL
);

CREATE TABLE `codigos_qr` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `tipo` varchar(20) NOT NULL COMMENT 'ENTRY, MACHINE, CLASS_SESSION',
  `es_entrada_gimnasio` boolean DEFAULT false,
  `maquina_id` int COMMENT 'Solo si tipo=MACHINE',
  `sesion_clase_id` int COMMENT 'Solo si tipo=CLASS_SESSION'
);

CREATE TABLE `asistencias` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `qr_id` int NOT NULL,
  `fecha_hora` timestamp DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `rutinas` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text,
  `entrenador_id` int NOT NULL,
  `fecha_creacion` timestamp DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `rutinas_clientes` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `rutina_id` int NOT NULL,
  `cliente_id` int NOT NULL,
  `fecha_asignacion` timestamp DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE `cuotas` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL COMMENT 'Ej: Mensual Premium',
  `descripcion` text,
  `precio` decimal(8,2) COMMENT 'Precio mensual'
);

ALTER TABLE `usuarios` ADD FOREIGN KEY (`rol_id`) REFERENCES `roles` (`id`) ON DELETE RESTRICT;

ALTER TABLE `clases` ADD FOREIGN KEY (`entrenador_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE;

ALTER TABLE `sesiones_clase` ADD FOREIGN KEY (`clase_id`) REFERENCES `clases` (`id`) ON DELETE CASCADE;

ALTER TABLE `codigos_qr` ADD FOREIGN KEY (`maquina_id`) REFERENCES `maquinas` (`id`) ON DELETE CASCADE;

ALTER TABLE `codigos_qr` ADD FOREIGN KEY (`sesion_clase_id`) REFERENCES `sesiones_clase` (`id`) ON DELETE CASCADE;

ALTER TABLE `asistencias` ADD FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE;

ALTER TABLE `asistencias` ADD FOREIGN KEY (`qr_id`) REFERENCES `codigos_qr` (`id`) ON DELETE CASCADE;

ALTER TABLE `rutinas` ADD FOREIGN KEY (`entrenador_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE;

ALTER TABLE `rutinas_clientes` ADD FOREIGN KEY (`rutina_id`) REFERENCES `rutinas` (`id`) ON DELETE CASCADE;

ALTER TABLE `rutinas_clientes` ADD FOREIGN KEY (`cliente_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE;
