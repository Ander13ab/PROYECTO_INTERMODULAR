USE hazelgym;

-- Demo support for the mobile QR flow.
-- Run this script if your database already existed before machine media URLs were added.
UPDATE maquinas
SET
  instrucciones = '1. Ajusta el banco a posicion horizontal\n2. Agarre a la anchura de hombros, codos a 45 grados\n3. Baja controlado hasta el pecho, empuja sin bloquear codos',
  nivel = 'Medio',
  advertencia_seguridad = 'No uses cargas elevadas sin un companero.',
  imagen_url = 'https://www.nike.com/es/a/como-y-por-que-hacer-press-de-banca'
WHERE nombre = 'Press de banca';

UPDATE maquinas
SET
  instrucciones = '1. Coloca la barra sobre los trapecios\n2. Pies a la anchura de hombros\n3. Baja hasta que los muslos esten paralelos al suelo',
  nivel = 'Principiante',
  advertencia_seguridad = 'Manten la espalda recta durante todo el movimiento.',
  imagen_url = 'https://www.entrenamientos.com/ejercicios/sentadilla-en-maquina-smith'
WHERE nombre = 'Sentadilla Smith';

UPDATE maquinas
SET
  instrucciones = '1. Sientate con los pies apoyados\n2. Agarra el mango con las palmas hacia dentro\n3. Tira hacia el abdomen manteniendo la espalda recta',
  nivel = 'Medio',
  advertencia_seguridad = 'No arquees la espalda al tirar.',
  imagen_url = 'https://www.myprotein.es/thezone/entrenamiento/remo-en-polea-baja-sentado/'
WHERE nombre = 'Remo en polea';
