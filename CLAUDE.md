# Estudio Procesal

App de estudio diario de derecho procesal colombiano. El dueño habla español: responde y redacta en español.

## Regla permanente del dueño

Todo cambio se publica en GitHub en el mismo momento, y el instalador de Android debe quedar siempre disponible para descargar desde el repositorio.

1. Después de cualquier cambio de contenido: subir `version` en `banco.js`, ejecutar `node actualizar.js` (debe terminar en "OK"), hacer commit y `git push origin main`.
2. Después de cualquier cambio en `index.html` o en la carpeta `android/`: subir `versionCode` y `versionName` en `android/AndroidManifest.xml`, recompilar el instalador y dejarlo como `EstudioProcesal.apk` en la raíz del repositorio, en el mismo commit.
3. Confirmar que GitHub Pages publicó el commit antes de dar el trabajo por terminado.

## Compilar el instalador

    FIRMA_CLAVE=... bash android/build.sh <build-tools> <android.jar> . <firma.jks> EstudioProcesal.apk

- Requiere las herramientas de Android (build-tools 34 y la plataforma 34), que se descargan de `dl.google.com`.
- El archivo de firma (`firma.jks`) y su clave **no están ni deben estar en el repositorio**: los tiene el dueño. Sin esa misma firma, una versión nueva no se instala encima de la anterior. Si no están disponibles, pídeselos; no generes una firma nueva sin avisarle.
- El código Java no debe usar clases internas ni anónimas: la herramienta `d8` de build-tools 34 falla con ellas bajo Java 21.

## Contenido

- `banco.js` es la fuente; `banco.json` se genera con `node actualizar.js` y es lo que la app instalada descarga para actualizarse.
- Formato y reglas del banco y de las lecciones: ver `README.md`.
- Las lecciones son explicaciones extensas, punto por punto, redactadas con palabras propias a partir de las normas. Los libros se citan solo como lectura para profundizar; no se copia su texto.
- Las lecciones nuevas se agregan al final del arreglo (los índices guardan el avance del usuario) y su posición en el programa se define con el campo `o`.
