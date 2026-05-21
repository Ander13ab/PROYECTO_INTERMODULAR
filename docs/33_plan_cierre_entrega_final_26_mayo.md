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

Estado: funcional, demo de maquina cerrada.

Lo que ya existe:

- El administrador puede crear QR de entrada, maquina y sesion.
- El cliente puede registrar asistencia por ID manual.
- El cliente puede escanear QR con CameraX y ML Kit.
- El emulador tiene modo de prueba por ID de QR.
- El cliente puede escanear QR de maquina desde la pestana `Maquinas` y ver ficha de uso, seguridad y recurso/video.
- Los datos de demo incluyen un script para rellenar instrucciones y enlaces de recurso en maquinas existentes.

Prioridad antes de entrega:

1. Validar demo de QR de maquina en emulador con un QR real o ID manual.
2. Asegurar demo de QR de sesion.
3. Intentar demo de QR de entrada al gimnasio si no pone en riesgo el cierre.

### 3. APK Android

Estado: pendiente.

Objetivo minimo:

- Generar APK debug instalable para demo.
- Documentar como instalarla.
- Dejar claro que para movil fisico necesita apuntar a una URL accesible del backend desplegado.

Objetivo deseable:

- Generar APK release sin firma de Play Store, suficiente para defensa y pruebas externas.

### 4. Despliegue AWS

Estado: pendiente.

Arquitectura recomendada para esta entrega:

- Frontend web: S3 + CloudFront.
- Backend Spring Boot: Elastic Beanstalk o App Runner.
- Base de datos: RDS MySQL.
- Secretos: variables de entorno en AWS, no en Git.

Orden recomendado:

1. Preparar backend para leer todo por variables de entorno.
2. Preparar frontend para usar una URL de API configurable.
3. Crear workflows de GitHub Actions.
4. Desplegar primero manualmente una vez.
5. Activar despliegue automatico con GitHub Actions.

### 5. CI/CD GitHub Actions

Estado: pendiente.

Workflows necesarios:

- `ci.yml`: valida backend, web y Android.
- `deploy-frontend.yml`: compila web y despliega a S3.
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

- Validar QR de maquina ya cerrado y cerrar QR de sesion como flujo demostrable.
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
- Despliegue en AWS accesible publicamente.
- GitHub Actions ejecutando validaciones y al menos un despliegue.
- Documentacion final actualizada en `/docs` y Notion.
