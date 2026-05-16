# Frontend web: login real y shell por rol

## Objetivo de este bloque

El primer paso funcional de la aplicacion web ha sido dejar de usar una maqueta estatica y pasar a una base real conectada al backend.

En este punto ya se ha montado:

- login real contra el backend
- lectura del usuario autenticado
- gestion sencilla de sesion en navegador
- panel de escritorio diferente segun el rol

## Decision tecnica principal

Se ha buscado una estructura facil de entender para alguien que no viene de React.

Por eso, en vez de introducir desde el principio:

- rutas complejas
- estado global
- contextos grandes
- gestores avanzados de cache

se ha optado por una solucion simple:

- `App.tsx` controla la sesion
- `authService.ts` centraliza las llamadas de autenticacion
- los dashboards reciben los datos del usuario por props

## Flujo funcional

El flujo actual de la app web es:

1. el usuario abre la web
2. `App.tsx` revisa si hay token guardado en sesion
3. si hay token, consulta `/api/auth/me`
4. si el token es valido, carga el panel correspondiente
5. si no hay token o no es valido, muestra login
6. al iniciar sesion, se llama a `/api/auth/login`
7. se guarda el token
8. se vuelve a consultar `/api/auth/me`
9. se muestra el panel segun el rol real del usuario

## Endpoints usados

- `POST /api/auth/login`
- `GET /api/auth/me`

## Estructura anadida

### `src/app/services/authService.ts`

Contiene:

- login
- consulta del usuario autenticado
- guardar token
- leer token
- borrar token

### `src/app/types/auth.ts`

Define los tipos mas importantes de autenticacion:

- peticion de login
- respuesta de login
- usuario autenticado
- roles permitidos

### `src/app/App.tsx`

Se ha simplificado para que sea el centro de la sesion.

Sus responsabilidades ahora son:

- comprobar si hay sesion previa
- controlar estado de login
- guardar usuario autenticado
- mostrar login o dashboard

## Roles contemplados

La web ya interpreta estos roles:

- `ADMIN`
- `TRAINER`
- `CLIENT`

Cada uno abre su pantalla de escritorio correspondiente.

## Valor para el TFG

Este bloque es importante porque demuestra que:

- la web ya no es solo diseno
- existe integracion real con el backend
- el sistema reconoce usuarios reales
- el acceso cambia segun el rol autenticado

## Limitaciones actuales

Todavia no se ha conectado informacion real dentro de cada dashboard.

De momento:

- el login ya es real
- el panel segun rol ya es real
- el contenido interno de cada panel sigue siendo la siguiente fase

## Siguiente paso recomendado

Con esta base, lo mas logico es seguir con una de estas opciones:

1. conectar datos reales del panel administrador
2. construir un layout comun web reutilizable
3. enlazar Figma desktop con la implementacion real

La recomendacion es empezar por el panel de administrador, porque suele ser el mas demostrable en una presentacion y reutiliza muchos endpoints ya disponibles.
