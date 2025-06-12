package com.mycompany.empresamsql.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CRUDView extends JFrame {

    private final JTextField txtId, txtNombre, txtDescripcion;
    private final JButton btnInsertar, btnModificar, btnEliminar, btnActualizar;
    private final JButton btnPrimero, btnAnterior, btnSiguiente, btnUltimo;

    private Connection conexion;
    private Statement sentencia;
    private ResultSet resultado;

    public CRUDView(String titulo) {
        super(titulo);

        // Componentes
        txtId = new JTextField();
        txtNombre = new JTextField();
        txtDescripcion = new JTextField();

        txtId.setEditable(false);

        btnInsertar = new JButton("Insertar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Actualizar");

        btnPrimero = new JButton("Primero");
        btnAnterior = new JButton("Anterior");
        btnSiguiente = new JButton("Siguiente");
        btnUltimo = new JButton("Último");

        // Layout formulario
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelCampos.add(new JLabel("ID Categoría:"), gbc);
        gbc.gridx = 1;
        panelCampos.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panelCampos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        panelCampos.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panelCampos.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        panelCampos.add(txtDescripcion, gbc);

        // Panel botones CRUD
        JPanel panelCRUD = new JPanel(new FlowLayout());
        panelCRUD.add(btnInsertar);
        panelCRUD.add(btnModificar);
        panelCRUD.add(btnEliminar);
        panelCRUD.add(btnActualizar);

        // Panel botones navegación
        JPanel panelNav = new JPanel(new FlowLayout());
        panelNav.add(btnPrimero);
        panelNav.add(btnAnterior);
        panelNav.add(btnSiguiente);
        panelNav.add(btnUltimo);

        // Layout principal
        setLayout(new BorderLayout());
        add(panelCampos, BorderLayout.CENTER);
        add(panelCRUD, BorderLayout.NORTH);
        add(panelNav, BorderLayout.SOUTH);

        setSize(600, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Eventos
        btnInsertar.addActionListener(e -> insertar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());
        btnActualizar.addActionListener(e -> actualizar());

        btnPrimero.addActionListener(e -> irPrimero());
        btnAnterior.addActionListener(e -> irAnterior());
        btnSiguiente.addActionListener(e -> irSiguiente());
        btnUltimo.addActionListener(e -> irUltimo());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarConexion();
            }
        });

        // BD
        conectar();
        cargarDatos();
    }

    private void conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/EmpresaMSQL", "root", "admin"
            );
            sentencia = conexion.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE
            );
        } catch (Exception e) {
            mostrarError("Error de conexión: " + e.getMessage());
        }
    }

    private void cargarDatos() {
        try {
            resultado = sentencia.executeQuery("SELECT * FROM categorias");
            if (resultado.next()) {
                mostrarActual();
            } else {
                limpiarCampos();
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar datos: " + e.getMessage());
        }
    }

    private void mostrarActual() {
        try {
            if (resultado != null && !resultado.isAfterLast() && !resultado.isBeforeFirst()) {
                txtId.setText(resultado.getString("idcategoria"));
                txtNombre.setText(resultado.getString("nombre"));
                txtDescripcion.setText(resultado.getString("descripcion"));
            }
        } catch (SQLException e) {
            mostrarError("Error al mostrar datos: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
    }

    private void irPrimero() {
        try {
            if (resultado.first()) mostrarActual();
        } catch (SQLException e) {
            mostrarError(e.getMessage());
        }
    }

    private void irAnterior() {
        try {
            if (!resultado.isFirst() && resultado.previous()) mostrarActual();
        } catch (SQLException e) {
            mostrarError(e.getMessage());
        }
    }

    private void irSiguiente() {
        try {
            if (!resultado.isLast() && resultado.next()) mostrarActual();
        } catch (SQLException e) {
            mostrarError(e.getMessage());
        }
    }

    private void irUltimo() {
        try {
            if (resultado.last()) mostrarActual();
        } catch (SQLException e) {
            mostrarError(e.getMessage());
        }
    }

    private void insertar() {
        String nombre = txtNombre.getText();
        String descripcion = txtDescripcion.getText();

        if (nombre.isEmpty() || descripcion.isEmpty()) {
            mostrarError("Nombre y Descripción no pueden estar vacíos.");
            return;
        }

        try (PreparedStatement ps = conexion.prepareStatement(
                "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)")) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.executeUpdate();
            actualizar();
            JOptionPane.showMessageDialog(this, "Registro insertado.");
        } catch (SQLException e) {
            mostrarError("Error al insertar: " + e.getMessage());
        }
    }

    private void modificar() {
        String id = txtId.getText();
        String nombre = txtNombre.getText();
        String descripcion = txtDescripcion.getText();

        if (id.isEmpty()) {
            mostrarError("Seleccione un registro para modificar.");
            return;
        }

        try (PreparedStatement ps = conexion.prepareStatement(
                "UPDATE categorias SET nombre = ?, descripcion = ? WHERE idcategoria = ?")) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setInt(3, Integer.parseInt(id));
            ps.executeUpdate();
            actualizar();
            JOptionPane.showMessageDialog(this, "Registro modificado.");
        } catch (SQLException e) {
            mostrarError("Error al modificar: " + e.getMessage());
        }
    }

    private void eliminar() {
        String id = txtId.getText();
        if (id.isEmpty()) {
            mostrarError("Seleccione un registro para eliminar.");
            return;
        }

        try (PreparedStatement ps = conexion.prepareStatement(
                "DELETE FROM categorias WHERE idcategoria = ?")) {
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
            actualizar();
            JOptionPane.showMessageDialog(this, "Registro eliminado.");
        } catch (SQLException e) {
            mostrarError("Error al eliminar: " + e.getMessage());
        }
    }

    private void actualizar() {
        cargarDatos(); // Vuelve a hacer el query y se posiciona en el primero
    }

    private void cerrarConexion() {
        try {
            if (resultado != null) resultado.close();
            if (sentencia != null) sentencia.close();
            if (conexion != null) conexion.close();
        } catch (SQLException ignored) {}
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
