package Lab04.Propuesto02;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

public class SistemaTarjetasGUI {
    private SistemaTarjetas sistemaTarjetas;

    public SistemaTarjetasGUI() {
        try {
            sistemaTarjetas = (SistemaTarjetas) Naming.lookup("rmi://localhost/SistemaTarjetasService");
            createAndShowGUI();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to RMI service: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Sistema de Tarjetas de Crédito");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new GridLayout(6, 1));
        frame.add(panel);

        JButton btnConsultarSaldo = new JButton("Consultar Saldo");
        JButton btnAgregarSaldo = new JButton("Agregar Saldo");
        JButton btnRestarSaldo = new JButton("Restar Saldo");
        JButton btnAgregarCliente = new JButton("Agregar Cliente");
        JButton btnAgregarTarjeta = new JButton("Agregar Tarjeta");
        JButton btnSalir = new JButton("Salir");

        panel.add(btnConsultarSaldo);
        panel.add(btnAgregarSaldo);
        panel.add(btnRestarSaldo);
        panel.add(btnAgregarCliente);
        panel.add(btnAgregarTarjeta);
        panel.add(btnSalir);

        btnConsultarSaldo.addActionListener(e -> consultarSaldo());
        btnAgregarSaldo.addActionListener(e -> agregarSaldo());
        btnRestarSaldo.addActionListener(e -> restarSaldo());
        btnAgregarCliente.addActionListener(e -> agregarCliente());
        btnAgregarTarjeta.addActionListener(e -> agregarTarjeta());
        btnSalir.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    private void consultarSaldo() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField txtDNI = new JTextField();
        JTextField txtNumeroTarjeta = new JTextField();

        panel.add(new JLabel("DNI:"));
        panel.add(txtDNI);
        panel.add(new JLabel("Número de Tarjeta:"));
        panel.add(txtNumeroTarjeta);

        int result = JOptionPane.showConfirmDialog(null, panel, "Consultar Saldo", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String dni = txtDNI.getText();
            int numeroTarjeta = Integer.parseInt(txtNumeroTarjeta.getText());
            try {
                BigDecimal saldo = sistemaTarjetas.getSaldo(dni, numeroTarjeta);
                JOptionPane.showMessageDialog(null, "El saldo actual es: " + saldo);
            } catch (RemoteException re) {
                JOptionPane.showMessageDialog(null, "Error: " + re.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void agregarSaldo() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField txtDNI = new JTextField();
        JTextField txtNumeroTarjeta = new JTextField();
        JTextField txtSaldo = new JTextField();

        panel.add(new JLabel("DNI:"));
        panel.add(txtDNI);
        panel.add(new JLabel("Número de Tarjeta:"));
        panel.add(txtNumeroTarjeta);
        panel.add(new JLabel("Saldo a Agregar:"));
        panel.add(txtSaldo);

        int result = JOptionPane.showConfirmDialog(null, panel, "Agregar Saldo", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String dni = txtDNI.getText();
            int numeroTarjeta = Integer.parseInt(txtNumeroTarjeta.getText());
            BigDecimal saldo = new BigDecimal(txtSaldo.getText());
            try {
                BigDecimal nuevoSaldo = sistemaTarjetas.addSaldo(dni, numeroTarjeta, saldo);
                JOptionPane.showMessageDialog(null, "Saldo después de agregar: " + nuevoSaldo);
            } catch (RemoteException re) {
                JOptionPane.showMessageDialog(null, "Error: " + re.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void restarSaldo() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField txtDNI = new JTextField();
        JTextField txtNumeroTarjeta = new JTextField();
        JTextField txtSaldo = new JTextField();

        panel.add(new JLabel("DNI:"));
        panel.add(txtDNI);
        panel.add(new JLabel("Número de Tarjeta:"));
        panel.add(txtNumeroTarjeta);
        panel.add(new JLabel("Saldo a Restar:"));
        panel.add(txtSaldo);

        int result = JOptionPane.showConfirmDialog(null, panel, "Restar Saldo", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String dni = txtDNI.getText();
            int numeroTarjeta = Integer.parseInt(txtNumeroTarjeta.getText());
            BigDecimal saldo = new BigDecimal(txtSaldo.getText());
            try {
                BigDecimal nuevoSaldo = sistemaTarjetas.subSaldo(dni, numeroTarjeta, saldo);
                JOptionPane.showMessageDialog(null, "Saldo después de restar: " + nuevoSaldo);
            } catch (RemoteException re) {
                JOptionPane.showMessageDialog(null, "Error: " + re.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void agregarCliente() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JTextField txtNombre = new JTextField();
        JTextField txtApellido = new JTextField();
        JTextField txtDNI = new JTextField();

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Apellido:"));
        panel.add(txtApellido);
        panel.add(new JLabel("DNI:"));
        panel.add(txtDNI);

        int result = JOptionPane.showConfirmDialog(null, panel, "Agregar Cliente", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            String dni = txtDNI.getText();
            try {
                sistemaTarjetas.agregarCliente(nombre, apellido, dni);
                JOptionPane.showMessageDialog(null, "Cliente agregado con éxito.");
            } catch (RemoteException re) {
                JOptionPane.showMessageDialog(null, "Error: " + re.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void agregarTarjeta() {
        JPanel panel = new JPanel(new GridLayout(7, 2));
        JTextField txtDNI = new JTextField();
        JTextField txtTipo = new JTextField();
        JTextField txtFechaVencimiento = new JTextField();
        JTextField txtCVV = new JTextField();
        JTextField txtNombreTitular = new JTextField();
        JTextField txtSaldoInicial = new JTextField();

        panel.add(new JLabel("DNI del Titular:"));
        panel.add(txtDNI);
        panel.add(new JLabel("Tipo de Tarjeta (CREDITO/DEBITO):"));
        panel.add(txtTipo);
        panel.add(new JLabel("Fecha de Vencimiento (MM/YYYY):"));
        panel.add(txtFechaVencimiento);
        panel.add(new JLabel("CVV:"));
        panel.add(txtCVV);
        panel.add(new JLabel("Nombre del Titular:"));
        panel.add(txtNombreTitular);
        panel.add(new JLabel("Saldo Inicial:"));
        panel.add(txtSaldoInicial);

        int result = JOptionPane.showConfirmDialog(null, panel, "Agregar Tarjeta", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String dni = txtDNI.getText();
            String tipo = txtTipo.getText().toUpperCase();
            String fechaVencimiento = txtFechaVencimiento.getText();
            String cvv = txtCVV.getText();
            String nombreTitular = txtNombreTitular.getText();
            BigDecimal saldoInicial = new BigDecimal(txtSaldoInicial.getText());
            try {
                sistemaTarjetas.agregarTarjeta(dni, TipoTarjeta.valueOf(tipo), fechaVencimiento, cvv, nombreTitular,
                        saldoInicial);
                JOptionPane.showMessageDialog(null, "Tarjeta agregada con éxito.");
            } catch (RemoteException re) {
                JOptionPane.showMessageDialog(null, "Error: " + re.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException iae) {
                JOptionPane.showMessageDialog(null, "Tipo de tarjeta inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SistemaTarjetasGUI::new);
    }
}