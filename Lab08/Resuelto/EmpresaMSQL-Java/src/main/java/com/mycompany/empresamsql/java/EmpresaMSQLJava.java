package com.mycompany.empresamsql.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EmpresaMSQLJava extends JFrame {

    public EmpresaMSQLJava() {
        super("Sistema de Gestión - Menú Principal");

        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnEjecutarSQL = new JButton("Ejecutar SQL");
        JButton btnDesplazarRegistros = new JButton("Desplazamiento Registros");
        JButton btnABM = new JButton("ABM (Insertar/Actualizar/Eliminar)");

        btnEjecutarSQL.addActionListener(e -> {
            new EjecutarSQLView("Ejecutar SQL").setVisible(true);
        });

        btnDesplazarRegistros.addActionListener(e -> {
            new DesplazamientoView("Desplazamiento de Registros").setVisible(true);
        });

        btnABM.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new CRUDView("Gestor de Categorías").setVisible(true);
            });
            
        });

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(btnEjecutarSQL);
        panel.add(btnDesplazarRegistros);
        panel.add(btnABM);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EmpresaMSQLJava().setVisible(true);
        });
    }
}
