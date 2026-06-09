public class MirrorForce extends CartaTrampa {

    public MirrorForce() {
        super("Mirror Force");
    }

    @Override
    public void activar(Jugador jugadorActivo, Jugador jugadorRival) {
        jugadorRival.getCampo().getMonstruos().clear();
    }

    @Override
    public String getDescripcion() {
        return "Destruye todos los monstruos del rival.";
    }

}
