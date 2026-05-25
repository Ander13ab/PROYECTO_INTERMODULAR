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

Configurar:

```text
VITE_API_BASE_URL=http://hazelgym-backend.eu-west-1.elasticbeanstalk.com
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

Si el frontend desplegado carga pero el login falla con error CORS, casi seguro falta la URL exacta de Amplify en esa variable.

## Validacion

- La web carga desde Amplify.
- Login funciona con admin, cliente y entrenador.
- Las pantallas cargan datos desde Elastic Beanstalk.
- No hay errores CORS en consola.
- El backend sigue respondiendo `/`, `/swagger-ui.html` y `/api-docs`.
