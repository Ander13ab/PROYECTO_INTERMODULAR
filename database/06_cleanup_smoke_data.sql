USE hazelgym;

-- Optional cleanup for local databases used during smoke tests.
-- It removes temporary records created with names/emails that start with "Smoke Test" or "smoke-".
DELETE FROM usuarios
WHERE email LIKE 'smoke-%@hazelgym.com';

DELETE FROM maquinas
WHERE nombre = 'Smoke Test Machine';

DELETE FROM cuotas
WHERE nombre = 'Smoke Test Fee';

DELETE FROM clases
WHERE nombre = 'Smoke Test Class';

DELETE FROM rutinas
WHERE nombre = 'Smoke Test Routine';
