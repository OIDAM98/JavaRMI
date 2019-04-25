package subasta;

/**
 * Information about history of offers for each item:
 * - Nickname of client who made the offer
 * - Product key
 * - Date and time og the offer
 * - Amount offered
 */

public class Bid {

    private String buyer;
    private String product;
    private float price;

    public Bid(String c, String p, float pr) {

        buyer = c;
        product = p;
        price = pr;

    }

    public String getProduct() {
        return product;
    }

    public float getPrice() {
        return price;
    }

    public String toString() {
        return String.format("%s, %.2f, %s", product, price, buyer);
    }

}
