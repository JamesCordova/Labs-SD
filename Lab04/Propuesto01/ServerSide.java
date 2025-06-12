package Lab04.Propuesto01;

import java.rmi.*;

/*
 * Representa el servidor en el sistema distribuido de farmacia.
 * Crea una instancia del objeto remoto Stock, le agrega medicinas al inventario,
 * y lo registra en el registro RMI bajo el nombre "PHARMACY" para que los clientes puedan acceder a él.
 */

public class ServerSide {
    public static void main(String[] args) throws Exception {
        // Crea una instancia del objeto remoto que gestiona el inventario
        Stock pharmacy = new Stock();

        // Agregan medicinas iniciales al inventario
        pharmacy.addMedicine("Paracetamol", 3.2f, 10);
        pharmacy.addMedicine("Mejoral", 2.0f, 20);
        pharmacy.addMedicine("Amoxilina", 1.0f, 30);
        pharmacy.addMedicine("Aspirina", 5.0f, 40);

        // Registra el objeto remoto en el RMI Registry con el nombre "PHARMACY"
        Naming.rebind("PHARMACY", pharmacy);

        System.out.println("Server ready"); // Confirmación de que el servidor está listo
    }
}
