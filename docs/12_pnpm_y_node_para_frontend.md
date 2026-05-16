# Migracion a pnpm y preparacion de Node para el frontend

## Objetivo

Este documento deja explicado el cambio del frontend de `npm` a `pnpm`, la configuracion de seguridad aplicada y como instalar Node.js en Windows para poder ejecutar React localmente.

## Cambio realizado

La carpeta `frontend` pasa a trabajar con `pnpm` como gestor principal de paquetes.

Se ha actualizado:

- `frontend/package.json`
- `frontend/pnpm-workspace.yaml`
- `frontend/README.md`

## Por que pasar a pnpm

El cambio no hace que las dependencias dejen automaticamente de venir del ecosistema npm, pero si permite aplicar controles de seguridad mas finos y un flujo mas ordenado.

Ademas:

- suele ser mas rapido
- usa mejor el espacio en disco
- gestiona bien workspaces
- permite endurecer la instalacion frente a scripts no revisados

## Configuracion de seguridad aplicada

En `pnpm-workspace.yaml` se ha dejado configurado:

### `minimumReleaseAge: 1440`

Solo permite instalar versiones publicadas hace al menos 24 horas.

Esto reduce el riesgo de instalar una version maliciosa recien subida.

### `minimumReleaseAgeStrict: true`

Fuerza que esa regla se cumpla de verdad.

Si una dependencia no cumple el tiempo minimo, la resolucion falla.

### `minimumReleaseAgeExclude`

Se han excluido temporalmente:

- `react`
- `react-dom`
- `vite`

La razon es no bloquear el trabajo diario con paquetes base muy habituales del proyecto.

### `blockExoticSubdeps: true`

Bloquea dependencias transitivas que intenten llegar desde fuentes raras como repositorios git o enlaces directos a tarballs, salvo dependencias directas controladas por el proyecto.

### `verifyDepsBeforeRun: error`

Si las dependencias no estan correctamente instaladas o actualizadas, `pnpm` no ejecutara la app silenciosamente: mostrara error para evitar ejecuciones inconsistentes.

### `strictDepBuilds: true`

No se permite ejecutar scripts de instalacion de dependencias sin revision previa.

### `allowBuilds`

Se permiten de forma explicita los builds de:

- `esbuild`
- `@tailwindcss/oxide`

Se han autorizado porque son dependencias tecnicas habituales para Vite y Tailwind y suelen necesitar ese paso para funcionar correctamente.

## Version de Node fijada

Se ha fijado esta referencia en el proyecto:

- `nodeVersion: 24.15.0`
- `engines.node: >=24.15.0`

Como referencia verificada el **15 de mayo de 2026**, la web oficial de Node.js muestra `v24.15.0` como version `LTS`.

## Lo que no se ha podido hacer desde este entorno

No he podido instalar Node.js en tu Windows ni descargar dependencias reales desde aqui porque este entorno de trabajo no tiene permisos para:

- instalar software global en tu sistema
- ejecutar descargas de red reales para el proyecto

Por eso he dejado la migracion preparada y documentada, pero el ultimo paso de instalacion lo tienes que lanzar en tu equipo.

## Instalacion recomendada en tu PC

### 1. Instalar Node.js

Descarga Node.js LTS desde la web oficial:

- [Node.js Download](https://nodejs.org/en/download)

En la fecha revisada, la opcion LTS visible es `v24.15.0`.

En Windows, lo mas sencillo es instalar el `.msi` oficial.

### 2. Comprobar que Node ha quedado instalado

En una terminal nueva de Visual Studio Code:

```powershell
node -v
npm -v
```

Si responde con versiones, Node ya esta listo.

### 3. Activar Corepack

Corepack viene con Node moderno y permite usar `pnpm` sin instalarlo globalmente de forma manual.

```powershell
corepack enable
corepack prepare pnpm@11.0.0 --activate
```

### 4. Entrar en la carpeta del frontend

```powershell
cd C:\Users\ander\Documents\2DAM\PROYECTO_INTERMODULAR\frontend
```

### 5. Instalar dependencias con pnpm

```powershell
pnpm install
```

Si `pnpm` detecta paquetes con scripts no revisados, puede pedirte que los apruebes o puede bloquear la instalacion por seguridad.

En ese caso, revisa primero lo que te marque y despues usa:

```powershell
pnpm approve-builds
```

## Arranque del frontend

Una vez instaladas las dependencias:

```powershell
pnpm dev
```

La web deberia quedar disponible en una URL parecida a:

```text
http://localhost:5173
```

## Backend necesario al mismo tiempo

Para que el login web funcione, el backend tiene que estar levantado porque la web llama a:

- `POST /api/auth/login`
- `GET /api/auth/me`

Por tanto, antes de probar la web, asegurate de que Spring Boot esta arrancado en:

```text
http://localhost:8080
```

## Como explicarlo en el TFG

Una forma sencilla de defender esta decision es:

> Se ha migrado el frontend web de npm a pnpm para mejorar el control sobre la instalacion de dependencias y endurecer el proceso frente a riesgos del supply chain. Ademas del cambio de gestor, se han configurado reglas concretas de seguridad, como el retraso minimo para instalar versiones recien publicadas, el bloqueo de dependencias transitivas desde fuentes no habituales y la aprobacion explicita de scripts de build.

## Siguiente paso

Una vez instalado Node y completado `pnpm install`, el siguiente paso logico es:

1. arrancar la web con `pnpm dev`
2. probar el login real
3. continuar con la conexion de datos reales en el panel administrador
