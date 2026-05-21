# Flujo QR movil para demo de maquinas

## Objetivo

Cerrar un flujo visible y defendible para la app movil:

- el administrador crea y consulta QR
- la app muestra un QR real en pantalla
- el cliente puede escanearlo o introducir su ID manualmente
- el registro diferencia entre entrada, maquina y sesion

## Estado actual

### Administrador

- La pantalla `Codigos QR` muestra cada codigo creado.
- Cada tarjeta incluye:
  - ID del QR
  - tipo
  - destino del QR
  - contenido real del QR en formato `hazelgym://qr/{id}?type=...`
  - imagen QR renderizada en la propia app

### Cliente

- El lector QR acepta:
  - ID numerico directo
  - QR Hazel Gym con el formato `hazelgym://qr/{id}`
  - variantes que incluyan el identificador dentro del contenido
- La app carga tambien la lista de QR disponibles para poder mostrar mensajes mas claros.
- El mensaje de resultado cambia segun el tipo:
  - entrada general
  - uso de maquina
  - asistencia a sesion
- En la pestana `Maquinas`, el cliente puede escanear un QR de tipo `MACHINE`.
- Al detectar una maquina:
  - se abre una ficha resumida dentro de la app
  - se muestran descripcion, grupo muscular, nivel, instrucciones y seguridad
  - si la maquina tiene `URL de video o recurso`, se puede abrir desde la propia ficha

### Administrador

- El formulario de maquinas ya permite informar:
  - instrucciones de uso
  - nivel recomendado
  - advertencia de seguridad
  - URL de video o recurso
- Eso permite preparar una demo completa sin editar la base de datos a mano.

## Preparar datos de demo

Si ya tenias la base de datos creada antes de anadir los campos multimedia, ejecuta:

```sql
SOURCE C:/Users/ander/Documents/2DAM/PROYECTO_INTERMODULAR/database/05_demo_machine_media.sql;
```

Esto rellena las maquinas de ejemplo con instrucciones, nivel, advertencias y un enlace externo de recurso/video para que el boton de la ficha QR se pueda mostrar durante la demo.

## Prueba rapida en emulador

1. Iniciar sesion como administrador.
2. Crear un QR de maquina.
3. Abrir el detalle de `Codigos QR`.
4. Anotar el ID o mostrar el QR en pantalla.
5. Iniciar sesion como cliente.
6. Ir a la pestana `Maquinas`.
7. Escanear el QR de la maquina o escribir su ID manualmente.
8. Verificar que aparece la ficha de la maquina y, si existe, el boton para abrir el video o recurso.
9. Ir a la pestana `QR` cuando quieras probar registro de entrada o de sesion.

## Siguiente mejora natural

- Mostrar tambien en el historial del cliente una descripcion mas rica del destino del QR, no solo el tipo tecnico.
- Si queda tiempo, separar visualmente los QR de entrada, maquina y sesion en la vista admin para que la demo sea aun mas clara.
