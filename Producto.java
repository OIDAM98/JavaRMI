/**
 * Information about products on sale:
 * - Name of product (also serves as key)
 * - Small description
 * - Initial price
 * - Date and time when bid closes.
 */

import java.io.Serializable;
import java.util.Date;

public class Producto implements Serializable {

    String vendedor;
    String producto;
    String descripcion;
    float precioInicial;
    float precioActual;
    Date fechaCierre;

    public Producto(String v, String p, String d, float pi, Date fc ) {

        vendedor = v;
        producto = p;
        descripcion = d;
        precioInicial = pi;
        precioActual = pi;
        fechaCierre = fc;

    }

    public boolean actualizaPrecio( float monto ) {

        if( monto > precioActual ) {
            precioActual = monto;
            return true;
        } else
            return false;
    }

    public String getNombreProducto() {

        return producto;
    }

    public float getPrecioActual() {

        return precioActual;
    }
}
