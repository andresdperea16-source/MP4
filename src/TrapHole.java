public class TrapHole extends CartaTrampa {

    public TrapHole() {
        super("Trap Hole");
    }

    @Override
    public void activar(Jugador jugadorActivo, Jugador jugadorRival) {
        if (jugadorRival.getCampo().getCantidadMonstruos() == 0) return;
        int ultimoIndex = jugadorRival.getCampo().getMonstruos().size() - 1;
        Monstruo objetivo = jugadorRival.getCampo().getMonstruos().get(ultimoIndex);
        if (objetivo.getAtk() > 1000) {
            jugadorRival.getCampo().quitarMonstruo(objetivo);
        }
    }

    @Override
    public String getDescripcion() {
        return "Destruye un monstruo enemigo con más de 1000 ATK.";
    }

}
