USE hazelgym;

-- Hazel Gym - datos de entrega y demo final.
-- Compatible con MySQL Workbench Safe Updates.
-- Este script evita datos temporales de smoke-test y deja contenido defendible.

DELETE FROM usuarios
WHERE id > 0 AND email LIKE 'smoke-%@hazelgym.com';

DELETE FROM maquinas
WHERE id > 0 AND nombre = 'Smoke Test Machine';

DELETE FROM cuotas
WHERE id > 0 AND nombre = 'Smoke Test Fee';

DELETE FROM clases
WHERE id > 0 AND nombre = 'Smoke Test Class';

DELETE FROM rutinas
WHERE id > 0 AND nombre = 'Smoke Test Routine';

-- Cuentas principales para demo.
-- Password de todas las cuentas: admin123
UPDATE usuarios
SET nombre = 'Marta Lopez', activo = TRUE
WHERE id > 0 AND email = 'admin@hazelgym.com';

UPDATE usuarios
SET nombre = 'Carlos Martinez', activo = TRUE
WHERE id > 0 AND email = 'carlos@hazelgym.com';

UPDATE usuarios
SET nombre = 'Laura Rodriguez', activo = TRUE
WHERE id > 0 AND email = 'laura@hazelgym.com';

-- Maquinas con instrucciones y recurso externo util para el cliente.
UPDATE maquinas
SET
  descripcion = 'Banco de press para trabajo de pectoral, hombros y triceps.',
  grupo_muscular = 'Pectoral, triceps, hombro',
  instrucciones = '1. Ajusta el banco y coloca los pies firmes en el suelo\n2. Agarra la barra algo mas ancho que los hombros\n3. Baja controlado hasta el pecho\n4. Empuja la barra sin bloquear por completo los codos',
  nivel = 'Medio',
  advertencia_seguridad = 'Usa cargas progresivas y pide ayuda si trabajas cerca del fallo.',
  imagen_url = 'https://www.wodcat.com/ejercicios/press-de-banca'
WHERE id > 0 AND nombre = 'Press de banca';

UPDATE maquinas
SET
  descripcion = 'Maquina guiada para practicar sentadilla con mayor control de trayectoria.',
  grupo_muscular = 'Cuadriceps, gluteos, femoral',
  instrucciones = '1. Coloca la barra sobre los trapecios, no sobre el cuello\n2. Situa los pies ligeramente adelantados\n3. Desciende manteniendo rodillas alineadas con los pies\n4. Sube empujando el suelo y manteniendo el torso estable',
  nivel = 'Principiante',
  advertencia_seguridad = 'No redondees la espalda y no bloquees las rodillas de forma brusca.',
  imagen_url = 'https://www.entrenamientos.com/ejercicios/sentadilla-en-maquina-smith'
WHERE id > 0 AND nombre = 'Sentadilla Smith';

UPDATE maquinas
SET
  descripcion = 'Ejercicio de polea para fortalecer dorsal, romboides y biceps.',
  grupo_muscular = 'Dorsal, romboides, biceps',
  instrucciones = '1. Sientate con el pecho erguido y pies apoyados\n2. Tira del agarre hacia el abdomen\n3. Junta ligeramente las escapulas al final\n4. Vuelve controlado sin perder la postura',
  nivel = 'Medio',
  advertencia_seguridad = 'Evita tirar con impulso o arquear la zona lumbar.',
  imagen_url = 'https://www.inspireusafoundation.org/seated-cable-row/'
WHERE id > 0 AND nombre = 'Remo en polea';

-- Clases y sesiones con fechas utiles para la demo final.
UPDATE clases
SET
  nombre = 'Spinning',
  descripcion = 'Clase cardiovascular de ciclismo indoor con bloques de intensidad guiados.',
  duracion = 50,
  activa = TRUE
WHERE id = 1;

UPDATE sesiones_clase
SET fecha = '2026-05-26', hora_inicio = '18:00:00', hora_fin = '18:50:00'
WHERE id = 1;

UPDATE sesiones_clase
SET fecha = '2026-05-27', hora_inicio = '10:00:00', hora_fin = '10:50:00'
WHERE id = 2;

-- Rutina asignada al cliente principal.
UPDATE rutinas
SET
  nombre = 'Rutina Full Body Inicial',
  descripcion = 'Rutina de adaptacion de 3 dias: press de banca, sentadilla guiada, remo en polea y trabajo de core.'
WHERE id = 1;

-- Cuotas comerciales para demo.
UPDATE cuotas
SET nombre = 'Plan Basico', descripcion = 'Acceso al gimnasio de lunes a viernes.', precio = 29.90
WHERE id > 0 AND nombre IN ('Basica Mensual', 'Plan Basico');

UPDATE cuotas
SET nombre = 'Plan Premium', descripcion = 'Acceso ilimitado al gimnasio y dos clases dirigidas por semana.', precio = 49.90
WHERE id > 0 AND nombre IN ('Premium Mensual', 'Plan Premium');

UPDATE cuotas
SET nombre = 'Plan Elite', descripcion = 'Acceso ilimitado, clases ilimitadas y seguimiento mensual con entrenador.', precio = 499.00
WHERE id > 0 AND nombre IN ('Elite Anual', 'Plan Elite');
