# Base de datos Hazel Gym (paso a paso)

Esta carpeta contiene todo lo necesario para crear la base de datos desde cero, sin sistema de recompensas/puntos/ranking.

## Orden de ejecucion

1. `01_create_database.sql`
2. `02_schema.sql`
3. `03_seed.sql`
4. `04_verify.sql`

Si la base de datos ya existia y solo quieres actualizar los datos de demo para el flujo QR de maquinas, ejecuta tambien:

- `05_demo_machine_media.sql`

Si has ejecutado muchas pruebas smoke y quieres limpiar registros temporales de demo:

- `06_cleanup_smoke_data.sql`

Para preparar la base de datos de cara a la entrega final, con datos mas defendibles y enlaces utiles para las maquinas:

- `07_prepare_delivery_demo_data.sql`

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

Para enriquecer una base ya creada con URLs de recurso/video para las maquinas:

```sql
SOURCE C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/database/05_demo_machine_media.sql;
```

Para limpiar usuarios y registros temporales creados por smoke tests:

```sql
SOURCE C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/database/06_cleanup_smoke_data.sql;
```

Para dejar RDS listo para la demo final:

```sql
SOURCE C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/database/07_prepare_delivery_demo_data.sql;
```

## Opcion B: desde MySQL Workbench

1. Abre cada archivo SQL en orden.
2. Ejecuta cada uno con el icono de rayo.
3. Comprueba resultados del archivo `04_verify.sql`.

## Opcion C: cargar en AWS RDS

El endpoint actual de RDS para el despliegue es:

```text
hazelgym-db.cpsq2kmkisyt.eu-west-1.rds.amazonaws.com
```

Para probar la conexion y cargar la base en RDS desde PowerShell:

```powershell
.\database\load-rds.ps1
```

El script pide la password de RDS en pantalla, no la guarda en archivos y ejecuta:

1. `02_schema.sql`
2. `03_seed.sql`
3. `05_demo_machine_media.sql`
4. `04_verify.sql`

Antes de entregar o grabar la demo, ejecuta tambien:

```text
07_prepare_delivery_demo_data.sql
```

Si la conexion falla antes de pedir o cargar datos, revisa en AWS:

- RDS debe estar en estado `Available`.
- `Public access` debe estar en `Yes` mientras cargas desde tu PC.
- El security group debe permitir `MySQL/Aurora`, puerto `3306`, origen `My IP`.
- El usuario configurado actualmente es `admin_hazelgym`.

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
