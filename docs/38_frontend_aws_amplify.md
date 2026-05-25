# Frontend web en AWS Amplify

## Objetivo

Publicar la aplicacion web React/Vite de Hazel Gym para que consuma el backend desplegado en Elastic Beanstalk.

## Estado previo

- Backend AWS validado con smoke test.
- Backend publico:

```text
http://hazelgym-backend.eu-west-1.elasticbeanstalk.com
```

- OpenAPI disponible:

```text
http://hazelgym-backend.eu-west-1.elasticbeanstalk.com/api-docs
```

## Variable correcta del frontend

El frontend ya centraliza la URL del backend en:

```text
frontend/src/app/services/apiConfig.ts
```

La variable que lee Vite es:

```text
VITE_API_BASE_URL
```

No usar `VITE_API_URL`, porque el codigo actual no la lee.

## Prueba local contra AWS

Archivo local:

```text
frontend/.env.local
```

Contenido:

```text
VITE_API_BASE_URL=http://hazelgym-backend.eu-west-1.elasticbeanstalk.com
```

Este archivo esta ignorado por Git para no subir configuraciones locales.

Comandos:

```powershell
cd C:\Users\ander\Documents\2DAM\PROYECTO_INTERMODULAR\frontend
pnpm install
pnpm dev
```

Validar en:

```text
http://localhost:5173
```

## Despliegue Amplify

En AWS:

```text
AWS Console -> Amplify -> Deploy an app -> GitHub
```

Seleccionar:

```text
Repository: Ander13ab/PROYECTO_INTERMODULAR
Branch: main
App root: frontend
```

El repositorio tiene un `amplify.yml` en la raiz con:

- Node 24.15.0.
- pnpm 11.0.0 mediante Corepack.
- `pnpm install --frozen-lockfile`.
- `pnpm build`.
- artefactos desde `frontend/dist`.

## Variable de entorno en Amplify

Si se llama directamente al backend desde Amplify:

```text
VITE_API_BASE_URL=http://hazelgym-backend.eu-west-1.elasticbeanstalk.com
```

Esta opcion puede funcionar en desarrollo local, pero en el despliegue publico puede fallar por `Mixed Content`, porque Amplify usa HTTPS y el backend actual usa HTTP.

Para el despliegue final en Amplify se recomienda configurar:

```text
VITE_API_BASE_URL=/api-proxy
```

## Problema HTTPS frontend + HTTP backend

Amplify publica la web con HTTPS:

```text
https://main.d1mithns8dqv1b.amplifyapp.com
```

Elastic Beanstalk esta sirviendo el backend por HTTP:

```text
http://hazelgym-backend.eu-west-1.elasticbeanstalk.com
```

Si el navegador carga una web HTTPS y el JavaScript intenta llamar directamente a un backend HTTP, puede bloquear la peticion por `Mixed Content`.

### Solucion aplicada para la entrega: Amplify + API Gateway

La primera idea fue crear una regla de rewrite en Amplify para que el frontend llamara a su mismo dominio HTTPS y Amplify reenviara la peticion al backend HTTP.

Esa opcion fallo porque Amplify no permite usar destinos `http://` en custom rewrites. AWS exige que el destino del rewrite sea `https://`.

Por eso se anadio una capa intermedia con API Gateway:

```text
Amplify HTTPS -> API Gateway HTTPS -> Elastic Beanstalk HTTP -> RDS MySQL
```

URLs reales usadas:

```text
Frontend Amplify:
https://main.d1mithns8dqv1b.amplifyapp.com

API Gateway:
https://k7edn14r3k.execute-api.eu-west-1.amazonaws.com

Backend Elastic Beanstalk:
http://hazelgym-backend.eu-west-1.elasticbeanstalk.com
```

## Configuracion de API Gateway

Se creo una HTTP API en API Gateway para actuar como proxy HTTPS hacia Elastic Beanstalk.

Configuracion importante:

```text
Route:
ANY /{proxy+}

Integration:
ANY http://hazelgym-backend.eu-west-1.elasticbeanstalk.com/{proxy}

Stage:
$default con auto-deploy activado
```

El punto clave fue anadir `/{proxy}` al final de la ruta de integracion. Sin ese fragmento, API Gateway recibia correctamente la peticion, pero no reenviaba bien la ruta completa al backend. El sintoma en el login fue:

```text
Request method 'POST' is not supported
```

Tras corregir la integracion a:

```text
http://hazelgym-backend.eu-west-1.elasticbeanstalk.com/{proxy}
```

las peticiones `POST` a `/api/auth/login` empezaron a llegar correctamente al backend.

Prueba de API Gateway:

```text
https://k7edn14r3k.execute-api.eu-west-1.amazonaws.com/api-docs
```

## Rewrite final en Amplify

En Amplify se configuro el rewrite para que el frontend use `/api-proxy` y Amplify lo reenvie a API Gateway, no directamente a Elastic Beanstalk.

En Amplify:

```text
App settings -> Rewrites and redirects -> Add rule
```

Reglas:

```text
Source address: /api-proxy/<*>
Target address: https://k7edn14r3k.execute-api.eu-west-1.amazonaws.com/<*>
Type: 200 (Rewrite)

Source address: /<*>
Target address: /index.html
Type: 404-200 (Rewrite)
```

Despues cambia la variable de entorno de Amplify:

```text
VITE_API_BASE_URL=/api-proxy
```

URL actual de Amplify:

```text
https://main.d1mithns8dqv1b.amplifyapp.com
```

Con esto, cuando el frontend haga:

```text
/api-proxy/api/auth/login
```

Amplify lo reenviara internamente a:

```text
https://k7edn14r3k.execute-api.eu-west-1.amazonaws.com/api/auth/login
```

Ventajas:

- El navegador solo ve HTTPS contra Amplify.
- Evita el bloqueo por Mixed Content.
- No requiere dominio propio ni certificado ACM.
- Sirve para la demo y defensa del TFG.

Limitacion:

- La API real sigue estando en HTTP por detras. Para una produccion real, lo correcto seria dominio propio + HTTPS en Elastic Beanstalk.

### Solucion definitiva de produccion

Para una solucion completa:

1. Comprar o usar un dominio propio.
2. Crear un certificado en AWS Certificate Manager.
3. Configurar un listener HTTPS 443 en el Load Balancer de Elastic Beanstalk.
4. Apuntar un subdominio, por ejemplo `api.hazelgym.com`, al backend.
5. Usar en el frontend:

```text
VITE_API_BASE_URL=https://api.hazelgym.com
```

## CORS posterior

Cuando Amplify termine, dara una URL parecida a:

```text
https://main.xxxxx.amplifyapp.com
```

Esa URL debe anadirse en Elastic Beanstalk:

```text
APP_CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:3000,http://localhost:8081,https://main.xxxxx.amplifyapp.com
```

Si usas el proxy de Amplify con `VITE_API_BASE_URL=/api-proxy`, el navegador llama al mismo dominio de Amplify y CORS deja de ser el problema principal. Aun asi, mantener la URL de Amplify en `APP_CORS_ALLOWED_ORIGINS` es correcto por claridad y por si haces llamadas directas al backend durante pruebas.

## Validacion

- La web carga desde Amplify.
- Login funciona con admin, cliente y entrenador.
- Las pantallas cargan datos desde Elastic Beanstalk.
- No hay errores CORS en consola.
- El backend sigue respondiendo `/`, `/swagger-ui.html` y `/api-docs`.
