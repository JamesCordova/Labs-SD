package Lab04.Propuesto01;

import java.rmi.server.UnicastRemoteObject;

/*
 * Representación de una medicina dentro del sistema distribuido de farmacia.
 * Implementa la interfaz remota MedicineInterface y extiende UnicastRemoteObject para permitir su uso en RMI.
 * Proporciona funcionalidades para consultar stock, obtener una cantidad del producto y mostrar su información.
 */

public class Medicine extends UnicastRemoteObject implements MedicineInterface {
    private String name;
    private float unitPrice;
    private int stock;

    public Medicine() throws Exception {
        super();
    }

    public Medicine(String name, float price, int stock) throws Exception {
        super();
        this.name = name;
        unitPrice = price;
        this.stock = stock;
    }

    @Override
    public Medicine getMedicine(int amount) throws Exception {
        // Verifica si hay stock suficiente antes de realizar la operación
        if (this.stock <= 0)
            throw new StockException("Stock empty");
        if (this.stock - amount < 0)
            throw new StockException("Stock not amount of medicine");

        this.stock -= amount;

        // Retorna una nueva instancia de Medicine con el precio ajustado y stock actual
        Medicine aux = new Medicine(name, unitPrice * amount, stock);
        return aux;
    }

    @Override
    public int getStock() throws Exception {
        // Retorna el stock disponible de esta medicina
        return this.stock;
    }

    public String print() throws Exception {
        // Devuelve una representación en texto del producto
        return this.name + "\nPrice: " + this.unitPrice + "\nStock: " + this.stock;
    }
}
