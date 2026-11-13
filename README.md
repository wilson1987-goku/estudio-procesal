# Estudio Procesal

App de estudio diario de derecho procesal colombiano: lección del día, prueba diaria con repaso espaciado, programa por cursos y novedades normativas.

## Archivos

- `index.html`: la aplicación.
- `banco.js`: el contenido (áreas, preguntas, lecciones, programa, novedades y fuentes). Es el archivo que se edita.
- `banco.json`: copia de `banco.js` en formato JSON. La app instalada lo consulta para recibir contenido nuevo sin reinstalar. Se regenera con `node actualizar.js`.
- `actualizar.js`: valida el banco y regenera `banco.json`.

## Reglas del banco

- En cada pregunta, la primera opción es la correcta; la app las baraja al mostrarlas.
- Cada pregunta lleva cuatro opciones distintas, una explicación breve y la norma o sentencia que la sustenta.
- Al cambiar el contenido hay que subir la fecha de `version` (formato AAAA-MM-DD) o la app no lo tomará como nuevo.
- El contenido se redacta a partir de las normas y decisiones oficiales, con palabras propias.
