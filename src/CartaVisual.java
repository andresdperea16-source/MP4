import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CartaVisual extends JPanel {

    // Paleta de tipos
    private static final Color COLOR_MONSTRUO = new Color(200, 130,  50);
    private static final Color COLOR_MAGIA    = new Color( 70, 180, 120);
    private static final Color COLOR_TRAMPA   = new Color(170,  70, 200);
    private static final Color COLOR_BORDE    = new Color( 55,  65,  85);
    private static final Color COLOR_DORADO   = new Color(201, 168,  76);

    private final Carta carta;
    private ImageIcon iconoActual;
    private boolean hovered = false;
    private Runnable onClic;

    public CartaVisual(Carta carta) {
        this.carta = carta;

        int W = ImagenCartaCache.ANCHO_MANO + 8;
        int H = ImagenCartaCache.ALTO_MANO  + 30;
        setPreferredSize(new Dimension(W, H));
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setToolTipText(buildTooltip());

        iconoActual = ImagenCartaCache.getInstance()
                .obtenerParaMano(carta.getNombre(), icon -> {
                    iconoActual = icon;
                    repaint();
                });

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
            @Override public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
            @Override public void mouseClicked(MouseEvent e) { if (onClic != null) onClic.run(); }
        });
    }

    public void setOnClic(Runnable onClic) { this.onClic = onClic; }

    public Carta getCarta() { return carta; }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int offsetY = hovered ? -4 : 0;
        int W = getWidth();
        int imgW = ImagenCartaCache.ANCHO_MANO;
        int imgH = ImagenCartaCache.ALTO_MANO;
        int imgX = (W - imgW) / 2;
        int imgY = 2 + offsetY;

        if (hovered) {
            g.setColor(new Color(0, 0, 0, 80));
            g.fillRoundRect(imgX + 3, imgY + 6, imgW, imgH, 6, 6);
        }

        // Imagen de la carta
        if (iconoActual != null) {
            g.drawImage(iconoActual.getImage(), imgX, imgY, imgW, imgH, null);
        }

        // Borde
        Color colorBorde = hovered ? COLOR_DORADO : COLOR_BORDE;
        g.setColor(colorBorde);
        g.setStroke(new BasicStroke(hovered ? 1.8f : 1f));
        g.drawRoundRect(imgX, imgY, imgW, imgH, 6, 6);

        // Etiqueta de tipo
        Color colorTipo = tipoColor();
        String etiqueta = tipoEtiqueta();
        g.setFont(new Font("SansSerif", Font.BOLD, 8));
        FontMetrics fm = g.getFontMetrics();
        int tw = fm.stringWidth(etiqueta) + 6;
        int tx = imgX + imgW - tw - 1;
        int ty = imgY + 1;
        g.setColor(new Color(colorTipo.getRed(), colorTipo.getGreen(), colorTipo.getBlue(), 200));
        g.fillRoundRect(tx, ty, tw, 11, 4, 4);
        g.setColor(Color.WHITE);
        g.drawString(etiqueta, tx + 3, ty + 9);

        // Nombre bajo la imagen
        g.setFont(new Font("SansSerif", Font.PLAIN, 8));
        g.setColor(new Color(200, 190, 170));
        String nombre = carta.getNombre();
        if (fm.stringWidth(nombre) > W - 4) nombre = nombre.substring(0, 8) + "…";
        g.setFont(new Font("SansSerif", Font.PLAIN, 8));
        fm = g.getFontMetrics();
        g.drawString(nombre, (W - fm.stringWidth(nombre)) / 2, imgY + imgH + 12 + offsetY);

        g.dispose();
    }

    // Helpers

    private Color tipoColor() {
        if (carta instanceof Monstruo)   return COLOR_MONSTRUO;
        if (carta instanceof CartaMagica) return COLOR_MAGIA;
        return COLOR_TRAMPA;
    }

    private String tipoEtiqueta() {
        if (carta instanceof Monstruo) return "★" + ((Monstruo) carta).getNivel();
        if (carta instanceof CartaMagica) return "MAG";
        return "TRP";
    }

    private String buildTooltip() {
        StringBuilder sb = new StringBuilder("<html><b>" + carta.getNombre() + "</b>");
        if (carta instanceof Monstruo) {
            Monstruo m = (Monstruo) carta;
            sb.append("<br>ATK: ").append(m.getAtk())
            .append(" / DEF: ").append(m.getDef())
            .append("<br>Nivel: ").append(m.getNivel());
        } else if (carta instanceof CartaMagica) {
            sb.append("<br><i>Carta Mágica</i>");
        } else {
            sb.append("<br><i>Carta Trampa</i>");
        }
        sb.append("</html>");
        return sb.toString();
    }
}
