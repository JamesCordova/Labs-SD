package Lab04.Propuesto02;

import java.math.BigDecimal;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.util.Scanner;

public class SistemaTarjetasClient {
    public static void main(String[] args) {
        try {
            SistemaTarjetas s = (SistemaTarjetas) Naming.lookup("rmi://localhost/SistemaTarjetasService");
            Scanner scanner = new Scanner(System.in);

            boolean exit = false;
            while (!exit) {
                System.out.println("Seleccione una opción:");
                System.out.println("1. Ver saldo");
                System.out.println("2. Agregar saldo");
                System.out.println("3. Restar saldo");
                System.out.println("4. Agregar cliente");
                System.out.println("5. Agregar tarjeta a cliente");
                System.out.println("6. Salir");

                int opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer de entrada

                switch (opcion) {
                    case 1:
                        System.out.println("Ingrese DNI:");
                        String dniSaldo = scanner.nextLine();
                        System.out.println("Ingrese número de tarjeta:");
                        int numeroTarjetaSaldo = scanner.nextInt();
                        scanner.nextLine(); // Limpiar el buffer de entrada
                        try {
                            System.out.println("El saldo actual de la tarjeta " + numeroTarjetaSaldo + " es: "
                                    + s.getSaldo(dniSaldo, numeroTarjetaSaldo));
                        } catch (RemoteException re) {
                            System.out.println("Error: " + re.getMessage());
                        }
                        break;
                    case 2:
                        System.out.println("Ingrese DNI:");
                        String dniAgregarSaldo = scanner.nextLine();
                        System.out.println("Ingrese número de tarjeta:");
                        int numeroTarjetaAgregarSaldo = scanner.nextInt();
                        System.out.println("Ingrese saldo a agregar:");
                        BigDecimal saldoAgregar = scanner.nextBigDecimal();
                        scanner.nextLine(); // Limpiar el buffer de entrada
                        try {
                            System.out.println("Saldo después de agregar: "
                                    + s.addSaldo(dniAgregarSaldo, numeroTarjetaAgregarSaldo, saldoAgregar));
                        } catch (RemoteException re) {
                            System.out.println("Error: " + re.getMessage());
                        }
                        break;
                    case 3:
                        System.out.println("Ingrese DNI:");
                        String dniRestarSaldo = scanner.nextLine();
                        System.out.println("Ingrese número de tarjeta:");
                        int numeroTarjetaRestarSaldo = scanner.nextInt();
                        System.out.println("Ingrese saldo a restar:");
                        BigDecimal saldoRestar = scanner.nextBigDecimal();
                        scanner.nextLine(); // Limpiar el buffer de entrada
                        try {
                            System.out.println("Saldo después de restar: "
                                    + s.subSaldo(dniRestarSaldo, numeroTarjetaRestarSaldo, saldoRestar));
                        } catch (RemoteException re) {
                            System.out.println("Error: " + re.getMessage());
                        }
                        break;
                    case 4:
                        System.out.println("Ingrese nombre:");
                        String nombre = scanner.nextLine();
                        System.out.println("Ingrese apellido:");
                        String apellido = scanner.nextLine();
                        System.out.println("Ingrese DNI:");
                        String dniNuevo = scanner.nextLine();
                        try {
                            s.agregarCliente(nombre, apellido, dniNuevo);
                            System.out.println("Cliente agregado con éxito.");
                        } catch (RemoteException re) {
                            System.out.println("Error: " + re.getMessage());
                        }
                        break;
                    case 5:
                        System.out.println("Ingrese DNI del titular de la tarjeta:");
                        String dniTitular = scanner.nextLine();
                        System.out.println("Ingrese tipo de tarjeta (CREDITO/DEBITO):");
                        String tipoTarjeta = scanner.nextLine().toUpperCase();
                        System.out.println("Ingrese fecha de vencimiento (MM/YYYY):");
                        String fechaVencimiento = scanner.nextLine();
                        System.out.println("Ingrese CVV:");
                        String cvv = scanner.nextLine();
                        System.out.println("Ingrese nombre del titular:");
                        String nombreTitular = scanner.nextLine();
                        System.out.println("Ingrese saldo inicial:");
                        BigDecimal saldoInicial = scanner.nextBigDecimal();
                        scanner.nextLine(); // Limpiar el buffer de entrada
                        try {
                            s.agregarTarjeta(dniTitular, TipoTarjeta.valueOf(tipoTarjeta), fechaVencimiento, cvv,
                                    nombreTitular, saldoInicial);
                            System.out.println("Tarjeta agregada con éxito.");
                        } catch (RemoteException re) {
                            System.out.println("Error: " + re.getMessage());
                        } catch (IllegalArgumentException iae) {
                            System.out.println("Tipo de tarjeta inválido.");
                        }
                        break;
                    case 6:
                        exit = true;
                        break;
                    default:
                        System.out.println("Opción no válida");
                        break;
                }
            }

            scanner.close();
        } catch (MalformedURLException murle) {
            System.out.println("MalformedURLException: " + murle);
        } catch (RemoteException re) {
            System.out.println("RemoteException: " + re);
        } catch (NotBoundException nbe) {
            System.out.println("NotBoundException: " + nbe);
        }
    }
}