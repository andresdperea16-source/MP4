public class JuegoMemento {

    private Jugador jugador1;
    private Jugador jugador2;
    private int numeroTurno;
    private int jugadorActualIdx;

    public JuegoMemento(
            Jugador jugador1,
            Jugador jugador2,
            int numeroTurno,
            int jugadorActualIdx) {

        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.numeroTurno = numeroTurno;
        this.jugadorActualIdx = jugadorActualIdx;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public int getNumeroTurno() {
        return numeroTurno;
    }

    public int getJugadorActualIdx() {
        return jugadorActualIdx;
    }
}