import java.util.LinkedList;

public class ControladorMenu {

    private MenuInicio menu;

    public ControladorMenu(MenuInicio menu) {
        this.menu = menu;

        menu.getBtnIniciar().addActionListener(e -> iniciarDuelo());
    }

    private void iniciarDuelo() {
        String n1 = menu.getNombre1();
        String n2 = menu.getNombre2();

        if (n1.isEmpty() || n2.isEmpty()) {
            menu.mostrarError("Por favor ingresa los nombres de ambos duelistas.");
            return;
        }
        if (n1.equals(n2)) {
            menu.mostrarError("Los nombres deben ser diferentes.");
            return;
        }

        // Crear modelo
        Jugador j1 = new Jugador(n1, 8000, new LinkedList<>(), null, new Campo());
        Jugador j2 = new Jugador(n2, 8000, new LinkedList<>(), null, new Campo());
        InicializadorMazo.repartirCartas(j1, j2);
        for (int i = 0; i < 5; i++) {
            j1.getMano().add(j1.getMazo().robarCarta());
            j2.getMano().add(j2.getMazo().robarCarta());
        }
        Juego juego = new Juego(j1, j2);

        // Crear vista del duelo (solo necesita los nombres)
        VentanaDuelo ventana = new VentanaDuelo(n1, n2);

        // Conectar modelo y vista a través del controlador del duelo
        new ControladorJuego(juego, ventana);

        menu.dispose();
    }
}
