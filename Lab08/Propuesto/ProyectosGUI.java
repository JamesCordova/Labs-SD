import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ProyectosGUI extends JFrame {

    private JTextField txtIdProyecto, txtNombre, txtFechaInicio, txtFechaFin;
    private JComboBox<String> cmbIdDepartamento;
    private JButton btnNuevo, btnGuardar, btnEliminar, btnActualizar;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private DatabaseConnection dbConnection;

    public ProyectosGUI() {
        dbConnection = new DatabaseConnection();
        configurarVentana();
        crearEncabezado();
        crearFormulario();
        crearTablaYBotones();
        agregarListeners();
        cargarProyectos();
    }

    private void configurarVentana() {
        setTitle("Gestion de Proyectos");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 255));
    }

    private void crearEncabezado() {
        JLabel lblTitulo = new JLabel("Gestion de Proyectos", JLabel.CENTER);
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
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del Proyecto"));

        txtIdProyecto = new JTextField(15);
        txtNombre = new JTextField(15);
        txtFechaInicio = new JTextField(15);
        txtFechaFin = new JTextField(15);
        cmbIdDepartamento = new JComboBox<>();
        cargarDepartamentos();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        formulario.add(new JLabel("ID Proyecto:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtIdProyecto, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        formulario.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formulario.add(new JLabel("Fecha Inicio (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        formulario.add(txtFechaInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formulario.add(new JLabel("Fecha Fin (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        formulario.add(txtFechaFin, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formulario.add(new JLabel("ID Departamento:"), gbc);
        gbc.gridx = 1;
        formulario.add(cmbIdDepartamento, gbc);

        add(formulario, BorderLayout.WEST);
    }

    private void crearTablaYBotones() {
        JPanel centro = new JPanel(new BorderLayout(10, 10));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnNuevo = new JButton("Nuevo");
        btnGuardar = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Actualizar");

        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnEliminar);
        botones.add(btnActualizar);

        String[] columnas = { "IdProyecto", "Nombre", "FechaInicio", "FechaFin", "IdDepartamento" };
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(28);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(480, 200));

        centro.add(botones, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);
    }

    private void agregarListeners() {
        btnNuevo.addActionListener(e -> limpiarCampos());

        btnGuardar.addActionListener(e -> guardarProyecto());

        btnEliminar.addActionListener(e -> eliminarProyecto());

        btnActualizar.addActionListener(e -> actualizarProyecto());

        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    txtIdProyecto.setText(modeloTabla.getValueAt(fila, 0).toString());
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtFechaInicio.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtFechaFin.setText(modeloTabla.getValueAt(fila, 3).toString());
                    cmbIdDepartamento.setSelectedItem(modeloTabla.getValueAt(fila, 4).toString());
                }
            }
        });
    }

    private void cargarProyectos() {
        List<String[]> proyectos = dbConnection.getProjects();
        modeloTabla.setRowCount(0);
        for (String[] p : proyectos) {
            modeloTabla.addRow(p);
        }
    }

    private void cargarDepartamentos() {
        List<String[]> departamentos = dbConnection.getDepartments();
        for (String[] d : departamentos) {
            cmbIdDepartamento.addItem(d[0]);
        }
    }

    private void limpiarCampos() {
        txtIdProyecto.setText("");
        txtNombre.setText("");
        txtFechaInicio.setText("");
        txtFechaFin.setText("");
        cmbIdDepartamento.setSelectedIndex(-1);
        txtIdProyecto.requestFocus();
    }

    private boolean esFechaValida(String fecha) {
        try {
            LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void guardarProyecto() {
        String id = txtIdProyecto.getText();
        String nombre = txtNombre.getText();
        String fechaIni = txtFechaInicio.getText();
        String fechaFin = txtFechaFin.getText();
        String dep = (String) cmbIdDepartamento.getSelectedItem();

        if (!esFechaValida(fechaIni) || !esFechaValida(fechaFin)) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use yyyy-MM-dd", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate ini = LocalDate.parse(fechaIni);
        LocalDate fin = LocalDate.parse(fechaFin);

        if (ini.isAfter(fin)) {
            JOptionPane.showMessageDialog(this, "La fecha de inicio debe ser anterior a la de fin", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!id.isEmpty() && !nombre.isEmpty()) {
            if (!dbConnection.projectExists(id)) {
                dbConnection.insertProject(id, nombre, fechaIni, fechaFin, dep);
                modeloTabla.addRow(new Object[]{ id, nombre, fechaIni, fechaFin, dep });
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Ya existe un proyecto con ese ID.", "ID duplicado", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Ingrese al menos ID y Nombre del proyecto.", "Campos requeridos", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarProyecto() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            String id = modeloTabla.getValueAt(fila, 0).toString();
            dbConnection.deleteProject(id);
            modeloTabla.removeRow(fila);
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un proyecto para eliminar.", "Seleccion requerida", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarProyecto() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            String id = txtIdProyecto.getText();
            String nombre = txtNombre.getText();
            String fechaIni = txtFechaInicio.getText();
            String fechaFin = txtFechaFin.getText();
            String dep = (String) cmbIdDepartamento.getSelectedItem();

            if (!id.isEmpty() && !nombre.isEmpty() && !fechaIni.isEmpty() && !fechaFin.isEmpty() && dep != null) {
                dbConnection.updateProject(id, nombre, fechaIni, fechaFin, dep);
                cargarProyectos();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Complete todos los campos para actualizar.", "Campos requeridos", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un proyecto para actualizar.", "Seleccion requerida", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProyectosGUI().setVisible(true));
    }
}
