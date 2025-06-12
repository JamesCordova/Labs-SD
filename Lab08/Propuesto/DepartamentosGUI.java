import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DepartamentosGUI extends JFrame {

    private JTextField txtId, txtName, txtPhone, txtFax;
    private JButton btnNew, btnSave, btnDelete, btnUpdate;
    private JTable table;
    private DefaultTableModel tableModel;
    private DatabaseConnection dbConnection;

    public DepartamentosGUI() {
        dbConnection = new DatabaseConnection();
        configurarVentana();
        crearEncabezado();
        crearFormularioEntrada();
        crearBotonesYTabla();
        agregarListeners();
        cargarDepartamentos();
    }

    private void configurarVentana() {
        setTitle("Gestion de Departamentos");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 255)); // color de fondo general
    }

    private void crearEncabezado() {
        JLabel lblTitle = new JLabel("Gestion de Departamentos", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setOpaque(true);
        lblTitle.setBackground(new Color(30, 60, 120));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(lblTitle, BorderLayout.NORTH);
    }

    private void crearFormularioEntrada() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(245, 245, 255));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Datos del Departamento"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        txtId = new JTextField(15);
        txtName = new JTextField(15);
        txtPhone = new JTextField(15);
        txtFax = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("ID Departamento:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Telefono:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(txtPhone, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Fax:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(txtFax, gbc);

        add(inputPanel, BorderLayout.WEST);
    }

    private void crearBotonesYTabla() {
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnNew = new JButton("Nuevo");
        btnSave = new JButton("Guardar");
        btnDelete = new JButton("Eliminar");
        btnUpdate = new JButton("Actualizar");

        buttonPanel.add(btnNew);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnUpdate);

        centerPanel.add(buttonPanel, BorderLayout.NORTH);

        // Tabla
        String[] columns = { "IdDepartamento", "Nombre", "Telefono", "Fax" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void agregarListeners() {
        btnNew.addActionListener(e -> clearFields());

        btnSave.addActionListener(e -> saveDepartment());

        btnDelete.addActionListener(e -> deleteDepartment());

        btnUpdate.addActionListener(e -> updateDepartment());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    txtPhone.setText(tableModel.getValueAt(row, 2).toString());
                    txtFax.setText(tableModel.getValueAt(row, 3).toString());
                }
            }
        });
    }

    private void cargarDepartamentos() {
        List<String[]> departments = dbConnection.getDepartments();
        for (String[] dep : departments) {
            tableModel.addRow(dep);
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtPhone.setText("");
        txtFax.setText("");
        txtId.requestFocus();
    }

    private void saveDepartment() {
        String id = txtId.getText();
        String name = txtName.getText();
        String phone = txtPhone.getText();
        String fax = txtFax.getText();

        if (!id.isEmpty() && !name.isEmpty() && !phone.isEmpty() && !fax.isEmpty()) {
            if (dbConnection.departmentExists(id)) {
                JOptionPane.showMessageDialog(this, "Ya existe un departamento con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                dbConnection.insertDepartment(id, name, phone, fax);
                tableModel.addRow(new Object[]{ id, name, phone, fax });
                clearFields();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
        }
    }

    private void deleteDepartment() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String id = tableModel.getValueAt(row, 0).toString();
            List<String> projects = dbConnection.getProjectsByDepartment(id);
            if (!projects.isEmpty()) {
                StringBuilder msg = new StringBuilder("No se puede eliminar. Proyectos asociados:\n");
                for (String pid : projects) msg.append(" - ").append(pid).append("\n");
                JOptionPane.showMessageDialog(this, msg.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                dbConnection.deleteDepartment(id);
                tableModel.removeRow(row);
                clearFields();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un departamento para eliminar.");
        }
    }

    private void updateDepartment() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String oldId = tableModel.getValueAt(row, 0).toString();
            String newId = txtId.getText();
            String name = txtName.getText();
            String phone = txtPhone.getText();
            String fax = txtFax.getText();

            if (!newId.equals(oldId) && dbConnection.departmentExists(newId)) {
                JOptionPane.showMessageDialog(this, "Ya existe un departamento con ese nuevo ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<String> projects = dbConnection.getProjectsByDepartment(oldId);
            if (!projects.isEmpty()) {
                StringBuilder msg = new StringBuilder("No se puede actualizar. Proyectos asociados:\n");
                for (String pid : projects) msg.append(" - ").append(pid).append("\n");
                JOptionPane.showMessageDialog(this, msg.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dbConnection.updateDepartment(oldId, newId, name, phone, fax);
            tableModel.setValueAt(newId, row, 0);
            tableModel.setValueAt(name, row, 1);
            tableModel.setValueAt(phone, row, 2);
            tableModel.setValueAt(fax, row, 3);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un departamento para actualizar.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DepartamentosGUI().setVisible(true));
    }
}
