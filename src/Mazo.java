import java.util.Stack;

public class Mazo {

    private Stack<Carta> cartas;

    public Mazo() {
        this.cartas = new Stack<>();
    }

    public void agregarCarta(Carta carta) {
        cartas.push(carta);
    }

    public int getCantidadCartas() {
        return cartas.size();
    }

    public boolean estaVacio() {
        return cartas.isEmpty();
    }

    public Stack<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(Stack<Carta> cartas) {
        this.cartas = cartas;
    }

    public Carta robarCarta() {
        if (cartas.isEmpty()) {
            return null;
        }

        return cartas.pop();
    }
}
