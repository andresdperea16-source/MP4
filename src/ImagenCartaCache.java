import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ImagenCartaCache {

    // Dimensiones estándar para cartas en mano y en campo
    public static final int ANCHO_MANO  = 60;
    public static final int ALTO_MANO   = 88;
    public static final int ANCHO_CAMPO = 72;
    public static final int ALTO_CAMPO  = 105;

    private static ImagenCartaCache instancia;
    public static synchronized ImagenCartaCache getInstance() {
        if (instancia == null) instancia = new ImagenCartaCache();
        return instancia;
    }

    private final Map<String, ImageIcon> cacheMano  = new ConcurrentHashMap<>();
    private final Map<String, ImageIcon> cacheCampo = new ConcurrentHashMap<>();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private final ImageIcon placeholderMano;
    private final ImageIcon placeholderCampo;

    private ImagenCartaCache() {
        placeholderMano  = crearPlaceholder(ANCHO_MANO,  ALTO_MANO,  "?");
        placeholderCampo = crearPlaceholder(ANCHO_CAMPO, ALTO_CAMPO, "?");
    }

    public ImageIcon obtenerParaMano(String nombreCarta, Consumer<ImageIcon> onLista) {
        return obtener(nombreCarta, ANCHO_MANO, ALTO_MANO, cacheMano, onLista);
    }

    public ImageIcon obtenerParaCampo(String nombreCarta, Consumer<ImageIcon> onLista) {
        return obtener(nombreCarta, ANCHO_CAMPO, ALTO_CAMPO, cacheCampo, onLista);
    }

    private ImageIcon obtener(String nombre, int w, int h,
        Map<String, ImageIcon> cache,
        Consumer<ImageIcon> onLista) {
        if (cache.containsKey(nombre)) return cache.get(nombre);

        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() {
                try {
                    String urlImagen = consultarUrlImagen(nombre);
                    if (urlImagen == null) return null;
                    BufferedImage img = ImageIO.read(new URL(urlImagen));
                    Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaled);
                } catch (IOException e) {
                    return null;
                }
            }

            @Override
            protected void done() { 
                try {
                    ImageIcon icon = get();
                    if (icon == null) icon = crearPlaceholder(w, h, nombre.substring(0, Math.min(3, nombre.length())));
                    cache.put(nombre, icon);
                    if (onLista != null) onLista.accept(icon);
                } catch (Exception ignored) {}
            }
        }.execute();

        return (w == ANCHO_MANO) ? placeholderMano : placeholderCampo;
    }

    private String consultarUrlImagen(String nombre) {
        try {
            String encoded = java.net.URLEncoder.encode(nombre, "UTF-8");
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create("https://db.ygoprodeck.com/api/v7/cardinfo.php?name=" + encoded))
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200) return null;

            String body = resp.body();
            String key = "\"image_url\":\"";
            int idx = body.indexOf(key);
            if (idx == -1) return null;
            int start = idx + key.length();
            int end   = body.indexOf('"', start);
            return (end > start) ? body.substring(start, end) : null;

        } catch (Exception e) {
            return null;
        }
    }

    private ImageIcon crearPlaceholder(int w, int h, String texto) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo con gradiente oscuro
        g.setPaint(new GradientPaint(0, 0, new Color(30, 40, 60), 0, h, new Color(10, 15, 25)));
        g.fillRoundRect(0, 0, w, h, 8, 8);

        // Borde dorado
        g.setColor(new Color(201, 168, 76, 180));
        g.setStroke(new BasicStroke(1.5f));
        g.drawRoundRect(1, 1, w - 2, h - 2, 8, 8);

        // Texto
        g.setColor(new Color(201, 168, 76, 200));
        g.setFont(new Font("Serif", Font.BOLD, Math.max(8, w / 5)));
        FontMetrics fm = g.getFontMetrics();
        String display = texto.length() > 6 ? texto.substring(0, 6) : texto;
        g.drawString(display,
                (w - fm.stringWidth(display)) / 2,
                (h + fm.getAscent()) / 2 - 2);

        g.dispose();
        return new ImageIcon(img);
    }
}