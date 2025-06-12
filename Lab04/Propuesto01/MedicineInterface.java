package Lab04.Propuesto01;

import java.rmi.Remote;

/*
 * Define los métodos remotos que deben ser implementados por una clase que representa una medicina.
 * Permite obtener una instancia de la medicina con cierta cantidad, consultar el stock disponible y obtener una representación en texto del producto.
 */

public interface MedicineInterface extends Remote {
    public Medicine getMedicine(int amount) throws Exception;

    public int getStock() throws Exception;

    public String print() throws Exception;
}
