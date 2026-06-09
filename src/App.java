import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuInicio menu = new MenuInicio();
            new ControladorMenu(menu);
            menu.setVisible(true);
        });
    }
}
