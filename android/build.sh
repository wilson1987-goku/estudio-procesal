#!/bin/bash
# Compila la app Android de Estudio Procesal sin Gradle.
# Uso: build.sh <carpeta build-tools> <android.jar> <carpeta web> <keystore> <apk de salida>
set -e
BT="$1"; JAR="$2"; WEB="$3"; KS="$4"; OUT="$5"
HERE="$(cd "$(dirname "$0")" && pwd)"
W="$HERE/build"
mkdir -p "$W/res" "$W/gen" "$W/classes" "$W/dex" "$HERE/assets" "$HERE/res/mipmap-xxxhdpi"

# Contenido web e icono
cp "$WEB/index.html" "$WEB/banco.js" "$WEB/icon.svg" "$WEB/icon-180.png" "$WEB/manifest.webmanifest" "$HERE/assets/"
cp "$WEB/icon-192.png" "$HERE/res/mipmap-xxxhdpi/ic_launcher.png"

# Recursos y manifiesto
"$BT/aapt2" compile --dir "$HERE/res" -o "$W/res/res.zip"
"$BT/aapt2" link -o "$W/unsigned.apk" -I "$JAR" --manifest "$HERE/AndroidManifest.xml" \
  -A "$HERE/assets" --java "$W/gen" "$W/res/res.zip"

# Código
javac --release 8 -nowarn -classpath "$JAR" -d "$W/classes" \
  "$HERE/src/co/estudioprocesal/app/MainActivity.java" "$HERE/src/co/estudioprocesal/app/AppClient.java" \
  "$W/gen/co/estudioprocesal/app/R.java"
"$BT/d8" --lib "$JAR" --min-api 24 --output "$W/dex" $(find "$W/classes" -name '*.class')

# Empaquetar, alinear y firmar
cp "$W/unsigned.apk" "$W/with-dex.apk"
(cd "$W/dex" && zip -q -j "$W/with-dex.apk" classes.dex)
"$BT/zipalign" -f -p 4 "$W/with-dex.apk" "$W/aligned.apk"
CLAVE="${FIRMA_CLAVE:?Define FIRMA_CLAVE con la clave del archivo de firma}"
"$BT/apksigner" sign --ks "$KS" --ks-pass "pass:$CLAVE" --key-pass "pass:$CLAVE" \
  --ks-key-alias estudio --out "$OUT" "$W/aligned.apk"
"$BT/apksigner" verify --verbose "$OUT" | head -6
