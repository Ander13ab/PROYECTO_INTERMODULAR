# Introduccion a React y base de la app web

## Objetivo

Este documento resume, de forma sencilla, como se va a plantear la aplicacion web de Hazel Gym y que conceptos de React necesito entender para poder explicarla y defenderla.

La idea del proyecto web no es hacer una arquitectura compleja, sino una version clara, progresiva y facil de mantener.

## Que es React

React es una libreria de JavaScript para construir interfaces de usuario.

En vez de escribir una pagina completa y manipularla manualmente, en React la interfaz se divide en piezas pequenas llamadas componentes. Cada componente representa una parte de la pantalla, por ejemplo:

- un login
- una tarjeta de maquina
- una cabecera
- una tabla de usuarios
- un panel de cliente o entrenador

React se encarga de volver a pintar la pantalla cuando cambian los datos.

## Idea mental sencilla

Una forma facil de entender React es esta:

- los datos cambian
- React detecta ese cambio
- React actualiza la parte visual afectada

No hay que reconstruir toda la pagina manualmente. Solo se recalcula la interfaz a partir del estado actual.

## Conceptos clave

## 1. Componente

Un componente es una funcion que devuelve interfaz visual.

Ejemplo mental:

- `LoginPage` pinta la pantalla de login
- `AdminDashboard` pinta el panel de administrador
- `MachineCard` pinta una sola maquina

Los componentes permiten separar la aplicacion en bloques faciles de leer y reutilizar.

## 2. Props

Las props son datos que un componente recibe desde fuera.

Ejemplo:

- una tarjeta de maquina puede recibir `nombre`, `estado` y `descripcion`
- una cabecera puede recibir el `nombre del usuario`

Sirven para reutilizar un mismo componente con datos distintos.

## 3. Estado

El estado es la informacion que cambia dentro de la aplicacion y que afecta a la vista.

Ejemplos:

- el email escrito en el login
- el rol seleccionado
- la lista de maquinas cargadas desde el backend
- si el usuario ha iniciado sesion o no
- si una ventana desplegable esta abierta o cerrada

En React, cuando cambia el estado, se actualiza la interfaz automaticamente.

## 4. Evento

Un evento es una accion del usuario.

Ejemplos:

- hacer clic en `Entrar`
- escribir en un campo
- abrir un desplegable
- pulsar `Guardar`

Los eventos suelen modificar el estado.

## 5. Renderizado

Renderizar significa mostrar en pantalla el resultado del componente con los datos actuales.

Si cambia el estado, React vuelve a renderizar ese componente con el nuevo valor.

## Flujo sencillo de React

El flujo basico que seguiremos en la app web sera este:

1. el usuario interactua con la pantalla
2. el componente guarda o cambia un dato en el estado
3. si hace falta, se llama al backend
4. llega una respuesta
5. se actualiza el estado
6. React repinta la interfaz con la nueva informacion

## Como se conecta con el backend

La web de Hazel Gym se va a conectar al backend Spring Boot ya existente.

Los primeros endpoints importantes para la web son:

- `/api/auth/login`
- `/api/auth/me`
- `/api/machines`
- `/api/users`
- `/api/attendances`
- `/api/classes`
- `/api/routines`

La idea es que la web consuma la misma API que ya usa la app movil.

## Decisiones de simplicidad para este proyecto

Como el objetivo es que el codigo sea entendible, la app web se va a construir con unas reglas simples:

- empezar con pocos archivos clave
- usar componentes pequenos y con nombres claros
- evitar patrones avanzados si no aportan valor real
- usar `useState` para los estados locales mas sencillos
- mover la logica repetida a funciones o servicios claros
- no introducir complejidad extra al principio

## Arquitectura sencilla propuesta

La idea base para la app web es esta:

### 1. `main.tsx`

Es el punto de entrada. Arranca React y monta la aplicacion.

### 2. `App.tsx`

Es el contenedor principal.

Su funcion sera decidir que parte de la aplicacion se muestra:

- login
- panel de cliente
- panel de entrenador
- panel de administrador

### 3. `components/`

Aqui iran las piezas visuales reutilizables o pantallas concretas.

Ejemplos:

- login
- dashboards
- tarjetas
- tablas
- bloques estadisticos

### 4. `services/` o `api/`

Aqui se pondran las llamadas al backend, para no mezclar toda la logica de red dentro de la pantalla.

Ejemplo:

- `authService.ts`
- `machineService.ts`

### 5. `types/`

Aqui se definiran los tipos de datos principales, para que el codigo sea mas claro.

Ejemplos:

- `User`
- `Machine`
- `Attendance`

## Por que usar React en este proyecto

React encaja bien porque:

- separa muy bien la interfaz en componentes
- facilita reutilizar pantallas y bloques
- conecta bien con APIs REST
- permite crecer por fases
- tiene una comunidad grande y mucha documentacion

## Como explicarlo en la defensa del TFG

Una explicacion clara seria:

> La parte web se ha construido con React porque permite dividir la interfaz en componentes reutilizables y conectarlos de forma sencilla con el backend REST ya desarrollado en Spring Boot. Para mantener el proyecto entendible, se ha optado por una arquitectura simple, basada en componentes, estados locales controlados y servicios para centralizar las llamadas a la API.

Otra forma de resumirlo:

> React se ha usado como capa de interfaz. El backend sigue teniendo la logica de negocio y la persistencia, mientras que la web se encarga de mostrar informacion, recoger acciones del usuario y consumir los endpoints ya disponibles.

## Diferencia entre backend y frontend en este proyecto

- el backend valida, autentica, guarda datos y aplica reglas de negocio
- el frontend web muestra la informacion y permite interactuar con ella
- el frontend movil hace lo mismo, pero adaptado a Android nativo

## En que punto estamos

Actualmente:

- el backend ya esta funcional
- la app movil ya cubre gran parte de los flujos principales
- la web puede empezar ya sin depender de grandes cambios previos

Por tanto, el siguiente paso logico es construir la web de forma progresiva:

1. login real
2. lectura del usuario autenticado
3. panel base segun rol
4. primeras vistas conectadas al backend
5. mejora visual en paralelo con Figma

## Idea final importante

No hace falta dominar React entero para defender esta parte del proyecto.

Lo importante es entender bien estas ideas:

- React divide la interfaz en componentes
- cada componente puede recibir datos y tener estado
- los cambios de estado actualizan la vista
- la web consume la API REST del backend
- la arquitectura se ha mantenido deliberadamente simple para facilitar mantenimiento, aprendizaje y escalado progresivo
