package subasta;

/**
 * Information about products on sale:
 * - Name of product (also serves as key)
 * - Small description
 * - Initial price
 * - Date and time when bid closes.
 */

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Producto implements Serializable {

    String vendedor;
    String vendedorActual;
    String producto;
    String descripcion;
    float precioInicial;
    float precioActual;
    LocalDateTime fechaCierre;
    private boolean isActive;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public Producto(String v, String p, String d, float pi, LocalDateTime fc ) {

        vendedor = v;
        vendedorActual = v;
        producto = p;
        descripcion = d;
        precioInicial = pi;
        precioActual = pi;
        fechaCierre = fc;
        isActive = true;

    }

    public boolean actualizaPrecio( float monto, String user ) {

        if( monto > precioActual ) {
            precioActual = monto;
            vendedorActual = user;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean status) {
        this.isActive = status;
    }

    @Override
    public String toString() {
        return String.format("%-10s Precio: %-10.2f Por: %-10s Fecha limite: %s",
                producto,
                precioActual,
                vendedor,
                fechaCierre.format(FORMATTER)
        );
    }
}
