package co.estudioprocesal.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Sirve los archivos de la app desde el paquete (carpeta assets) bajo una dirección
 * interna, y manda los enlaces externos al navegador del teléfono.
 */
public class AppClient extends WebViewClient {
    static final String HOST = "estudio.local";
    private final Activity activity;

    AppClient(Activity activity) {
        this.activity = activity;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView v, WebResourceRequest r) {
        Uri u = r.getUrl();
        if (!HOST.equals(u.getHost())) return null;          // peticiones externas: red normal
        String path = u.getPath();
        if (path == null || path.equals("/")) path = "/index.html";
        try {
            InputStream in = activity.getAssets().open(path.substring(1));
            return new WebResourceResponse(mime(path), isText(path) ? "utf-8" : null, in);
        } catch (Exception e) {
            return new WebResourceResponse("text/plain", "utf-8", 404, "Not Found", null,
                    new ByteArrayInputStream(new byte[0]));
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest r) {
        if (HOST.equals(r.getUrl().getHost())) return false;
        try {                                                   // enlaces a fuentes: navegador del teléfono
            activity.startActivity(new Intent(Intent.ACTION_VIEW, r.getUrl()));
        } catch (Exception ignored) { }
        return true;
    }

    private static boolean isText(String p) {
        return p.endsWith(".html") || p.endsWith(".js") || p.endsWith(".json")
                || p.endsWith(".svg") || p.endsWith(".webmanifest") || p.endsWith(".css");
    }

    private static String mime(String p) {
        if (p.endsWith(".html")) return "text/html";
        if (p.endsWith(".js")) return "text/javascript";
        if (p.endsWith(".json")) return "application/json";
        if (p.endsWith(".svg")) return "image/svg+xml";
        if (p.endsWith(".png")) return "image/png";
        if (p.endsWith(".webmanifest")) return "application/manifest+json";
        if (p.endsWith(".css")) return "text/css";
        return "application/octet-stream";
    }
}
