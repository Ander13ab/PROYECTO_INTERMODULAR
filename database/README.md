# Base de datos Hazel Gym (paso a paso)

Esta carpeta contiene todo lo necesario para crear la base de datos desde cero, sin sistema de recompensas/puntos/ranking.

## Orden de ejecucion

1. `01_create_database.sql`
2. `02_schema.sql`
3. `03_seed.sql`
4. `04_verify.sql`

## Requisitos previos

- MySQL Server 8.x instalado.
- Cliente `mysql` disponible en terminal o uso de MySQL Workbench.

## Opcion A: desde terminal (`mysql`)

```bash
mysql -u root -p
```

Dentro de MySQL:

```sql
SOURCE C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/database/01_create_database.sql;
SOURCE C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/database/02_schema.sql;
SOURCE C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/database/03_seed.sql;
SOURCE C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/database/04_verify.sql;
```

## Opcion B: desde MySQL Workbench

1. Abre cada archivo SQL en orden.
2. Ejecuta cada uno con el icono de rayo.
3. Comprueba resultados del archivo `04_verify.sql`.

## Resultado esperado

- 10 tablas creadas:
  - `roles`
  - `usuarios`
  - `maquinas`
  - `clases`
  - `sesiones_clase`
  - `codigos_qr`
  - `asistencias`
  - `rutinas`
  - `rutinas_clientes`
  - `cuotas`
- Datos de prueba insertados:
  - 3 roles
  - 3 usuarios
  - 3 maquinas
  - QR de entrada, maquinas y sesiones
  - 1 clase con 2 sesiones
  - 1 rutina asignada
  - 3 cuotas
