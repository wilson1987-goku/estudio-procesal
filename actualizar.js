// Valida banco.js y regenera banco.json. Uso: node actualizar.js
const fs = require('fs');
const path = require('path');
global.window = {};
require(path.join(__dirname, 'banco.js'));
const B = window.BANCO;
const errores = [];
const areas = new Set(B.areas.map(a => a.id));
const ids = new Set();

if (!/^\d{4}-\d{2}-\d{2}(\.\d+)?$/.test(B.version)) errores.push('version debe tener formato AAAA-MM-DD (o AAAA-MM-DD.N para una segunda publicación el mismo día)');
for (const q of B.preguntas) {
  if (ids.has(q.id)) errores.push('id repetido: ' + q.id);
  ids.add(q.id);
  if (!areas.has(q.a)) errores.push(q.id + ': área desconocida ' + q.a);
  if (!Array.isArray(q.o) || q.o.length !== 4 || new Set(q.o).size !== 4) errores.push(q.id + ': necesita cuatro opciones distintas');
  if (!q.q || !q.e || !q.n) errores.push(q.id + ': falta pregunta, explicación o norma');
}
for (const l of B.lecciones) if (!areas.has(l.a) || !l.t || !Array.isArray(l.p) || l.p.length < 3 || l.p.some(x => typeof x !== 'string')) errores.push('lección inválida: ' + l.t);
for (const c of B.programa) for (const a of c.areas) if (!areas.has(a)) errores.push('programa: área desconocida ' + a);
for (const n of B.novedades) if (!n.f || !n.t || !n.d || !/^https:\/\//.test(n.u) || !areas.has(n.a)) errores.push('novedad inválida: ' + n.t);

if (errores.length) { console.error(errores.join('\n')); process.exit(1); }
fs.writeFileSync(path.join(__dirname, 'banco.json'), JSON.stringify(B));
console.log(`OK: versión ${B.version}, ${B.preguntas.length} preguntas, ${B.lecciones.length} lecciones, ${B.novedades.length} novedades.`);
