/**
 * Information about history of offers for each item:
 * - Nickname of client who made the offer
 * - Product key
 * - Date and time og the offer
 * - Amount offered
 */

import java.util.Date;

public class Historico {

    String comprador;
    String producto;
    float monto;
    Date fecha;

    public Historico(String c, String p, Date d, float m ) {

        comprador = c;
        producto = p;
        fecha = d;
        monto = m;

   }
}
