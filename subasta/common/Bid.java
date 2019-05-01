package subasta.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Information about history of offers for each item:
 * - Nickname of client who made the offer
 * - Product key
 * - Date and time of the offer
 * - Amount offered
 */

public class Bid {

    private String buyer;
    private String product;
    private float price;
    private LocalDateTime date;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public Bid(String c, String p, float pr) {

        buyer = c;
        product = p;
        price = pr;
        date = LocalDateTime.now();

    }

    public String getProduct() {
        return product;
    }

    public float getPrice() {
        return price;
    }

    public String toString() {
        return String.format("%s, %.2f, %s %s",
                product,
                price,
                buyer,
                date.format(FORMATTER) );
    }

}
