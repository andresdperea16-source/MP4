import java.util.List;

public class Jugador {
    private String nombre;
    private int puntosVida;
    private List<Carta> mano;
    private Mazo mazo;
    private Campo campo;
    private boolean primerTurno = true;

    public Jugador(String nombre, int puntosVida, List<Carta> mano, Mazo mazo, Campo campo) {
        this.nombre = nombre;
        this.puntosVida = puntosVida;
        this.mano = mano;
        this.mazo = mazo;
        this.campo = campo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntosVida() {
        return puntosVida;
    }

    public void setPuntosVida(int puntosVida) {
        this.puntosVida = puntosVida;
    }

    public List<Carta> getMano() {
        return mano;
    }

    public void setMano(List<Carta> mano) {
        this.mano = mano;
    }

    public Mazo getMazo() {
        return mazo;
    }

    public void setMazo(Mazo mazo) {
        this.mazo = mazo;
    }

    public Campo getCampo() {
        return campo;
    }

    public void setCampo(Campo campo) {
        this.campo = campo;
    }

    public boolean isPrimerTurno() {
        return primerTurno;
    }

    public void setPrimerTurno(boolean primerTurno) {
        this.primerTurno = primerTurno;
    }
}