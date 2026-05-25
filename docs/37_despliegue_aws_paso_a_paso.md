# Despliegue AWS paso a paso

## Objetivo

Publicar Hazel Gym para que la web y la APK puedan conectarse a un backend accesible fuera del ordenador local.

## Arquitectura elegida

- Base de datos: Amazon RDS MySQL.
- Backend: Elastic Beanstalk con Spring Boot.
- Frontend web: S3 + CloudFront.
- Automatizacion: GitHub Actions.

## 1. Crear RDS MySQL

Este es el primer paso real del despliegue. La APK falla en un movil fisico porque esta apuntando a `10.0.2.2`, que solo existe dentro del emulador Android. Cuando el backend este desplegado, la app movil y la web usaran una URL publica.

### Valores recomendados para la entrega

- Motor: MySQL.
- Version: MySQL 8.
- Tipo: `db.t3.micro` o `db.t4g.micro` si aparece dentro de Free Tier.
- Despliegue: Single-AZ.
- Almacenamiento: 20 GB o el minimo que permita la consola, sin activar escalado automatico si no hace falta.
- Base de datos inicial: `hazelgym`.
- Usuario: definir uno propio, por ejemplo `hazelgym_admin`.
- Acceso publico: `Yes` solo mientras necesites cargar SQL desde tu ordenador con MySQL Workbench.
- Proteccion contra eliminacion: `No` durante la entrega, para poder borrar recursos si hay que evitar costes.

### Checklist en la consola de AWS

Entra en:

```text
AWS Console -> RDS -> Databases -> Create database
```

Selecciona:

```text
Choose a database creation method: Standard create
Engine type: MySQL
Templates: Free tier
DB instance identifier: hazelgym-db
Master username: hazelgym_admin
Credentials management: Self managed
Master password: guarda una contrasena segura fuera del repo
DB instance class: db.t3.micro o db.t4g.micro
Storage type: gp2 o gp3
Allocated storage: 20 GiB
Storage autoscaling: Off si la consola lo permite
Availability: Single-AZ
Compute resource: Don't connect to an EC2 compute resource
Public access: Yes
VPC security group: Create new
New VPC security group name: hazelgym-rds-sg
Initial database name: hazelgym
Backup retention: 1 day o desactivado si AWS lo permite para reducir coste
Deletion protection: No durante la entrega
```

### Regla de seguridad inicial

Mientras cargamos los SQL desde tu PC, el security group de RDS debe permitir tu IP actual:

```text
Type: MySQL/Aurora
Port: 3306
Source: My IP
```

No uses `0.0.0.0/0` salvo emergencia muy puntual. Si cambias de red, por ejemplo de casa a clase, tendras que actualizar `Source: My IP`.

Cuando Elastic Beanstalk este funcionando, la regla ideal sera permitir MySQL 3306 desde el security group del backend, no desde todo internet.

### Datos que tienes que guardar

Despues de crear RDS, guarda:

```text
RDS_ENDPOINT=endpoint que aparece en Connectivity & security
MYSQL_URL=jdbc:mysql://RDS_ENDPOINT:3306/hazelgym?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=utf8
MYSQL_USERNAME=hazelgym_admin
MYSQL_PASSWORD=contrasena elegida en RDS
```

No pegues la contrasena en Notion, GitHub ni en ningun archivo del proyecto. La usaremos despues como variable de entorno o secreto.

Configuracion real creada para Hazel Gym:

```text
AWS_REGION=eu-west-1
RDS_ENDPOINT=hazelgym-db.cpsq2kmkisyt.eu-west-1.rds.amazonaws.com
MYSQL_USERNAME=admin_hazelgym
MYSQL_DATABASE=hazelgym
MYSQL_PORT=3306
```

## 2. Cargar esquema y datos

Ejecuta en MySQL Workbench contra RDS:

1. Crea una nueva conexion.
2. En `Hostname`, pega el endpoint de RDS sin `:3306`.
3. En `Port`, usa `3306`.
4. En `Username`, usa el usuario master de RDS.
5. Prueba la conexion con `Test Connection`.
6. Abre una pestana SQL y ejecuta los `SOURCE`.

```sql
SOURCE C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/database/01_create_database.sql;
SOURCE C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/database/02_schema.sql;
SOURCE C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/database/03_seed.sql;
SOURCE C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/database/05_demo_machine_media.sql;
SOURCE C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/database/04_verify.sql;
```

Si RDS ya crea la base `hazelgym`, puedes omitir `01_create_database.sql` y ejecutar desde `02_schema.sql`.

Si Workbench no conecta:

- Comprueba que el endpoint no tenga `https://` ni `/`.
- Comprueba que el puerto sea `3306`.
- Comprueba que el security group tenga `My IP`.
- Comprueba que `Public access` este en `Yes` durante esta fase.
- Comprueba que la instancia este en estado `Available`.

Tambien puedes cargar RDS desde PowerShell con el script preparado:

```powershell
.\database\load-rds.ps1
```

El script usa el endpoint real, busca `mysql.exe`, pide la password de RDS de forma interactiva y ejecuta `02_schema.sql`, `03_seed.sql`, `05_demo_machine_media.sql` y `04_verify.sql`.

## 3. Crear Elastic Beanstalk

Antes de crear el entorno en AWS, el backend ya debe estar empaquetado.

Estado real del paquete de despliegue:

```text
backend/Procfile
backend/target/hazelgym-0.0.1-SNAPSHOT.jar
backend/deploy/hazelgym-0.0.1-SNAPSHOT.jar
backend/deploy/Procfile
backend/hazelgym-backend-eb.zip
```

El `Procfile` usado para Elastic Beanstalk es:

```text
web: java -jar hazelgym-0.0.1-SNAPSHOT.jar
```

El ZIP `backend/hazelgym-backend-eb.zip` contiene solo:

```text
hazelgym-0.0.1-SNAPSHOT.jar
Procfile
```

Crear una aplicacion para backend:

```text
Application name: hazelgym-backend
Environment name: hazelgym-backend-prod
Platform: Java 17
```

Variables de entorno necesarias en Elastic Beanstalk:

```text
MYSQL_URL=jdbc:mysql://HOST_DE_RDS:3306/hazelgym?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=utf8
MYSQL_USERNAME=...
MYSQL_PASSWORD=...
JWT_SECRET=una_clave_larga_de_32_o_mas_caracteres
JWT_EXPIRATION_MS=86400000
APP_CORS_ALLOWED_ORIGINS=http://localhost:5173,https://DOMINIO_FRONTEND
PORT=5000
```

## 4. Crear bucket de artefactos backend

Crear un bucket S3 privado para subir versiones de Elastic Beanstalk:

```text
hazelgym-backend-artifacts
```

Este bucket no es el de la web; solo guarda ZIPs del backend.

## 5. Crear frontend en AWS Amplify

Para la entrega se usara AWS Amplify Hosting para publicar la web React/Vite.

```text
AWS Console -> Amplify -> Deploy an app -> GitHub
```

Seleccionar:

```text
Repository: Ander13ab/PROYECTO_INTERMODULAR
Branch: main
App root: frontend
```

El repositorio incluye `amplify.yml` en la raiz. Ese archivo indica a Amplify que debe entrar en `frontend`, activar pnpm y ejecutar `pnpm build`.

Variable de entorno necesaria en Amplify:

```text
VITE_API_BASE_URL=http://hazelgym-backend.eu-west-1.elasticbeanstalk.com
```

Cuando tengas la URL publica de Amplify, por ejemplo:

```text
https://main.xxxxx.amplifyapp.com
```

actualiza `APP_CORS_ALLOWED_ORIGINS` en Elastic Beanstalk para incluirla.

## 6. Secretos en GitHub

En GitHub, entra en:

```text
Settings -> Secrets and variables -> Actions -> New repository secret
```

Secretos necesarios:

```text
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
AWS_REGION
AWS_BACKEND_ARTIFACT_BUCKET
AWS_EB_APPLICATION_NAME
AWS_EB_ENVIRONMENT_NAME
VITE_API_BASE_URL
```

Valores esperados:

```text
AWS_BACKEND_ARTIFACT_BUCKET=hazelgym-backend-artifacts
AWS_EB_APPLICATION_NAME=hazelgym-backend
AWS_EB_ENVIRONMENT_NAME=hazelgym-backend-prod
VITE_API_BASE_URL=http://hazelgym-backend.eu-west-1.elasticbeanstalk.com
```

Si se usa Amplify manual conectado a GitHub, no necesitas `AWS_S3_BUCKET` ni `AWS_CLOUDFRONT_DISTRIBUTION_ID` para la web.

## 7. Ejecutar workflows

Orden recomendado:

1. Ejecutar `Deploy backend to Elastic Beanstalk`.
2. Probar `https://URL_PUBLICA_BACKEND/api-docs`.
3. Conectar Amplify al repositorio y desplegar `frontend`.
4. Probar login web desde la URL de Amplify.
5. Ejecutar `Android APK` con `api_base_url=https://URL_PUBLICA_BACKEND/`.
6. Descargar artifact `hazelgym-debug-apk`.
7. Instalar APK en movil fisico.

## Validacion final

- Backend responde `/api-docs`.
- Web publica permite login con admin, cliente y entrenador.
- APK instalada en movil fisico puede hacer login.
- Cliente puede ver maquinas.
- Cliente puede registrar QR de sesion o maquina.

## Incidencia: raiz `/` devuelve 403 en Elastic Beanstalk

Durante la primera prueba del backend en Elastic Beanstalk, estas rutas funcionaban:

```text
http://hazelgym-backend.eu-west-1.elasticbeanstalk.com/swagger-ui.html
http://hazelgym-backend.eu-west-1.elasticbeanstalk.com/api-docs
```

pero la raiz devolvia `HTTP 403`:

```text
http://hazelgym-backend.eu-west-1.elasticbeanstalk.com/
```

La causa era la configuracion de Spring Security. Swagger y OpenAPI estaban en la lista de rutas publicas, pero `/` no. Como la regla final es `anyRequest().authenticated()`, Spring Security trataba la raiz como una ruta protegida.

Solucion aplicada:

- Se ha creado `RootController` con `GET /`.
- Se ha anadido `/` a la lista de rutas permitidas en `SecurityConfig`.
- La raiz devuelve ahora un JSON sencillo con estado de la API, ruta de Swagger y ruta OpenAPI.

Respuesta esperada:

```json
{
  "app": "Hazel Gym API",
  "status": "running",
  "docs": "/swagger-ui.html",
  "openapi": "/api-docs",
  "timestamp": "..."
}
```

Tras este cambio hay que volver a subir `backend/hazelgym-backend-eb.zip` como nueva version de Elastic Beanstalk.

## Referencias oficiales

- [AWS RDS Free Tier](https://aws.amazon.com/rds/free/)
- [Crear una instancia de base de datos en Amazon RDS](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_CreateDBInstance.html)
- [Crear y conectar una instancia MySQL en Amazon RDS](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/CHAP_GettingStarted.CreatingConnecting.MySQL.html)
- [Control de acceso con security groups en RDS](https://docs.aws.amazon.com/AmazonRDS/latest/gettingstartedguide/security-groups.html)
