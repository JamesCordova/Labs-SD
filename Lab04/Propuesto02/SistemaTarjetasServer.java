package Lab04.Propuesto02;

import java.rmi.Naming;

public class SistemaTarjetasServer {
    public SistemaTarjetasServer() {
        try {
            SistemaTarjetas t = new SistemaTarjetasImpl();

            Naming.rebind("rmi://localhost:1099/SistemaTarjetasService", t);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    public static void main(String args[]) {
        new SistemaTarjetasServer();
    }
}