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

public class Product implements Serializable {

    String seller;
    String lastBidder;
    String name;
    String description;
    float firstPrice;
    float currentPrice;
    LocalDateTime closingDate;
    private boolean isActive;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public Product(String v, String p, String d, float pi, LocalDateTime fc ) {

        seller = v;
        lastBidder = v;
        name = p;
        description = d;
        firstPrice = pi;
        currentPrice = pi;
        closingDate = fc;
        isActive = true;

    }

    public boolean actualizaPrecio( float monto, String user ) {

        if( monto > currentPrice) {
            currentPrice = monto;
            lastBidder = user;
            return true;
        } else
            return false;
    }

    public String getNombreProducto() {

        return name;
    }

    public float getCurrentPrice() {

        return currentPrice;
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
                name,
                currentPrice,
                lastBidder,
                closingDate.format(FORMATTER)
        );
    }
}
