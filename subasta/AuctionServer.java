package subasta;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class AuctionServer implements Server {

    Hashtable<String, User> users; //Hashtable of users
    Hashtable<String, Product> products; //Hashtable of products
    List<Bid> bidHistory; //List of bids
    List<Controller> clients; //List of controllers

    /*
    Constructs a new server
    - Initializes:
        - Users hashtable
        - Products hashtable
        - List of bid history
        - List of controllers of clients
     */
    public AuctionServer() {

        users = new Hashtable<>();
        products = new Hashtable<>();
        bidHistory = new ArrayList<>();
        clients = new ArrayList<>();

    }

    /*
    Updates all the clients.
    Method is called when a change in the interface has to be propagated to all clients.
    - For each controller in clients:
        - Update View of the Controller
     */
    public void updateClients() {
        Controller temp = null; //Temporal Controller if there's an error
        try {
            //For each controller in clients
            for(Controller c : clients) {
                c.updateView(); //Update View of Client
                temp = c; //Modify temp with current Controller
            }
        }
        catch (RemoteException ex) { //Print Controller that had problem
            System.out.println("Hubo un error al actualizar cliente " + temp);
            //Unsubscribe Controller with problem
            this.unsubscribe(temp);
        }

    }

    /*
    Method to subscribe a desired client through its Controller.
    - Adds desired Controller to the list of clients.
     */
    public void subscribe(Controller c) {
        clients.add(c); //Add Controller to list of controllers
        System.out.println("Subscribed!");
        System.out.println(clients);
        updateClients(); //Update all Controllers
    }

    /*
    Method to unsubscribe a desired client through its Controller.
    - Removes desired Controller to the list of clients.
     */
    public void unsubscribe(Controller c) {
        clients.remove(c); //Remove Controller of the list of controllers
        System.out.println("Client disconnected!");
    }

    /*
    Registers user and client to this session.
    - Returns true if the user is added correctly.
    - Returns false if the user was already added before.
     */
    public boolean registerUser(String name, User client) {

        if (!users.containsKey(name)) { //If the user doesn't exist during the session

            System.out.println("Adding new user: " + name);
            users.put(name, client); //Add user to session
            return true;

        } else

            return false;
    }

    /*
    Checks whether or nor the user exists on the current session.
    - Returns true if it was already added.
    - Returns false if it doesn't exist.
     */
    public boolean userExists(String number) {
        return users.containsKey(number);
    }

    /*
    Adds a product to the current auction.
    - Checks if the product was/is already on the sale.
    - If it's a new product:
        - Saves the product on the current session.
        - Gets the difference in seconds between now and the date of the product to sell.
        - Sets a new Timer that starts at the difference in seconds calculated before.
            -
        - Updates clients of new product.
        - Returns true because it was possible to add new product.
    - Else, returns false because it's not possible to add it again.
     */
    public boolean addProductToAuction(String user, String product, Product offer) {
        //If product doesn't exist on session
        if (!products.containsKey(product)) {

            System.out.println("Adding product: " + product);
            products.put(product, offer); //Add product to the hashtable
            bidHistory.add(new Bid(user, product, offer.getInitialPrice())); //Add new offer to bid history

            ZoneId znid = ZoneId.systemDefault(); //Gets default Time Zone of the server
            LocalDateTime end = offer.getClosingDate(); //Gets closing date of the product
            Instant endTime = end.atZone(znid).toInstant(); //Convert closing date to the default timezone
            long startTime = Instant.now().toEpochMilli(); //Gets current DateTime in milliseconds
            //Calculate difference between closing date of product and current date in milliseconds
            long durationSecond = endTime.minusMillis(startTime).toEpochMilli();

            Timer timer = new Timer(); //Create new Timer
            //Create new task for the timer to perform when closing date arrives:
            TimerTask task = new TimerTask() {
                @Override
                public void run() {

                    System.out.println("Auction of " + product + " ended!");
                    //Set the product as no longer active
                    offer.setActive(false);
                    //Notify the seller of the product who won the auction
                    notifyBid(offer.getSeller(), offer);
                    System.out.println("Updating clients!");
                    //Update all clients of the change made
                    updateClients();
                    System.out.println("Updated!");
                    //Cancel and terminate all tasks on the timer
                    //To ensure nothing was left begin
                    timer.cancel();
                    timer.purge();

                }
            };

            timer.schedule(task, durationSecond, 1000); //Schedule task on timer

            this.updateClients(); //Updates Controllers because of new product
            return true;

        } else
            return false;
    }

    /*
    Checks whether or nor the product exists on the current session.
    - Returns true if it was already added.
    - Returns false if it doesn't exist.
     */
    public boolean productExists(String product) {
        return products.containsKey(product);
    }

    /*
    Adds an offer to a product, adds this offer to the history.
    - Gets the product from the hashtable based on the product name that was provided.
    - Gets the client from the hashtable based on the clients name that was provided.
    - If the current price can be updated to the amount provided:
         - Adds the bid to the history.
         - Updates all clients with change made.
         - Returns true.
    - Else, returns false.
     */
    public boolean addOffer(String buyer, String product, float amount) {

        if (products.containsKey(product)) { //If product is currently in the session

            Product infoProd = products.get(product); //Get the product based on the specified name
            User user = users.get(buyer); //Get the user based on the specified name

            if (infoProd.updatePrice(amount, user.name)) { //If price of product can be updated with bid

                bidHistory.add(new Bid(user.name, infoProd.getName(), infoProd.getCurrentPrice())); //Adds to history about new change made
                this.updateClients(); //Updates all clients about the update done.
                return true;

            } else
                return false; //Price couldn't be updated

        } else
            return false; //Product is not on current session

    }

    /*
    Returns the list of products that are currently active.
    - Gets a Collection of all the products from the hashtable of products.
    - Converts the Collection to a Stream in order to use Java 8 Functional Interfaces.
    - Filters the Stream by all products that are active.
    - Converts the Stream to a List.
    - Returns the List of filtered products.
     */
    public List<Product> getCatalog() {

        List<Product> toRet =
                products //Gets all products ( Hashtable<String, Product> )
                .values() //Gets all values of products ( Collection<Product> )
                .stream() //Gets an stream to use Java 8 Lambdas ( Stream<Product> )
                .filter(p -> p.isActive()) //Filter all products that are active ( Stream<Product> )
                .collect(Collectors.toList()); //Collects filtered stream in a list of products ( List<Product> )
        return toRet; //Returns filtered list of products

    }

    /*
    Returns the history of all the bids made during the session, organized by name of product, and then by price.
    - Copies the current unsorted history of bids.
    - Sorts the copy:
        - First sorts the list by product's name.
        - Then sorts the list by amount that was bid.
    - Returns sorted bids.
     */
    public List<Bid> getBidHistory() {
        List<Bid> sorted = new ArrayList<>(bidHistory); //Copy of history of bids
        //Sort the copy made
        sorted.sort(
                Comparator.comparing(Bid::getProduct) //Sort by product's name
                        .thenComparing(Bid::getPrice) //Then, sort by amount bid.
        );
        return sorted; //Returns sorted list
    }

    /*
    Method to be called when the server stops.
    - Save the history of the bids.
    - Unsubscribe any remaining client that hasn't disconnected.
     */
    public void exitApp() {
        saveBids(); //Save the bid history
        unsubscribeRemainingClients(); //Unsubscribe remaining clients.
    }

    /*
    Notifies the desired client that the offer he made has ended and who was the higher bidder.
    - For each Controller in the client list:
        - Check if the user of the Controller is equal to the client to notify (ignoring case).
            - If true, gets the user that made the last bid of the product.
            - Notifies, through this Controller, who was the last bidder of the product.
     */
    private void notifyBid(String notify, Product prod) {
        //For each Controller in clients list
        for(Controller c : clients) {
            try {
                //Compare the user of the current Controller with the name of client to notify
                if(c.getUser().equalsIgnoreCase(notify)) { //If they're equal
                    User client = users.get(prod.getLastBidder()); //Get the client that made the last bid
                    c.notifyEndBid(client, prod); //Notify client of this client.
                }
            }
            catch (RemoteException ex) {
                System.out.println("Error al buscar cliente");
            }
        }
    }

    /*
    Saves the bids made during the session to a file named upon the date and time of session's ending.
    - Prints to console all the history of the session.
    - Saves to a file in folder "historial":
        - Opens a file with the name of current date and time, with CSV extension.
        - Opens a PrintWriter based on the file opened.
        - Prints each bid of the history to this file.
        - Closes both file and PrintWriter.
     */
    private void saveBids() {
        System.out.println("\nBids during the period");
        this.getBidHistory().forEach(System.out::println);
        try{
            FileWriter fw = new FileWriter("historial/" + LocalDateTime.now() + ".csv");
            PrintWriter outFile = new PrintWriter(fw);
            this.getBidHistory().forEach(outFile::println);
            outFile.close();
            fw.close();
        }
        catch (IOException ex) {
            System.out.println("Problema al escribir al archivo");
        }
    }

    /*
    Unsubscribe any remaining client that is left at the ending of the session.
    - If list of clients isn't empty:
        - Copy the remaining clients to a new list.
        - For every client in this copy:
            - Unsubscribe from the session.
     */
    private void unsubscribeRemainingClients() {
        if(!clients.isEmpty()) { //If lit of clients isn't empty
            List<Controller> remains = new ArrayList<>(clients); //Copy remaining clients to new list
            remains.forEach(this::unsubscribe); //For each client => unsubscribe
        }
    }

    public static void main(String... args) {
        try {

            AuctionServer obj = new AuctionServer();
            Server stub = (Server) UnicastRemoteObject.exportObject(obj, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.bind("subasta", stub);

            System.out.println("Server ready");

            Runtime.getRuntime().addShutdownHook(new Thread( obj::exitApp ));

        }
        catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
