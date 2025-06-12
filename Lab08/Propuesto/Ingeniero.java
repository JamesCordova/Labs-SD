import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class Ingeniero extends JFrame {

    private JTextField txtId, txtNombre, txtEspecialidad, txtCargo;
    private JButton btnNuevo, btnGuardar, btnEliminar, btnActualizar;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private DatabaseConnection dbConnection;

    public Ingeniero() {
        dbConnection = new DatabaseConnection();
        configurarVentana();
        crearEncabezado();
        crearFormulario();
        crearTablaYBotones();
        agregarListeners();
        cargarIngenieros();
    }

    private void configurarVentana() {
        setTitle("Gestion de Ingenieros");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 255));
    }

    private void crearEncabezado() {
        JLabel lblTitulo = new JLabel("Gestion de Ingenieros", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(30, 60, 120));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(lblTitulo, BorderLayout.NORTH);
    }

    private void crearFormulario() {
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(new Color(240, 240, 255));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del Ingeniero"));

        txtId = new JTextField(15);
        txtNombre = new JTextField(15);
        txtEspecialidad = new JTextField(15);
        txtCargo = new JTextField(15);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formulario.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formulario.add(new JLabel("Especialidad:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtEspecialidad, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formulario.add(new JLabel("Cargo:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtCargo, gbc);

        add(formulario, BorderLayout.WEST);
    }

    private void crearTablaYBotones() {
        JPanel centro = new JPanel(new BorderLayout(10, 10));

        // Botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnNuevo = new JButton("Nuevo");
        btnGuardar = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Actualizar");

        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnEliminar);
        botones.add(btnActualizar);

        // Tabla
        String[] columnas = { "ID", "Nombre", "Especialidad", "Cargo" };
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(28);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(420, 200));

        centro.add(botones, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);

        add(centro, BorderLayout.CENTER);
    }

    private void agregarListeners() {
        btnNuevo.addActionListener(e -> limpiarCampos());

        btnGuardar.addActionListener(e -> guardarIngeniero());

        btnEliminar.addActionListener(e -> eliminarIngeniero());

        btnActualizar.addActionListener(e -> actualizarIngeniero());

        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtEspecialidad.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtCargo.setText(modeloTabla.getValueAt(fila, 3).toString());
                }
            }
        });
    }

    private void cargarIngenieros() {
        List<String[]> ingenieros = dbConnection.getEngineers();
        for (String[] ing : ingenieros) {
            modeloTabla.addRow(ing);
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtEspecialidad.setText("");
        txtCargo.setText("");
        txtId.requestFocus();
    }

    private void guardarIngeniero() {
        String id = txtId.getText();
        String nombre = txtNombre.getText();
        String cargo = txtCargo.getText();
        String especialidad = txtEspecialidad.getText();

        if (!id.isEmpty() && !nombre.isEmpty() && !cargo.isEmpty() && !especialidad.isEmpty()) {
            if (dbConnection.engineerExists(id)) {
                JOptionPane.showMessageDialog(this, "Ya existe un ingeniero con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                dbConnection.insertEngineers(id, nombre, cargo, especialidad);
                modeloTabla.addRow(new Object[]{ id, nombre, especialidad, cargo });
                limpiarCampos();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
        }
    }

    private void actualizarIngeniero() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            String oldId = modeloTabla.getValueAt(fila, 0).toString();
            String newId = txtId.getText();
            String nombre = txtNombre.getText();
            String cargo = txtCargo.getText();
            String especialidad = txtEspecialidad.getText();

            if (!oldId.isEmpty() && !newId.isEmpty() && !nombre.isEmpty() && !cargo.isEmpty() && !especialidad.isEmpty()) {
                if (!newId.equals(oldId) && dbConnection.engineerExists(newId)) {
                    JOptionPane.showMessageDialog(this, "Ya existe un ingeniero con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<String> asignaciones = dbConnection.getAssignmentByEngineer(oldId);
                if (!asignaciones.isEmpty()) {
                    StringBuilder msg = new StringBuilder("No se puede actualizar el ingeniero porque tiene asignaciones:\n");
                    for (String a : asignaciones) {
                        msg.append("- ").append(a).append("\n");
                    }
                    JOptionPane.showMessageDialog(this, msg.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                modeloTabla.setValueAt(newId, fila, 0);
                modeloTabla.setValueAt(nombre, fila, 1);
                modeloTabla.setValueAt(especialidad, fila, 2);
                modeloTabla.setValueAt(cargo, fila, 3);
                dbConnection.updateEngineer(oldId, newId, nombre, cargo, especialidad);
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un ingeniero para actualizar.");
        }
    }

    private void eliminarIngeniero() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            String id = modeloTabla.getValueAt(fila, 0).toString();
            List<String> asignaciones = dbConnection.getAssignmentByEngineer(id);
            if (!asignaciones.isEmpty()) {
                StringBuilder msg = new StringBuilder("No se puede eliminar el ingeniero porque tiene asignaciones:\n");
                for (String a : asignaciones) {
                    msg.append("- ").append(a).append("\n");
                }
                JOptionPane.showMessageDialog(this, msg.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                dbConnection.deleteEngineer(id);
                modeloTabla.removeRow(fila);
                limpiarCampos();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un ingeniero para eliminar.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Ingeniero().setVisible(true));
    }
}
