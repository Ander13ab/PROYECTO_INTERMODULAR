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

### Solucion recomendada para la entrega: proxy de Amplify

En Amplify se puede crear una regla de rewrite para que el frontend llame a su mismo dominio HTTPS y Amplify reenvie la peticion al backend HTTP.

En Amplify:

```text
App settings -> Rewrites and redirects -> Add rule
```

Regla:

```text
Source address: /api-proxy/<*>
Target address: http://hazelgym-backend.eu-west-1.elasticbeanstalk.com/<*>
Type: 200 (Rewrite)
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
http://hazelgym-backend.eu-west-1.elasticbeanstalk.com/api/auth/login
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
