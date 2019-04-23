package subasta;

/**
 * Information about products on sale:
 * - Name of product (also serves as key)
 * - Small description
 * - Initial price
 * - Date and time when bid closes.
 */

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Producto {

    String vendedor;
    String producto;
    String descripcion;
    float precioInicial;
    float precioActual;
    LocalDateTime fechaCierre;
    private static final String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMATTER);


    public Producto(String v, String p, String d, float pi, LocalDateTime fc ) {

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

    @Override
    public String toString() {
        return String.format("%s %.2f %s", producto, precioActual, fechaCierre.format(FORMATTER) );
    }
}
