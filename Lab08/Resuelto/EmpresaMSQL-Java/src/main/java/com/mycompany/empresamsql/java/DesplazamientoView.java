package com.mycompany.empresamsql.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DesplazamientoView extends JFrame {

    private final JTextField txtIdCategoria, txtNombre, txtDescripcion;
    private final JButton btnPrimero, btnAnterior, btnSiguiente, btnUltimo;

    private Connection conexion;
    private Statement sentencia;
    private ResultSet resultados;

    public DesplazamientoView(String titulo) {
        super(titulo);

        // Campos de texto
        txtIdCategoria = new JTextField();
        txtNombre = new JTextField();
        txtDescripcion = new JTextField();

        txtIdCategoria.setEditable(false);
        txtNombre.setEditable(false);
        txtDescripcion.setEditable(false);

        // Layout de formulario con GridBagLayout
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCampos.add(new JLabel("ID Categoría:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panelCampos.add(txtIdCategoria, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        panelCampos.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panelCampos.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        panelCampos.add(new JLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panelCampos.add(txtDescripcion, gbc);

        // Botones
        btnPrimero = new JButton("Primero");
        btnAnterior = new JButton("Anterior");
        btnSiguiente = new JButton("Siguiente");
        btnUltimo = new JButton("Último");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.add(btnPrimero);
        panelBotones.add(btnAnterior);
        panelBotones.add(btnSiguiente);
        panelBotones.add(btnUltimo);

        // Layout principal
        setLayout(new BorderLayout(10, 10));
        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        setSize(500, 220);
        setLocationRelativeTo(null);

        // Eventos
        btnPrimero.addActionListener(e -> moverPrimero());
        btnAnterior.addActionListener(e -> moverAnterior());
        btnSiguiente.addActionListener(e -> moverSiguiente());
        btnUltimo.addActionListener(e -> moverUltimo());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarConexion();
                dispose();
            }
        });

        conectarBD();
        cargarDatos();
    }

    private void conectarBD() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/EmpresaMSQL", "root", "admin"
            );
            sentencia = conexion.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            );
        } catch (Exception e) {
            mostrarError("Error al conectar con la base de datos:\n" + e.getMessage());
        }
    }

    private void cargarDatos() {
        try {
            resultados = sentencia.executeQuery("SELECT * FROM categorias");
            if (resultados.next()) {
                mostrarRegistro();
            }
        } catch (SQLException e) {
            mostrarError("Error al obtener datos:\n" + e.getMessage());
        }
    }

    private void mostrarRegistro() {
        try {
            txtIdCategoria.setText(resultados.getString("idcategoria"));
            txtNombre.setText(resultados.getString("nombre"));
            txtDescripcion.setText(resultados.getString("descripcion"));
        } catch (SQLException e) {
            mostrarError("Error al mostrar registro:\n" + e.getMessage());
        }
    }

    private void moverPrimero() {
        try {
            if (resultados.first()) mostrarRegistro();
        } catch (SQLException e) {
            mostrarError(e.getMessage());
        }
    }

    private void moverAnterior() {
        try {
            if (!resultados.isFirst() && resultados.previous()) mostrarRegistro();
        } catch (SQLException e) {
            mostrarError(e.getMessage());
        }
    }

    private void moverSiguiente() {
        try {
            if (!resultados.isLast() && resultados.next()) mostrarRegistro();
        } catch (SQLException e) {
            mostrarError(e.getMessage());
        }
    }

    private void moverUltimo() {
        try {
            if (resultados.last()) mostrarRegistro();
        } catch (SQLException e) {
            mostrarError(e.getMessage());
        }
    }

    private void cerrarConexion() {
        try {
            if (resultados != null) resultados.close();
            if (sentencia != null) sentencia.close();
            if (conexion != null) conexion.close();
        } catch (SQLException ignored) {}
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
