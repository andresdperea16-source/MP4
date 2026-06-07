import javax.swing.*;
import java.awt.*;

/**
 * Componente visual para una carta ya invocada en el campo de duelo.
 *
 * Muestra:
 *   - Imagen oficial de la carta (desde ImagenCartaCache).
 *   - Indicador de posición: ATK (derecha) / DEF (rotada 90°).
 *   - Para trampas/magias: imagen con overlay semitransparente morado/verde.
 *   - ATK y DEF superpuestos en la parte inferior de la imagen.
 */
public class SlotCampoVisual extends JPanel {

    private static final Color COLOR_ATK   = new Color(224,  64,  64, 210);
    private static final Color COLOR_DEF   = new Color( 64, 140, 224, 210);
    private static final Color COLOR_MAGIA = new Color( 50, 160, 100, 180);
    private static final Color COLOR_TRAMPA= new Color(150,  60, 200, 180);
    private static final Color COLOR_DORADO= new Color(201, 168,  76);

    private final Carta carta;
    private ImageIcon icono;
    private final boolean esMonstruo;
    private final boolean enAtaque;   // solo relevante si esMonstruo

    /**
     * @param carta     La carta que ocupa este slot.
     * @param enAtaque  true = posición ataque, false = defensa (irrelevante para magia/trampa).
     */
    public SlotCampoVisual(Carta carta, boolean enAtaque) {
        this.carta      = carta;
        this.esMonstruo = (carta instanceof Monstruo);
        this.enAtaque   = enAtaque;

        int W = ImagenCartaCache.ANCHO_CAMPO + 4;
        int H = ImagenCartaCache.ALTO_CAMPO  + 4;
        setPreferredSize(new Dimension(W, H));
        setOpaque(false);
        setToolTipText(buildTooltip());

        // Pedir imagen al caché
        icono = ImagenCartaCache.getInstance()
                .obtenerParaCampo(carta.getNombre(), icon -> {
                    icono = icon;
                    repaint();
                });
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int W  = getWidth();
        int H  = getHeight();
        int iW = ImagenCartaCache.ANCHO_CAMPO;
        int iH = ImagenCartaCache.ALTO_CAMPO;
        int x  = (W - iW) / 2;
        int y  = (H - iH) / 2;

        if (esMonstruo && !enAtaque) {
            // Posición defensa: rotar 90° sobre el centro de la imagen
            g.translate(x + iW / 2.0, y + iH / 2.0);
            g.rotate(Math.PI / 2);
            dibujarImagen(g, -iH / 2, -iW / 2, iH, iW);
            g.rotate(-Math.PI / 2);
            g.translate(-(x + iW / 2.0), -(y + iH / 2.0));
        } else {
            dibujarImagen(g, x, y, iW, iH);
        }

        // Overlay de color para magia/trampa
        if (!esMonstruo) {
            Color overlay = (carta instanceof CartaMagica) ? COLOR_MAGIA : COLOR_TRAMPA;
            g.setColor(overlay);
            g.fillRoundRect(x, y, iW, iH, 6, 6);
        }

        // Borde dorado fino
        g.setColor(new Color(COLOR_DORADO.getRed(), COLOR_DORADO.getGreen(), COLOR_DORADO.getBlue(), 180));
        g.setStroke(new BasicStroke(1.2f));
        g.drawRoundRect(x, y, iW, iH, 6, 6);

        // Stats ATK/DEF en la parte inferior (solo monstruos)
        if (esMonstruo) {
            Monstruo m = (Monstruo) carta;
            String stat = enAtaque
                    ? "ATK " + m.getAtk()
                    : "DEF " + m.getDef();
            Color cStat = enAtaque ? COLOR_ATK : COLOR_DEF;

            g.setFont(new Font("SansSerif", Font.BOLD, 9));
            FontMetrics fm = g.getFontMetrics();
            int tw = fm.stringWidth(stat) + 8;
            int tx = x + (iW - tw) / 2;
            int ty = y + iH - 14;

            g.setColor(new Color(0, 0, 0, 160));
            g.fillRoundRect(tx, ty, tw, 13, 4, 4);
            g.setColor(cStat);
            g.drawString(stat, tx + 4, ty + 10);
        }

        // Etiqueta MAG / TRP centrada (para magia y trampa)
        if (!esMonstruo) {
            String label = (carta instanceof CartaMagica) ? "MAGIA" : "TRAMPA";
            g.setFont(new Font("SansSerif", Font.BOLD, 10));
            FontMetrics fm = g.getFontMetrics();
            g.setColor(Color.WHITE);
            g.drawString(label, x + (iW - fm.stringWidth(label)) / 2, y + iH / 2 + 4);
        }

        g.dispose();
    }

    private void dibujarImagen(Graphics2D g, int x, int y, int w, int h) {
        if (icono != null) {
            g.drawImage(icono.getImage(), x, y, w, h, null);
        } else {
            // Placeholder oscuro mientras carga
            g.setColor(new Color(20, 28, 42));
            g.fillRoundRect(x, y, w, h, 6, 6);
        }
    }

    private String buildTooltip() {
        StringBuilder sb = new StringBuilder("<html><b>" + carta.getNombre() + "</b>");
        if (carta instanceof Monstruo) {
            Monstruo m = (Monstruo) carta;
            sb.append("<br>ATK: ").append(m.getAtk())
              .append(" / DEF: ").append(m.getDef())
              .append("<br>Nivel: ").append(m.getNivel())
              .append("<br>Posición: ").append(enAtaque ? "Ataque" : "Defensa");
        }
        sb.append("</html>");
        return sb.toString();
    }
}
