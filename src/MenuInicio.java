import javax.swing.*;
import java.awt.*;

public class MenuInicio extends JFrame {

    private JTextField campoNombre1;
    private JTextField campoNombre2;
    private JButton btnIniciar;

    public MenuInicio() {
        setTitle("Yu-Gi-Oh! - Inicio");
        setSize(420, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(10, 12, 16));
        initComponents();
    }

    private void initComponents() {
        JLabel titulo = new JLabel("YU-GI-OH!", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 30));
        titulo.setForeground(new Color(201, 168, 76));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));
        titulo.setOpaque(false);
        add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(new Color(10, 12, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 16, 8, 16);

        JLabel sub = new JLabel("¡Confía en el corazón de las cartas!", SwingConstants.CENTER);
        sub.setFont(new Font("Serif", Font.ITALIC, 12));
        sub.setForeground(new Color(122, 112, 96));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        centro.add(sub, gbc);

        gbc.gridwidth = 1;

        JLabel lbl1 = new JLabel("Duelista 1:");
        lbl1.setForeground(new Color(201, 168, 76));
        lbl1.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST;
        centro.add(lbl1, gbc);

        campoNombre1 = new JTextField("Yugi", 14);
        campoNombre1.setFont(new Font("SansSerif", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        centro.add(campoNombre1, gbc);

        JLabel lbl2 = new JLabel("Duelista 2:");
        lbl2.setForeground(new Color(201, 168, 76));
        lbl2.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST;
        centro.add(lbl2, gbc);

        campoNombre2 = new JTextField("Kaiba", 14);
        campoNombre2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        centro.add(campoNombre2, gbc);

        add(centro, BorderLayout.CENTER);

        btnIniciar = new JButton("¡DUELAR!");
        btnIniciar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnIniciar.setForeground(new Color(201, 168, 76));
        btnIniciar.setBackground(new Color(10, 12, 16));
        btnIniciar.setFocusPainted(false);
        btnIniciar.setBorder(BorderFactory.createLineBorder(new Color(201, 168, 76), 1));
        btnIniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JPanel sur = new JPanel();
        sur.setBackground(new Color(10, 12, 16));
        sur.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        sur.add(btnIniciar);
        add(sur, BorderLayout.SOUTH);
    }

    public String getNombre1() { return campoNombre1.getText().trim(); }
    public String getNombre2() { return campoNombre2.getText().trim(); }
    public JButton getBtnIniciar() { return btnIniciar; }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
