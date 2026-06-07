import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class VentanaDuelo extends JFrame {

    private static final Color C_BG_DARK   = new Color( 10,  12,  16);
    private static final Color C_BG_PANEL  = new Color( 18,  22,  30);
    private static final Color C_BG_CAMPO  = new Color( 14,  16,  24);
    private static final Color C_DORADO    = new Color(201, 168,  76);
    private static final Color C_GRIS      = new Color(122, 112,  96);
    private static final Color C_VERDE_LP  = new Color( 77, 232, 122);
    private static final Color C_LILA      = new Color(180, 140, 220);
    private static final Color C_TEXTO     = new Color(232, 224, 204);

    private JLabel lblLPJ1, lblLPJ2;
    private JLabel lblMazo1, lblMazo2;
    private JLabel lblMano1, lblMano2;
    private JLabel lblMonstruos1, lblMonstruos2;
    private JLabel lblTrampa1, lblTrampa2;
    private JLabel lblTurno, lblLog;

    private JButton btnJugarCarta;
    private JButton btnAtacar;
    private JButton btnActivarTrampa;
    private JButton btnTerminarTurno;

    private JPanel panelManoJ1;
    private JPanel panelManoJ2;

    private JPanel panelCampoMonstruosJ1;
    private JPanel panelCampoMonstruosJ2;
    private JPanel panelCampoMagiaJ1;
    private JPanel panelCampoMagiaJ2;

    private final String nombreJ1;
    private final String nombreJ2;

    public VentanaDuelo(String nombreJ1, String nombreJ2) {
        this.nombreJ1 = nombreJ1;
        this.nombreJ2 = nombreJ2;

        setTitle("Yu-Gi-Oh! - Campo de Duelo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(C_BG_DARK);

        add(crearHeader(),  BorderLayout.NORTH);
        add(crearCentro(),  BorderLayout.CENTER);
        add(crearFooter(),  BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(1100, 720));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public JButton getBtnJugarCarta()    { return btnJugarCarta; }
    public JButton getBtnAtacar()        { return btnAtacar; }
    public JButton getBtnActivarTrampa() { return btnActivarTrampa; }
    public JButton getBtnTerminarTurno() { return btnTerminarTurno; }

    // El controlador llama este metodo pasando todos los datos ya listos
    public void actualizarInterfaz(
            int lpJ1, int lpJ2,
            int mazoJ1, int mazoJ2,
            int manoJ1, int manoJ2,
            int monstruosJ1, int monstruosJ2,
            int trampasJ1, int trampasJ2,
            List<Carta> cartasManoJ1, List<Carta> cartasManoJ2,
            List<Monstruo> campMonstruosJ1, List<Monstruo> campMonstruosJ2,
            List<CartaMagica> campMagiasJ1, List<CartaMagica> campMagiasJ2,
            List<CartaTrampa> campTrampasJ1, List<CartaTrampa> campTrampasJ2) {

        lblLPJ1.setText(String.valueOf(lpJ1));
        lblLPJ2.setText(String.valueOf(lpJ2));

        lblMazo1.setText(mazoJ1 + " cartas");
        lblMazo2.setText(mazoJ2 + " cartas");
        lblMano1.setText(manoJ1 + " cartas");
        lblMano2.setText(manoJ2 + " cartas");

        lblMonstruos1.setText(monstruosJ1 + " monstruos");
        lblMonstruos2.setText(monstruosJ2 + " monstruos");
        lblTrampa1.setText(trampasJ1 + " trampas");
        lblTrampa2.setText(trampasJ2 + " trampas");

        reconstruirMano(panelManoJ1, cartasManoJ1);
        reconstruirMano(panelManoJ2, cartasManoJ2);

        reconstruirCampoMonstruos(panelCampoMonstruosJ1, campMonstruosJ1);
        reconstruirCampoMonstruos(panelCampoMonstruosJ2, campMonstruosJ2);

        reconstruirCampoMagia(panelCampoMagiaJ1, campMagiasJ1, campTrampasJ1);
        reconstruirCampoMagia(panelCampoMagiaJ2, campMagiasJ2, campTrampasJ2);

        revalidate();
        repaint();
    }

    public void setLog(String mensaje)  { lblLog.setText(mensaje); }
    public void setTurno(int numero)    { lblTurno.setText("TURNO " + numero + "  "); }

    public void deshabilitarAcciones() {
        btnJugarCarta   .setEnabled(false);
        btnAtacar       .setEnabled(false);
        btnActivarTrampa.setEnabled(false);
        btnTerminarTurno.setEnabled(false);
    }

    private JPanel crearHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(Color.BLACK);
        h.setBorder(new EmptyBorder(4, 8, 4, 8));

        JLabel titulo = new JLabel("  \u2666 YU-GI-OH!  CAMPO DE DUELO \u2666");
        titulo.setForeground(C_DORADO);
        titulo.setFont(new Font("Serif", Font.BOLD, 17));
        h.add(titulo, BorderLayout.WEST);

        lblTurno = new JLabel("TURNO 1  ");
        lblTurno.setForeground(C_DORADO);
        lblTurno.setFont(new Font("Serif", Font.BOLD, 14));
        h.add(lblTurno, BorderLayout.EAST);
        return h;
    }

    private JPanel crearCentro() {
        JPanel centro = new JPanel(new BorderLayout(6, 6));
        centro.setBackground(C_BG_DARK);
        centro.setBorder(new EmptyBorder(4, 4, 4, 4));

        centro.add(crearPanelStats(nombreJ1, true),  BorderLayout.WEST);
        centro.add(crearPanelStats(nombreJ2, false), BorderLayout.EAST);
        centro.add(crearAreaCentral(),               BorderLayout.CENTER);
        return centro;
    }

    private JPanel crearPanelStats(String nombre, boolean esJ1) {
        JPanel p = new JPanel(new GridLayout(6, 1, 0, 2));
        p.setBackground(C_BG_PANEL);
        p.setPreferredSize(new Dimension(130, 0));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 50, 70), 1),
                new EmptyBorder(4, 6, 4, 6)));

        JLabel lNombre = new JLabel(nombre, SwingConstants.CENTER);
        lNombre.setForeground(C_DORADO);
        lNombre.setFont(new Font("Serif", Font.BOLD, 13));
        lNombre.setOpaque(true);
        lNombre.setBackground(C_BG_DARK);
        p.add(lNombre);

        p.add(crearFilaStat("LP:",      "8000", C_VERDE_LP, esJ1, "lp"));
        p.add(crearFilaStat("Mazo:",    "0 cartas",    C_TEXTO,   esJ1, "mazo"));
        p.add(crearFilaStat("Mano:",    "0 cartas",    C_TEXTO,   esJ1, "mano"));
        p.add(crearFilaStat("Campo:",   "0 monstruos", C_TEXTO,   esJ1, "monstruos"));
        p.add(crearFilaStat("Trampas:", "0 trampas",   C_LILA,    esJ1, "trampa"));
        return p;
    }

    private JPanel crearFilaStat(String etiq, String val, Color cVal, boolean esJ1, String tipo) {
        JPanel f = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 2));
        f.setBackground(C_BG_PANEL);
        JLabel e = new JLabel(etiq); e.setForeground(C_GRIS); e.setFont(new Font("SansSerif", Font.PLAIN, 11));
        JLabel v = new JLabel(val);  v.setForeground(cVal);   v.setFont(new Font("Serif", Font.BOLD, 13));
        f.add(e); f.add(v);

        if (esJ1) {
            switch (tipo) {
                case "lp":        lblLPJ1       = v; break;
                case "mazo":      lblMazo1      = v; break;
                case "mano":      lblMano1      = v; break;
                case "monstruos": lblMonstruos1 = v; break;
                case "trampa":    lblTrampa1    = v; break;
            }
        } else {
            switch (tipo) {
                case "lp":        lblLPJ2       = v; break;
                case "mazo":      lblMazo2      = v; break;
                case "mano":      lblMano2      = v; break;
                case "monstruos": lblMonstruos2 = v; break;
                case "trampa":    lblTrampa2    = v; break;
            }
        }
        return f;
    }

    private JPanel crearAreaCentral() {
        JPanel area = new JPanel(new GridLayout(7, 1, 0, 4));
        area.setBackground(C_BG_CAMPO);
        area.setBorder(new EmptyBorder(4, 6, 4, 6));

        panelManoJ2           = crearContenedorMano("Mano de " + nombreJ2);
        panelCampoMonstruosJ2 = crearFilaCampo(new Color(14, 30, 14));
        panelCampoMagiaJ2     = crearFilaCampo(new Color(10, 14, 30));

        JLabel vs = new JLabel("\u2500\u2500\u2500 VS \u2500\u2500\u2500", SwingConstants.CENTER);
        vs.setForeground(C_GRIS);
        vs.setFont(new Font("Serif", Font.ITALIC, 12));
        vs.setOpaque(true);
        vs.setBackground(C_BG_DARK);

        panelCampoMagiaJ1     = crearFilaCampo(new Color(10, 14, 30));
        panelCampoMonstruosJ1 = crearFilaCampo(new Color(14, 30, 14));
        panelManoJ1           = crearContenedorMano("Mano de " + nombreJ1);

        area.add(panelManoJ2);
        area.add(panelCampoMonstruosJ2);
        area.add(panelCampoMagiaJ2);
        area.add(vs);
        area.add(panelCampoMagiaJ1);
        area.add(panelCampoMonstruosJ1);
        area.add(panelManoJ1);

        return area;
    }

    private JPanel crearContenedorMano(String titulo) {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(new Color(12, 14, 22));
        contenedor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 50, 70), 1),
                new EmptyBorder(2, 4, 2, 4)));

        JLabel lbl = new JLabel(titulo, SwingConstants.LEFT);
        lbl.setForeground(C_GRIS);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 9));
        contenedor.add(lbl, BorderLayout.NORTH);

        JPanel cartas = new JPanel(new WrapLayout(FlowLayout.LEFT, 4, 2));
        cartas.setOpaque(false);
        contenedor.add(cartas, BorderLayout.CENTER);
        return contenedor;
    }

    private JPanel crearFilaCampo(Color bg) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 2));
        p.setBackground(bg);
        p.setBorder(BorderFactory.createLineBorder(new Color(30, 40, 60), 1));
        return p;
    }

    private void reconstruirMano(JPanel contenedor, List<Carta> mano) {
        JPanel panelCartas = (JPanel) ((BorderLayout) contenedor.getLayout())
                .getLayoutComponent(BorderLayout.CENTER);
        panelCartas.removeAll();
        if (mano != null) {
            for (Carta c : mano) {
                panelCartas.add(new CartaVisual(c));
            }
        }
        panelCartas.revalidate();
        panelCartas.repaint();
    }

    private void reconstruirCampoMonstruos(JPanel fila, List<Monstruo> lista) {
        fila.removeAll();
        if (lista != null) {
            for (Monstruo m : lista) {
                fila.add(new SlotCampoVisual(m, m.isEnPosicionAtaque()));
            }
            for (int i = lista.size(); i < 5; i++) {
                fila.add(crearSlotVacio("M"));
            }
        }
        fila.revalidate();
        fila.repaint();
    }

    private void reconstruirCampoMagia(JPanel fila, List<CartaMagica> magias, List<CartaTrampa> trampas) {
        fila.removeAll();
        if (magias != null && trampas != null) {
            for (CartaMagica m : magias)  fila.add(new SlotCampoVisual(m, true));
            for (CartaTrampa t : trampas) fila.add(new SlotCampoVisual(t, true));
            int n = magias.size() + trampas.size();
            for (int i = n; i < 5; i++) {
                fila.add(crearSlotVacio("S/T"));
            }
        }
        fila.revalidate();
        fila.repaint();
    }

    private JLabel crearSlotVacio(String texto) {
        JLabel slot = new JLabel(texto, SwingConstants.CENTER);
        slot.setPreferredSize(new Dimension(
                ImagenCartaCache.ANCHO_CAMPO + 4,
                ImagenCartaCache.ALTO_CAMPO  + 4));
        slot.setOpaque(true);

        Color bg  = texto.equals("M") ? new Color(14, 30, 14) : new Color(10, 14, 30);
        Color brd = texto.equals("M") ? new Color(30, 58, 30) : new Color(26, 30, 58);

        slot.setBackground(bg);
        slot.setForeground(new Color(brd.getRed(), brd.getGreen(), brd.getBlue(), 180));
        slot.setFont(new Font("SansSerif", Font.BOLD, 10));
        slot.setBorder(BorderFactory.createLineBorder(brd, 1));
        return slot;
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(C_BG_DARK);
        footer.setBorder(new EmptyBorder(4, 8, 6, 8));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        botones.setBackground(C_BG_DARK);

        btnJugarCarta    = new JButton("JUGAR CARTA");
        btnAtacar        = new JButton("ATACAR");
        btnActivarTrampa = new JButton("ACTIVAR TRAMPA");
        btnTerminarTurno = new JButton("TERMINAR TURNO");

        estilizarBoton(btnJugarCarta,    C_DORADO);
        estilizarBoton(btnAtacar,        new Color(224,  64,  64));
        estilizarBoton(btnActivarTrampa, new Color(160,  80, 200));
        estilizarBoton(btnTerminarTurno, C_GRIS);

        botones.add(btnJugarCarta);
        botones.add(btnAtacar);
        botones.add(btnActivarTrampa);
        footer.add(botones, BorderLayout.WEST);

        lblLog = new JLabel("\u00a1El duelo comienza!");
        lblLog.setForeground(C_GRIS);
        lblLog.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblLog.setHorizontalAlignment(SwingConstants.CENTER);
        footer.add(lblLog, BorderLayout.CENTER);

        JPanel der = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        der.setBackground(C_BG_DARK);
        der.add(btnTerminarTurno);
        footer.add(der, BorderLayout.EAST);

        return footer;
    }

    private void estilizarBoton(JButton btn, Color color) {
        btn.setForeground(color);
        btn.setBackground(C_BG_DARK);
        btn.setFont(new Font("SansSerif", Font.BOLD, 10));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(color, 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
