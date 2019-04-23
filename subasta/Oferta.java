package subasta;

/**
 * Information about history of offers for each item:
 * - Nickname of client who made the offer
 * - Product key
 * - Date and time og the offer
 * - Amount offered
 */

public class Oferta {

    Cliente comprador;
    Producto producto;

    public Oferta(Cliente c, Producto p) {

        comprador = c;
        producto = p;

    }
}
