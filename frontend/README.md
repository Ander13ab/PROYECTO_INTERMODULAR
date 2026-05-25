# Hazel Gym Web

## Objetivo

Este frontend sera la version web de Hazel Gym conectada al backend Spring Boot del proyecto.

La idea de trabajo es mantener una estructura sencilla para que el codigo sea facil de entender, mantener y defender en el TFG.

## Base tecnica

- `Vite` para arrancar el proyecto web
- `React` para construir la interfaz
- `TypeScript` para que los datos y componentes queden mas claros
- `pnpm` como gestor de paquetes principal

## Estructura simple que vamos a seguir

- `src/main.tsx`
  Punto de entrada de React.
- `src/app/App.tsx`
  Contenedor principal de la aplicacion.
- `src/app/components`
  Pantallas y bloques visuales reutilizables.
- futuras carpetas `services` o `api`
  Para centralizar llamadas al backend.

## Filosofia del frontend

En esta parte del proyecto no buscamos una arquitectura rebuscada.

Vamos a priorizar:

- componentes pequenos
- nombres claros
- poco acoplamiento
- estados faciles de seguir
- conexion progresiva con el backend

## Orden recomendado de implementacion

1. Login web real
2. Lectura del usuario autenticado
3. Panel base segun rol
4. Dashboard admin conectado
5. Dashboard entrenador conectado
6. Dashboard cliente conectado

## Ejecucion local

1. Instalar dependencias
2. Tener el backend arrancado en `http://localhost:8080`
3. Arrancar el entorno web

En esta carpeta:

- `pnpm install`
- `pnpm dev`
- `pnpm build`

Por defecto la web usa el backend local en `http://localhost:8080`.

Para compilar o ejecutar contra el backend desplegado:

```powershell
$env:VITE_API_BASE_URL="http://hazelgym-backend.eu-west-1.elasticbeanstalk.com"
pnpm build
```

Para desarrollo local contra AWS, crea `frontend/.env.local`:

```text
VITE_API_BASE_URL=http://hazelgym-backend.eu-west-1.elasticbeanstalk.com
```

Ese archivo no se sube a Git. La variable que debes configurar tambien en AWS Amplify es `VITE_API_BASE_URL`.

## Despliegue en AWS Amplify

El repositorio incluye `amplify.yml` en la raiz para que Amplify sepa que la app web vive en `frontend/`.

Configuracion esperada:

- App root: `frontend`
- Build command: `pnpm build`
- Output directory: `dist`
- Variable de entorno recomendada en Amplify: `VITE_API_BASE_URL=/api-proxy`

Cuando Amplify genere la URL publica, hay que anadirla en Elastic Beanstalk dentro de `APP_CORS_ALLOWED_ORIGINS`.

### Evitar bloqueo HTTPS/HTTP en navegador

Amplify sirve la web por HTTPS, pero Elastic Beanstalk esta sirviendo la API por HTTP. Si el navegador intenta llamar directamente desde:

```text
https://main.d1mithns8dqv1b.amplifyapp.com
```

a:

```text
http://hazelgym-backend.eu-west-1.elasticbeanstalk.com
```

puede bloquear la llamada por `Mixed Content`.

Para la entrega se usa API Gateway como proxy HTTPS intermedio. La arquitectura queda:

```text
Amplify HTTPS -> API Gateway HTTPS -> Elastic Beanstalk HTTP -> RDS MySQL
```

La regla directa de Amplify hacia Elastic Beanstalk HTTP no sirve, porque Amplify no acepta destinos `http://` en custom rewrites.

API Gateway esta configurado asi:

```text
Route: ANY /{proxy+}
Integration: ANY http://hazelgym-backend.eu-west-1.elasticbeanstalk.com/{proxy}
Stage: $default
```

El `/{proxy}` final de la integracion es necesario para que rutas como `/api/auth/login` lleguen completas al backend.

Rewrite de Amplify:

```text
Source address: /api-proxy/<*>
Target address: https://k7edn14r3k.execute-api.eu-west-1.amazonaws.com/<*>
Type: 200 (Rewrite)
```

Con esa regla, el frontend desplegado debe usar:

```text
VITE_API_BASE_URL=/api-proxy
```

Asi el navegador llama al mismo dominio HTTPS de Amplify y Amplify reenvia la peticion al backend.

## Nota sobre compilacion

La compilacion de produccion se ejecuta con un script propio (`scripts/build.mjs`) que llama a Vite de forma programatica.

Se deja asi para evitar bloqueos de resolucion del archivo de configuracion que pueden aparecer en Windows al lanzar el build estandar de Vite.

## Documentacion relacionada

- [Introduccion a React y base web](../docs/10_introduccion_react_y_base_web.md)
- [Arranque de la parte web desde Figma](../docs/09_frontend_web_figma_y_arranque.md)
- [Login real y shell por rol](../docs/11_frontend_web_login_y_shell.md)
- [Migracion a pnpm y preparacion de Node](../docs/12_pnpm_y_node_para_frontend.md)
- [Login web a pantalla completa y CORS local](../docs/15_frontend_web_login_fullscreen_y_cors_local.md)
- [Cliente web conectado y logica de QR por plataforma](../docs/16_frontend_web_cliente_conectado_y_logica_qr.md)
- [Navegacion real por roles en la web](../docs/17_frontend_web_navegacion_real_por_roles.md)
- [Gestion real de maquinas y titulo de la app](../docs/18_frontend_web_admin_maquinas_y_titulo_app.md)
- [Fullscreen y responsive en los paneles web](../docs/19_frontend_web_fullscreen_y_responsive.md)
- [Pulido visual respecto a Figma y mapa interno web](../docs/20_frontend_web_pulido_figma_y_mapa_interno.md)
- [Usuarios admin en web y su pantalla en Figma](../docs/21_frontend_web_admin_usuarios_y_figma.md)
- [Clases admin en web y su pantalla en Figma](../docs/22_frontend_web_admin_clases_y_figma.md)
- [Cuotas admin en web y su pantalla en Figma](../docs/23_frontend_web_admin_cuotas_y_figma.md)
- [QR admin en web y su pantalla en Figma](../docs/24_frontend_web_admin_qr_y_figma.md)
- [Clientes del entrenador en web y su pantalla en Figma](../docs/25_frontend_web_trainer_clientes_y_figma.md)
- [Rutinas del entrenador en web y su pantalla en Figma](../docs/26_frontend_web_trainer_rutinas_y_figma.md)
- [Sesiones del entrenador en web y su pantalla en Figma](../docs/27_frontend_web_trainer_sesiones_y_figma.md)
- [Asistencia del cliente en web y su pantalla en Figma](../docs/28_frontend_web_cliente_asistencia_y_figma.md)
- [Rutinas del cliente en web y su pantalla en Figma](../docs/29_frontend_web_cliente_rutinas_y_figma.md)
- [Perfil del cliente en web y su pantalla en Figma](../docs/30_frontend_web_cliente_perfil_y_figma.md)
- [Pulido global entre admin, entrenador y cliente](../docs/31_frontend_web_pulido_global_entre_roles.md)
