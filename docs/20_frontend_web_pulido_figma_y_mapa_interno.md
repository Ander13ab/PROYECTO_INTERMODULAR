# Frontend web: pulido visual Figma y mapa interno

## Objetivo

En esta iteracion se ha dado un pase fino al estilo visual de la aplicacion web para acercarla mas al lenguaje de Figma sin complicar la estructura de React.

Ademas, se ha dejado documentado en Figma el mapa actual de navegacion interna de la web para que el diseño y el codigo no evolucionen por caminos distintos.

## Cambios visuales aplicados

### Login

- tarjeta de acceso con mas presencia visual
- mejor equilibrio entre la zona editorial izquierda y el formulario
- detalles de sombra y jerarquia tipografica mas cercanos al prototipo

### Paneles por rol

- fondos con degradados suaves segun el rol
- sidebar con mas identidad visual y mejor lectura del estado activo
- tarjetas con sombras ligeras para separar niveles sin que parezcan modales flotantes
- cabeceras con etiquetas superiores mas limpias y faciles de leer
- misma linea visual entre `admin`, `entrenador` y `cliente`

## Archivos modificados

- `frontend/src/app/components/DesktopLoginScreen.tsx`
- `frontend/src/app/components/DesktopAdminDashboard.tsx`
- `frontend/src/app/components/DesktopTrainerDashboard.tsx`
- `frontend/src/app/components/DesktopClientDashboard.tsx`

## Documentacion en Figma

Dentro del archivo `Páginas Hazel Gym` se ha actualizado la parte web con una pieza de apoyo en la pagina:

- `WEB ESCRITORIO`

Y dentro de ella se ha creado la seccion:

- `MAPA WEB INTERNA`

Esta seccion no sustituye a las pantallas finales, pero deja trazado:

- que secciones existen hoy por rol
- como se reparte la navegacion interna
- que partes ya son reales en React y cuales quedan listas para ampliacion

## Decision tomada

En esta iteracion no se han añadido nuevas pantallas internas completas en Figma, porque el objetivo principal era pulir el lenguaje visual y documentar la navegacion actual. Si en la siguiente fase se crean secciones web nuevas con entidad propia, se deberan añadir tambien como pantallas dentro de Figma.

## Siguiente paso recomendado

Con esta base ya tiene mucho sentido seguir por uno de estos caminos:

1. convertir secciones internas web en pantallas de gestion mas completas
2. seguir refinando la fidelidad visual del dashboard respecto a Figma
3. ampliar el archivo de Figma con las pantallas internas que vayan dejando de ser simples listados
