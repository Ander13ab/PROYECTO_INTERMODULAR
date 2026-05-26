# Plan de cierre final - entrega 26 mayo 2026

## Objetivo

Cerrar Hazel Gym como proyecto demostrable antes del 26 de mayo de 2026.

El foco de esta fase no es anadir muchas funcionalidades nuevas, sino asegurar que lo ya construido se puede ensenar, instalar, desplegar y defender sin improvisar.

## Ruta critica

### 1. Validacion funcional completa

Estado: en progreso.

- Backend validado con smoke test.
- Web validada funcionalmente por roles.
- Build web corregido con `pnpm build`.
- TypeScript limpio para el codigo usado por la web real.
- Pendiente: ejecutar una validacion final conjunta backend + web + Android.

### 2. QR y asistencia

Estado: funcional, demo de maquina y sesion cerradas con smoke test.

Lo que ya existe:

- El administrador puede crear QR de entrada, maquina y sesion.
- El cliente puede registrar asistencia por ID manual.
- El cliente puede escanear QR con CameraX y ML Kit.
- El emulador tiene modo de prueba por ID de QR.
- El cliente puede escanear QR de maquina desde la pestana `Maquinas` y ver ficha de uso, seguridad y recurso/video.
- Los datos de demo incluyen un script para rellenar instrucciones y enlaces de recurso en maquinas existentes.
- El smoke test registra asistencia con un QR `CLASS_SESSION` y valida que el backend responde ese tipo.

Prioridad antes de entrega:

1. Intentar demo de QR de entrada al gimnasio si no pone en riesgo el cierre.
2. Mantener estable QR de maquina y QR de sesion durante el despliegue.
3. Validar de nuevo QR contra backend desplegado.

### 3. APK Android

Estado: preparado para generar y validar.

Objetivo minimo:

- Generar APK debug instalable para demo.
- Documentar como instalarla.
- Dejar claro que para movil fisico necesita apuntar a una URL accesible del backend desplegado.
- Guia creada en `docs/36_generacion_apk_android.md`.
- Script local creado en `mobile-android/scripts/build-debug-apk.ps1`.

Objetivo deseable:

- Generar APK release sin firma de Play Store, suficiente para defensa y pruebas externas.

### 4. Despliegue AWS

Estado: desplegado y en fase de validacion final.

Arquitectura final de entrega:

- Frontend web: AWS Amplify.
- Proxy HTTPS: Amazon API Gateway.
- Backend Spring Boot: AWS Elastic Beanstalk.
- Base de datos: Amazon RDS MySQL.
- Secretos: variables de entorno en AWS, no en Git.

Flujo final:

```text
Usuario web -> Amplify HTTPS -> /api-proxy -> API Gateway HTTPS -> Elastic Beanstalk HTTP -> RDS MySQL
Android APK -> API Gateway HTTPS -> Elastic Beanstalk HTTP -> RDS MySQL
```

Esta solucion evita el bloqueo de contenido mixto del navegador, porque la web de Amplify siempre consume una URL HTTPS.

### 5. CI/CD GitHub Actions

Estado: preparado.

Workflows necesarios:

- `ci.yml`: valida backend, web y Android.
- Amplify despliega la web automaticamente desde GitHub al detectar cambios en `main`.
- `deploy-backend.yml`: empaqueta backend y despliega a AWS.
- `android-apk.yml`: genera APK como artifact descargable.

### 6. Documentacion y defensa

Estado: en progreso.

Pendiente antes del 26:

- Guia de arranque local.
- Guia de demo por roles.
- Guia de despliegue AWS.
- Guia de generacion de APK.
- Resumen tecnico para defensa.
- Actualizacion final en Notion.

## Plan por dias

### 20 mayo

- Cerrar plan de entrega.
- Preparar workflows base de GitHub Actions.
- Preparar configuracion de frontend/backend para entornos local y nube.

### 21 mayo

- QR de maquina y QR de sesion cerrados; empezar despliegue AWS inicial.
- Generar primer APK debug.
- Validar Android contra backend local.

### 22 mayo

- Preparar despliegue AWS inicial.
- Crear RDS MySQL o definir conexion final.
- Subir backend y web por primera vez.

### 23 mayo

- Activar CI/CD con GitHub Actions.
- Conectar frontend web al backend desplegado.
- Preparar Android para apuntar al backend desplegado.

### 24 mayo

- Ronda completa de validacion: local, nube, web, Android y QR.
- Corregir errores de demo.

### 25 mayo

- Congelar funcionalidades.
- Preparar guion de defensa.
- Exportar documentacion y bitacora.
- Generar APK final.

### 26 mayo

- Solo comprobaciones finales.
- No introducir cambios grandes salvo bloqueo critico.

## Riesgos principales

- AWS puede consumir tiempo si se intenta una arquitectura demasiado compleja.
- La APK instalada en movil fisico no funcionara contra `10.0.2.2`; necesita backend desplegado o IP accesible.
- CI/CD depende de secretos de GitHub y credenciales AWS.
- QR de entrada es prioridad baja; no debe retrasar QR de maquina, QR de sesion, APK y despliegue.

## Definicion de terminado

El proyecto se considerara listo cuando se cumpla:

- Backend arrancable y validado.
- Web compilable y usable por los tres roles.
- Android instalable mediante APK.
- QR de maquina o sesion demostrable de extremo a extremo.
- Despliegue en AWS accesible publicamente mediante Amplify, API Gateway, Elastic Beanstalk y RDS.
- GitHub Actions ejecutando validaciones y generando APK cuando sea necesario.
- Documentacion final actualizada en `/docs` y Notion.
