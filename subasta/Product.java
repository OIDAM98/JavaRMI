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

    private String seller;
    private String lastBidder;
    private String name;
    private String description;
    private float initialPrice;
    private float currentPrice;
    private LocalDateTime closingDate;
    private boolean isActive;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /*
    Creates a new product with the seller, the name, the description, the price and its closing date provided
     */
    public Product(String s, String n, String d, float price, LocalDateTime cd ) {

        seller = s;
        lastBidder = s;
        name = n;
        description = d;
        initialPrice = price;
        currentPrice = price;
        closingDate = cd;
        isActive = true;

    }

    /*
    Updates the price of the product.
    - Checks if the new price is greater than the current price.
        - If true, updates the price and who made the last bid, and return true
        - Else, return false
     */
    public boolean updatePrice(float amount, String user ) {

        if( amount > currentPrice) { //If new price is greater than current one
            //Update current price and last bidder
            currentPrice = amount;
            lastBidder = user;
            return true;
        } else
            return false;
    }

    /*
    Returns this product's seller
     */
    public String getSeller() {
        return seller;
    }

    /*
    Returns this product's last bidder
     */
    public String getLastBidder() {
        return lastBidder;
    }

    /*
    Returns this product's name
     */
    public String getName() {
        return name;
    }

    /*
    Returns this product's description
     */
    public String getDescription() {
        return description;
    }

    /*
    Returns this product's initial price
     */
    public float getInitialPrice() {
        return initialPrice;
    }

    /*
    Returns this product's current price
     */
    public float getCurrentPrice() {
        return currentPrice;
    }

    public LocalDateTime getClosingDate() {
        return closingDate;
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
