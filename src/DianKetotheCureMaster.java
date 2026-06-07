public class DianKetotheCureMaster extends CartaMagica {

    public DianKetotheCureMaster() {
        super("Dian Keto the Cure Master");
    }

    @Override
    public void activar(Jugador jugadorActivo, Jugador jugadorRival) {
        jugadorActivo.setPuntosVida(jugadorActivo.getPuntosVida() + 1000);
    }

    @Override
    public String getDescripcion() {
        return "Cura 1000 puntos de vida del jugador activo.";
    }
}
