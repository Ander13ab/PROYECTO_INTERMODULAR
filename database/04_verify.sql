-- Hazel Gym - Step 4: verification queries
USE hazelgym;

SHOW TABLES;

SELECT * FROM roles;

SELECT u.id, u.nombre, u.email, r.nombre AS rol
FROM usuarios u
JOIN roles r ON r.id = u.rol_id
ORDER BY u.id;

SELECT id, nombre, grupo_muscular, nivel, estado
FROM maquinas
ORDER BY id;

SELECT qr.id, qr.tipo, qr.es_entrada_gimnasio, m.nombre AS maquina, sc.id AS sesion_clase
FROM codigos_qr qr
LEFT JOIN maquinas m ON m.id = qr.maquina_id
LEFT JOIN sesiones_clase sc ON sc.id = qr.sesion_clase_id
ORDER BY qr.id;

SELECT c.nombre AS clase, sc.fecha, sc.hora_inicio, sc.hora_fin
FROM sesiones_clase sc
JOIN clases c ON c.id = sc.clase_id
ORDER BY sc.id;

SELECT r.nombre AS rutina, cli.nombre AS cliente, ent.nombre AS entrenador
FROM rutinas_clientes rc
JOIN rutinas r ON r.id = rc.rutina_id
JOIN usuarios cli ON cli.id = rc.cliente_id
JOIN usuarios ent ON ent.id = r.entrenador_id
ORDER BY rc.id;

SELECT * FROM cuotas ORDER BY id;
