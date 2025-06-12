package Lab04.Propuesto02;

import java.rmi.Naming;

// Servidor RMI para el sistema de gestión de tarjetas

public class SistemaTarjetasServer {
    public SistemaTarjetasServer() {
        try {
            // Instancia e implementación del objeto remoto
            SistemaTarjetas t = new SistemaTarjetasImpl();

            // Registro del objeto remoto en el registry RMI con el nombre "SistemaTarjetasService"
            Naming.rebind("rmi://localhost:1099/SistemaTarjetasService", t);
        } catch (Exception e) {
            // Captura de cualquier error en la creación o registro del servicio
            System.out.println("Trouble: " + e);
        }
    }

    public static void main(String args[]) {
        // Inicializa el servidor
        new SistemaTarjetasServer();
    }
}
