import java.util.List;
import javax.swing.JOptionPane;

public class ControladorJuego {

    private Juego juego;
    private VentanaDuelo ventana;

    public ControladorJuego(Juego juego, VentanaDuelo ventana) {
        this.juego = juego;
        this.ventana = ventana;

        ventana.getBtnJugarCarta().addActionListener(e -> jugarCartaGUI());
        ventana.getBtnAtacar().addActionListener(e -> atacarGUI());
        ventana.getBtnActivarTrampa().addActionListener(e -> activarTrampaGUI());
        ventana.getBtnTerminarTurno().addActionListener(e -> terminarTurnoGUI());
        ventana.getBtnGuardar().addActionListener(e -> guardarPartidaGUI());

        ventana.setLog("¡El duelo comienza! Turno de " + juego.getJugadorActual().getNombre());
        ventana.setTurno(juego.getNumeroTurno());
        actualizarVista();
    }

    private void actualizarVista() {
        Jugador j1 = juego.getJugador1();
        Jugador j2 = juego.getJugador2();

        ventana.actualizarInterfaz(
            j1.getPuntosVida(), j2.getPuntosVida(),
            j1.getMazo() != null ? j1.getMazo().getCantidadCartas() : 0,
            j2.getMazo() != null ? j2.getMazo().getCantidadCartas() : 0,
            j1.getMano() != null ? j1.getMano().size() : 0,
            j2.getMano() != null ? j2.getMano().size() : 0,
            j1.getCampo() != null ? j1.getCampo().getCantidadMonstruos() : 0,
            j2.getCampo() != null ? j2.getCampo().getCantidadMonstruos() : 0,
            j1.getCampo() != null ? j1.getCampo().getCartasTrampa().size() : 0,
            j2.getCampo() != null ? j2.getCampo().getCartasTrampa().size() : 0,
            j1.getMano(),
            j2.getMano(),
            j1.getCampo() != null ? j1.getCampo().getMonstruos() : null,
            j2.getCampo() != null ? j2.getCampo().getMonstruos() : null,
            j1.getCampo() != null ? j1.getCampo().getCartasMagicas() : null,
            j2.getCampo() != null ? j2.getCampo().getCartasMagicas() : null,
            j1.getCampo() != null ? j1.getCampo().getCartasTrampa() : null,
            j2.getCampo() != null ? j2.getCampo().getCartasTrampa() : null
        );
    }

    private void jugarCartaGUI() {
        List<Carta> mano = juego.getJugadorActual().getMano();
        if (mano.isEmpty()) { ventana.setLog("No tienes cartas en la mano."); return; }

        String[] opciones = new String[mano.size()];
        for (int i = 0; i < mano.size(); i++) {
            Carta c = mano.get(i);
            if (c instanceof Monstruo) {
                Monstruo m = (Monstruo) c;
                opciones[i] = m.getNombre() + " " + m.getEstrellas() + " ATK:" + m.getAtk() + " DEF:" + m.getDef();
            } else {
                String tipo = (c instanceof CartaMagica) ? "MAGIA" : "TRAMPA";
                opciones[i] = "[" + tipo + "] " + c.getNombre() + " - " + c.getDescripcion();
            }
        }

        int sel = JOptionPane.showOptionDialog(ventana, "Elige una carta para jugar:", "Jugar carta",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
        if (sel == -1) return;

        Carta carta = mano.get(sel);
        if (carta instanceof Monstruo) {
            String[] modos = {"Posición Ataque", "Posición Defensa"};
            int modo = JOptionPane.showOptionDialog(ventana, "¿En qué posición invocar?", "Posición",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, modos, modos[0]);
            if (modo == -1) return;

            Monstruo m = (Monstruo) carta;
            int indexSacrificio = -1;

            if (m.getNivel() > 4) {
                List<Monstruo> monstruosCampo = juego.getJugadorActual().getCampo().getMonstruos();
                if (monstruosCampo.isEmpty()) {
                    ventana.setLog("Necesitas un monstruo para sacrificar.");
                    return;
                }
                String[] opcionesSacrificio = monstruosCampo.stream()
                        .map(mon -> mon.getNombre() + " ATK:" + mon.getAtk())
                        .toArray(String[]::new);
                indexSacrificio = JOptionPane.showOptionDialog(ventana, "Elige el monstruo a sacrificar:", "Sacrificio",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opcionesSacrificio, opcionesSacrificio[0]);
                if (indexSacrificio == -1) return;
            }

            juego.jugarCartaDesdeMano(sel, indexSacrificio);
            List<Monstruo> campo = juego.getJugadorActual().getCampo().getMonstruos();
            if (!campo.isEmpty()) campo.get(campo.size() - 1).setEnPosicionAtaque(modo == 0);
        } else {
            juego.jugarCartaDesdeMano(sel, -1);
        }

        ventana.setLog("Carta jugada: " + opciones[sel]);
        actualizarVista();
        verificarGanador();
    }

    private void atacarGUI() {
        Jugador actual = juego.getJugadorActual();
        Jugador rival  = juego.getOponente();

        if (actual.getCampo().getMonstruos().isEmpty()) {
            ventana.setLog("No tienes monstruos para atacar.");
            return;
        }

        List<Monstruo> misMonst = actual.getCampo().getMonstruos();
        String[] atacantes = misMonst.stream()
                .map(m -> m.getNombre() + " ATK:" + m.getAtk() + (m.isYaAtaco() ? " (ya atacó)" : ""))
                .toArray(String[]::new);

        int idxAtacante = JOptionPane.showOptionDialog(ventana, "Elige tu monstruo atacante:", "Atacar",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, atacantes, atacantes[0]);
        if (idxAtacante == -1) return;

        if (misMonst.get(idxAtacante).isYaAtaco()) {
            ventana.setLog(misMonst.get(idxAtacante).getNombre() + " ya atacó este turno.");
            return;
        }

        int idxDefensor = 0;
        if (!rival.getCampo().getMonstruos().isEmpty()) {
            List<Monstruo> rivMonst = rival.getCampo().getMonstruos();
            String[] defensores = rivMonst.stream()
                    .map(m -> m.getNombre() + " [" + (m.isEnPosicionAtaque() ? "ATK:" + m.getAtk() : "DEF:" + m.getDef()) + "]")
                    .toArray(String[]::new);
            idxDefensor = JOptionPane.showOptionDialog(ventana, "Elige el monstruo enemigo:", "Defensor",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, defensores, defensores[0]);
            if (idxDefensor == -1) return;
        }

        String resultado = juego.atacar(idxAtacante, idxDefensor);
        ventana.setLog(resultado);
        actualizarVista();
        verificarGanador();
    }

    private void activarTrampaGUI() {
        List<CartaTrampa> trampas = juego.getJugadorActual().getCampo().getCartasTrampa();
        if (trampas.isEmpty()) { ventana.setLog("No tienes trampas en el campo."); return; }

        String[] opciones = trampas.stream().map(Carta::getNombre).toArray(String[]::new);
        int sel = JOptionPane.showOptionDialog(ventana, "Elige una trampa para activar:", "Activar trampa",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);
        if (sel == -1) return;

        String resultado = juego.activarTrampa(sel);
        ventana.setLog(resultado);
        actualizarVista();
        verificarGanador();
    }

    private void terminarTurnoGUI() {
        juego.cambiarTurno();
        Jugador actual = juego.getJugadorActual();

        Carta robada = actual.getMazo().robarCarta();
        if (robada != null) {
            actual.getMano().add(robada);

            juego.registrarEvento(
                actual.getNombre() +
                " robó la carta " +
                robada.getNombre()
            );

            ventana.setLog("Turno de " + actual.getNombre() + " | Robó: " + robada.getNombre());
        } else {
            ventana.setLog(actual.getNombre() + " no puede robar carta. ¡Pierde el duelo!");
            mostrarGanador(juego.getOponente().getNombre());
            return;
        }

        ventana.setTurno(juego.getNumeroTurno());
        actualizarVista();
    }

    private void guardarPartidaGUI() {
        try {
            GestorArchivos.guardarPartida(juego);
            ventana.setLog("Partida guardada correctamente.");
            JOptionPane.showMessageDialog(ventana,
                "La partida fue guardada exitosamente.\nPuedes continuar desde el menu principal.",
                "Partida guardada", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(ventana,
                "Error al guardar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verificarGanador() {
        if (juego.hayGanador()) mostrarGanador(juego.getNombreGanador());
    }

    private void mostrarGanador(String nombre) {
        ventana.deshabilitarAcciones();

        // Guardar resultado en el historial
        GestorArchivos.guardarResultado(
            juego.getJugador1().getNombre(),
            juego.getJugador2().getNombre(),
            nombre,
            juego.getNumeroTurno(),
            juego.getJugador1().getPuntosVida(),
            juego.getJugador2().getPuntosVida()
        );

        String mensaje = "<html><center>" +
                "<font size='5' color='#C9A84C'><b>¡DUELO TERMINADO!</b></font><br><br>" +
                "<font size='4' color='white'>¡<b>" + nombre + "</b> gana el duelo!</font><br><br>" +
                "<font size='3' color='gray'><i>\"Confía en el corazón de las cartas\"</i></font>" +
                "</center></html>";
        JOptionPane.showMessageDialog(ventana, mensaje, "Yu-Gi-Oh!", JOptionPane.PLAIN_MESSAGE);
    }
}
