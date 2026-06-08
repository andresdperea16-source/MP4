import java.util.List;
import java.util.Random;

public class Juego {

    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador jugadorActual;
    private Jugador oponente;

    private boolean cartaJugadasEnTurno = false;
    private int numeroTurno = 1;

    public Jugador getJugadorActual(){ return jugadorActual; }
    public Jugador getOponente(){ return oponente; }
    public Jugador getJugador1(){ return jugador1; }
    public Jugador getJugador2(){ return jugador2; }
    public int getNumeroTurno(){ return numeroTurno; }

    public Juego(Jugador j1, Jugador j2) {
        this.jugador1 = j1;
        this.jugador2 = j2;

        if (new Random().nextBoolean()) {
            jugadorActual = jugador1;
            oponente = jugador2;
        } else {
            jugadorActual = jugador2;
            oponente = jugador1;
        }

        jugadorActual.setPrimerTurno(true);
        oponente.setPrimerTurno(false);
    }

    // Constructor para cargar una partida guardada
    public Juego(Jugador j1, Jugador j2, int numeroTurno, int jugadorActualIdx) {
        this.jugador1 = j1;
        this.jugador2 = j2;
        this.numeroTurno = numeroTurno;

        if (jugadorActualIdx == 0) {
            jugadorActual = jugador1;
            oponente = jugador2;
        } else {
            jugadorActual = jugador2;
            oponente = jugador1;
        }
        // En una partida cargada no es el primer turno de nadie
        jugadorActual.setPrimerTurno(false);
        oponente.setPrimerTurno(false);
    }

    public void jugarCartaDesdeMano(int index, int indexSacrificio) {

        if (cartaJugadasEnTurno) return;

        List<Carta> mano = jugadorActual.getMano();

        if (mano.isEmpty() || index < 0 || index >= mano.size()) return;

        Carta carta = mano.get(index);

        if (carta instanceof Monstruo) {

            Monstruo m = (Monstruo) carta;

            // Sacrificio para monstruos fuertes
            if (m.getNivel() > 4) {

                List<Monstruo> monstruosCampo = jugadorActual.getCampo().getMonstruos();

                if (monstruosCampo.isEmpty()) return;

                if (indexSacrificio < 0 || indexSacrificio >= monstruosCampo.size()) return;

                Monstruo sacrificio = monstruosCampo.get(indexSacrificio);

                jugadorActual.getCampo().quitarMonstruo(sacrificio);
            }

            jugadorActual.getCampo().colocarMonstruo(m);

        } else if (carta instanceof CartaMagica) {

            ((CartaMagica) carta).activar(jugadorActual, oponente);

        } else if (carta instanceof CartaTrampa) {

            jugadorActual.getCampo().colocarCartaTrampa((CartaTrampa) carta);
        }

        mano.remove(index);

        cartaJugadasEnTurno = true;
    }

    public String activarTrampa(int index) {
        List<CartaTrampa> trampas = jugadorActual.getCampo().getCartasTrampa();
        if (index < 0 || index >= trampas.size()) return "Trampa inválida.";
        CartaTrampa trampa = trampas.get(index);
        trampa.activar(jugadorActual, oponente);
        trampas.remove(index);
        return "¡Trampa activada! " + trampa.getNombre();
    }

    public String atacar(int indexAtacante, int indexDefensor) {
        if (jugadorActual.isPrimerTurno()) return "No puedes atacar en el primer turno.";
        if (jugadorActual.getCampo().getCantidadMonstruos() == 0) return "No tienes monstruos para atacar.";

        List<Monstruo> monstruosActuales = jugadorActual.getCampo().getMonstruos();
        if (indexAtacante < 0 || indexAtacante >= monstruosActuales.size()) return "Atacante inválido.";

        Monstruo atacante = monstruosActuales.get(indexAtacante);
        if (atacante.isYaAtaco()) return atacante.getNombre() + " ya atacó este turno.";

        // Ataques
        if (oponente.getCampo().getCantidadMonstruos() == 0) {
            oponente.setPuntosVida(oponente.getPuntosVida() - atacante.getAtk());
            atacante.setYaAtaco(true);
            return "¡Ataque directo de " + atacante.getNombre() + " por " + atacante.getAtk() + "!";
        }

        List<Monstruo> monstruosRival = oponente.getCampo().getMonstruos();
        if (indexDefensor < 0 || indexDefensor >= monstruosRival.size()) return "Defensor inválido.";

        Monstruo defensor = monstruosRival.get(indexDefensor);
        atacante.setYaAtaco(true);

        if (defensor.isEnPosicionAtaque()) {
            // atacanta a atacante
            if (atacante.getAtk() > defensor.getAtk()) {
                int daño = atacante.getAtk() - defensor.getAtk();
                oponente.setPuntosVida(oponente.getPuntosVida() - daño);
                oponente.getCampo().quitarMonstruo(defensor);
                return "Destruiste a " + defensor.getNombre() + " y causaste " + daño + " de daño.";
            } else if (atacante.getAtk() < defensor.getAtk()) {
                int daño = defensor.getAtk() - atacante.getAtk();
                jugadorActual.setPuntosVida(jugadorActual.getPuntosVida() - daño);
                jugadorActual.getCampo().quitarMonstruo(atacante);
                return "Tu monstruo fue destruido. Recibes " + daño + " de daño.";
            } else {
                jugadorActual.getCampo().quitarMonstruo(atacante);
                oponente.getCampo().quitarMonstruo(defensor);
                return "Ambos monstruos fueron destruidos.";
            }
        } else {
            // atacante a defensa
            if (atacante.getAtk() > defensor.getDef()) {
                oponente.getCampo().quitarMonstruo(defensor);
                return "Destruiste a " + defensor.getNombre() + " en posición de defensa. Sin daño a LP.";
            } else if (atacante.getAtk() < defensor.getDef()) {
                int daño = defensor.getDef() - atacante.getAtk();
                jugadorActual.setPuntosVida(jugadorActual.getPuntosVida() - daño);
                return "¡Defensa resistida! Recibes " + daño + " de daño.";
            } else {
                return "Ataque igualado. Sin efectos.";
            }
        }
    }

    public void cambiarTurno() {
        Jugador temp = jugadorActual;
        jugadorActual = oponente;
        oponente = temp;
        jugadorActual.setPrimerTurno(false);
        // Resetear ataques de monstruos
        for (Monstruo m : jugadorActual.getCampo().getMonstruos()) {
            m.setYaAtaco(false);
        }
        cartaJugadasEnTurno = false;
        numeroTurno++;
    }

    public boolean hayGanador() {
        return jugador1.getPuntosVida() <= 0 || jugador2.getPuntosVida() <= 0;
    }

    public String getNombreGanador() {
        if (jugador1.getPuntosVida() <= 0) return jugador2.getNombre();
        if (jugador2.getPuntosVida() <= 0) return jugador1.getNombre();
        return null;
    }
}
