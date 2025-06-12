package com.mycompany.empresamsql.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;


class EjecutarSQLView extends JFrame {

    private final JTextField txtSql;
    private final JTextArea areaResultados;
    private final JButton btnConsulta;

    public EjecutarSQLView(String titulo) {
        super(titulo);
        

        txtSql = new JTextField("SELECT * FROM productos", 50);
        areaResultados = new JTextArea(15, 50);
        areaResultados.setEditable(false);
        btnConsulta = new JButton("Ejecutar SQL");

        JScrollPane scrollPanel = new JScrollPane(areaResultados);

        btnConsulta.addActionListener(this::ejecutarConsulta);

        JPanel panelInput = new JPanel();
        panelInput.setLayout(new BorderLayout(10, 10));
        panelInput.add(new JLabel("Consulta SQL:"), BorderLayout.WEST);
        panelInput.add(txtSql, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnConsulta);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.add(panelInput, BorderLayout.NORTH);
        panelPrincipal.add(scrollPanel, BorderLayout.CENTER);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        add(panelPrincipal);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Cerrando aplicación...");
            }
        });
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
            // this.setVisible(true);
    }

    private void ejecutarConsulta(ActionEvent e) {
        String sql = txtSql.getText().trim();
        if (sql.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingresa una consulta SQL válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Cargar el driver de MySQL (versión moderna)
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/EmpresaMSQL", "root", "admin");
                 Statement sentencia = conexion.createStatement()) {

                boolean tieneResultados = sentencia.execute(sql);

                if (tieneResultados) {
                    try (ResultSet resultado = sentencia.getResultSet()) {
                        mostrarResultados(resultado);
                    }
                } else {
                    int updateCount = sentencia.getUpdateCount();
                    areaResultados.setText("Consulta ejecutada correctamente.\nFilas afectadas: " + updateCount);
                }

            }
        } catch (ClassNotFoundException ex) {
            areaResultados.setText("Error: No se encontró el controlador JDBC.\n" + ex.getMessage());
        } catch (SQLException ex) {
            areaResultados.setText("Error de SQL:\n" + ex.getMessage());
        }
    }

    private void mostrarResultados(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int columnas = meta.getColumnCount();
        StringBuilder sb = new StringBuilder();

        // Nombres de columnas
        for (int i = 1; i <= columnas; i++) {
            sb.append(meta.getColumnName(i)).append("\t");
        }
        sb.append("\n");

        // Filas
        while (rs.next()) {
            for (int i = 1; i <= columnas; i++) {
                sb.append(rs.getString(i)).append("\t");
            }
            sb.append("\n");
        }

        areaResultados.setText(sb.toString());
    }
}
