package Lab04.Propuesto01;

/*
 * Define una excepción personalizada que se lanza cuando ocurren errores relacionados con el stock,
 * como intentos de comprar una cantidad mayor a la disponible o cuando no hay stock.
 */

public class StockException extends Exception {
    public StockException(String msg) {
        super(msg);
    }
}
