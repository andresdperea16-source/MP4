public abstract class CartaMagica extends Carta implements Activable {

    public CartaMagica(String nombre) {
        super(nombre);
    }

    @Override
    public abstract void activar(Jugador jugadorActivo, Jugador jugadorRival);
}