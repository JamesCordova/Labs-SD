import javax.swing.*;
import java.awt.*;

public class Principal extends JFrame {

    private JButton btnDepartamentos, btnAsignaciones, btnProyectos, btnIngenieros;
    private JLabel lblTitulo;
    private JPanel panelTitulo, panelBotones;

    public Principal() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Cliente/Servidor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 255));

        // Panel del Título
        panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(30, 60, 120));
        lblTitulo = new JLabel("CLIENTE / SERVIDOR", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panelTitulo.add(lblTitulo);

        // Panel de botones
        panelBotones = new JPanel(new GridLayout(2, 2, 20, 20));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panelBotones.setBackground(new Color(240, 240, 255));

        btnDepartamentos = new JButton("Departamentos");
        btnAsignaciones = new JButton("Asignaciones");
        btnProyectos = new JButton("Proyectos");
        btnIngenieros = new JButton("Ingenieros");

        panelBotones.add(btnDepartamentos);
        panelBotones.add(btnAsignaciones);
        panelBotones.add(btnProyectos);
        panelBotones.add(btnIngenieros);

        add(panelTitulo, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);

        agregarListeners();
    }

    private void agregarListeners() {
        btnDepartamentos.addActionListener(e -> new DepartamentosGUI().setVisible(true));
        btnAsignaciones.addActionListener(e -> new AsignacionesGUI().setVisible(true));
        btnProyectos.addActionListener(e -> new ProyectosGUI().setVisible(true));
        btnIngenieros.addActionListener(e -> new Ingeniero().setVisible(true));
    }

    public static void main(String[] args) {
        // Aplicar Nimbus Look & Feel si está disponible
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Si no se puede, se usa el predeterminado
        }

        SwingUtilities.invokeLater(() -> new Principal().setVisible(true));
    }
}

