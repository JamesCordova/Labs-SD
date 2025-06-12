import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AsignacionesGUI extends JFrame {

    private JTextField txtFechaAsignacion;
    private JButton btnNew, btnSave, btnDelete, btnUpdate;
    private JComboBox<String> cmbIdProyecto;
    private JComboBox<String> cmbIdIngeniero;
    private JTable table;
    private DefaultTableModel tableModel;
    private DatabaseConnection dbConnection;

    public AsignacionesGUI() {
        dbConnection = new DatabaseConnection();
        configurarVentana();
        crearEncabezado();
        crearFormulario();
        crearTablaYBotones();
        agregarListeners();
        cargarAssignments();
    }

    private void configurarVentana() {
        setTitle("Gestion de Asignaciones");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 255));
    }

    private void crearEncabezado() {
        JLabel lblTitle = new JLabel("Gestion de Asignaciones", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(30, 60, 120));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(lblTitle, BorderLayout.NORTH);
    }

    private void crearFormulario() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(240, 240, 255));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Datos de la Asignacion"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.WEST;

        cmbIdProyecto = new JComboBox<>();
        cargarProyectos();
        cmbIdIngeniero = new JComboBox<>();
        cargarIngenieros();
        txtFechaAsignacion = new JTextField(12);

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("ID Proyecto:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(cmbIdProyecto, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("ID Ingeniero:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(cmbIdIngeniero, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Fecha Asignacion (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        inputPanel.add(txtFechaAsignacion, gbc);

        add(inputPanel, BorderLayout.WEST);
    }

    private void crearTablaYBotones() {
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnNew = new JButton("Nuevo");
        btnSave = new JButton("Guardar");
        btnDelete = new JButton("Eliminar");
        btnUpdate = new JButton("Actualizar");

        buttonPanel.add(btnNew);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnUpdate);

        String[] columns = { "IDProyecto", "IDIngeniero", "FechaAsignacion" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(420, 200));

        centerPanel.add(buttonPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void agregarListeners() {
        btnNew.addActionListener(e -> clearFields());

        btnSave.addActionListener(e -> saveAssignment());

        btnDelete.addActionListener(e -> deleteAssignment());

        btnUpdate.addActionListener(e -> updateAssignment());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    cmbIdProyecto.setSelectedItem(tableModel.getValueAt(row, 0).toString());
                    cmbIdIngeniero.setSelectedItem(tableModel.getValueAt(row, 1).toString());
                    txtFechaAsignacion.setText(tableModel.getValueAt(row, 2).toString());
                }
            }
        });
    }

    private void cargarProyectos() {
        List<String[]> proyectos = dbConnection.getProjects();
        for (String[] proyecto : proyectos) {
            cmbIdProyecto.addItem(proyecto[0]);
        }
    }

    private void cargarIngenieros() {
        List<String[]> ingenieros = dbConnection.getEngineers();
        for (String[] ingeniero : ingenieros) {
            cmbIdIngeniero.addItem(ingeniero[0]);
        }
    }

    private void cargarAssignments() {
        List<String[]> assignments = dbConnection.getAssignments();
        for (String[] assignment : assignments) {
            tableModel.addRow(assignment);
        }
    }

    private void clearFields() {
        cmbIdProyecto.setSelectedIndex(-1);
        cmbIdIngeniero.setSelectedIndex(-1);
        txtFechaAsignacion.setText("");
    }

    private boolean isValidDateFormat(String dateStr) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void saveAssignment() {
        String idProy = (String) cmbIdProyecto.getSelectedItem();
        String idIng = (String) cmbIdIngeniero.getSelectedItem();
        String fecha = txtFechaAsignacion.getText();

        if (!isValidDateFormat(fecha)) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use yyyy-MM-dd", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!idProy.isEmpty() && !idIng.isEmpty() && !fecha.isEmpty()) {
            if (dbConnection.assignmentExists(idProy)) {
                JOptionPane.showMessageDialog(this, "Ya existe una asignacion con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                dbConnection.insertAssignments(idProy, idIng, fecha);
                tableModel.addRow(new Object[] { idProy, idIng, fecha });
                clearFields();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.");
        }
    }

    private void deleteAssignment() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String id = tableModel.getValueAt(row, 0).toString();
            dbConnection.deleteAssignment(id);
            tableModel.removeRow(row);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una asignacion para eliminar.");
        }
    }

    private void updateAssignment() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String oldId = tableModel.getValueAt(row, 0).toString();
            String newId = (String) cmbIdProyecto.getSelectedItem();
            String idIng = (String) cmbIdIngeniero.getSelectedItem();
            String fecha = txtFechaAsignacion.getText();

            if (!isValidDateFormat(fecha)) {
                JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use yyyy-MM-dd", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newId.equals(oldId) && dbConnection.assignmentExists(newId)) {
                JOptionPane.showMessageDialog(this, "Ya existe una asignacion con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dbConnection.updateAssignment(oldId, newId, idIng, fecha);
            tableModel.setValueAt(newId, row, 0);
            tableModel.setValueAt(idIng, row, 1);
            tableModel.setValueAt(fecha, row, 2);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una asignacion para actualizar.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AsignacionesGUI().setVisible(true));
    }
}
