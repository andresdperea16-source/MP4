public abstract class CartaTrampa extends Carta implements Activable {

    public CartaTrampa(String nombre) {
        super(nombre);
    }

    @Override
    public abstract void activar(Jugador jugadorActivo, Jugador jugadorRival);
}