package subasta;

import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.List;

import javax.swing.*;

public class AuctionController extends UnicastRemoteObject implements Controller {

    Client view; //View
    Server model; //Server
    Hashtable<String, String> listDescription; //List of descriptions

    /*
    Constructor, receives a View:
        - Binds the view received to this Controller.
        - Searches for the server on the Name Register:
            - If found, binds it to this Controller.
            - Else, notifies user of error.
     */
    public AuctionController(Client v) throws RemoteException {
        view = v; //Binds View to Controller
        //Searches for server
        try {
            Registry registry = LocateRegistry.getRegistry(); //Gets registry
            model = (Server) registry.lookup("subasta"); //Searches for server named as subasta.
        }
        //If server is not found
        catch (NotBoundException e) {
            //Notify user of error
            JOptionPane.showMessageDialog(null,
                    "Error al conectarse al servidor!",
                    "Conectar al servidor",
                    JOptionPane.ERROR_MESSAGE);
            //System.exit(1);
        }
    }

    /*
    Connects the user to the server:
    - If user hasn't registered to the session before:
        - Asks for user's information.
        - Registers this user to the server.
        - Subscribes this controller to the server.
        - Notify user about success on operation.
    - Else, this user was registered before:
        - Subscribes this controller to the server.
        - Notifies of return to session was made successfully.
     */
    public void connectUser() {

        try {

            //Retrieve user from View.
            String user = view.getCurrentUser();

            if (!model.userExists(user)) { //If user doesn't exist on server
                System.out.println("Registering user: " + user);

                //Shoe dialog to fill information about new client
                int result = JOptionPane.showConfirmDialog(null,
                        view.getUserPanel(),
                        "Dar de alta un userField",
                        JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        String direction = view.getDirection(); //Get directionField from user
                        String email = view.getEmail(); //Get emailField from user
                        String phone = view.getPhone(); //Get phone number from user
                        String nickname = view.getNickname(); //Get nicknameField from user
                        User newClient = new User(user, direction, email, phone, nickname); //Create new user to be subscribed
                        //Print on console information about new user
                        String printy = String.format("%s %s %s %s %s", user, direction, email, phone, nickname);
                        System.out.println(printy);

                        //If registration of new user was successful
                        if (model.registerUser(user, newClient)) {
                            //Subscribe Controller to server
                            model.subscribe(this);
                            //Show user that connection and registration to server was successful
                            JOptionPane.showMessageDialog(null,
                                    "Cliente dado de alta con éxito!",
                                    "Dar de alta cliente",
                                    JOptionPane.INFORMATION_MESSAGE);
                            //Activate corresponding buttons on the View
                            view.activateButtons();
                        } else { //If there was a problem doing the registration on server side
                            //Show user about error
                            JOptionPane.showMessageDialog(null,
                                    "Hubo un error al dar de alta el cliente.",
                                    "Dar de alta cliente",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    catch (NumberFormatException ex) { //If a number wasn't introduced on telephone area
                        JOptionPane.showMessageDialog(null,
                                "Campo debe de contener solamente numeros",
                                "Dar de alta cliente",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    catch (IllegalArgumentException ex) { //If there was an error on any field
                        JOptionPane.showMessageDialog(null,
                                ex.getMessage(),
                                "Dar de alta cliente",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    finally { //At the end of this operation, restart panel of user to default.
                        view.resetUserPanel();
                    }

                }
            }
            //If user exists on the server side during this session
            else {
                //Subscribe controller to server
                model.subscribe(this);
                //Activates the corresponding buttons on view
                view.activateButtons();
                //Notify that the user has returned to the session successfully
                JOptionPane.showMessageDialog(null,
                        "Bienvenido de regreso " + user + "!",
                        "Dar de alta cliente",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        }
        catch (IllegalArgumentException ex) { //If user field was left empty
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "Dar de alta cliente",
                    JOptionPane.ERROR_MESSAGE);
        }
        catch (RemoteException ex) { //If there was a connection error with the server
            JOptionPane.showMessageDialog(null,
                    "Fallo en el servidor",
                    "Dar de alta cliente",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
    Puts on sale a new product and adds it to the server.
    - Retrieves the current user.
    - Asks the user for the information of product to be offered.
    - If the product wasn't on sale before:
        - Gets the amount to be offered.
        - Gets the time and date of the offer.
        - Creates the Product object to be offered.
        - Adds the bid to the server.
        - Informs the user if any error was encountered on the process.
    - Else, notifies the user that the product was on sale before.
     */
    public void putOnSale() throws RemoteException {
        String user = view.getCurrentUser(); //Gets user from View

        //Show user the panel to give information about product.
        int result = JOptionPane.showConfirmDialog(null,
                view.getProductPanel(),
                "Datos para makeBid name",
                JOptionPane.OK_CANCEL_OPTION);

        String product = view.getProductName(); //Gets product from view

        if(product.length() > 0) { //If product wasn't left empty

            if (!model.productExists(product)) { //If product wasn't offered before

                System.out.println("Haciendo oferta del name: " + product);

                if (result == JOptionPane.OK_OPTION) {
                    try{
                        float amount = view.getProductPrice(); //Gets amount of product
                        String description = view.getProductDescription(); //Gets description of product
                        //Gets date and time limit for the offers on this product to be made
                        int year = view.getOfferYear();
                        int month = view.getOfferMonth();
                        int day = view.getOfferDay();
                        int hour = view.getOfferHour();
                        int minutes = view.getOfferMinutes();

                        //If time of offer is not allowed, throw new Exception informing of this error
                        if(checkTimeOfOffer(hour, minutes))
                            throw new IllegalArgumentException("La subasta debe de tener al menos dos minutos de tiempo activo");

                        //Constructs new LocalDateTime to be included on the product's information
                        LocalDateTime fecha = LocalDateTime.of(year, month, day, hour, minutes);
                        //Constructs new product to be offered
                        Product offer = new Product(user, product, description, amount, fecha);

                        //If the product was successfully registered on server's side
                        if (model.addProductToAuction(user, product, offer)) {
                            //Notify user of success on this operation
                            JOptionPane.showMessageDialog(null,
                                    "Producto ofrecido con éxito!",
                                    "Ofrecer name",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            //Notify user of error on this operation
                            JOptionPane.showMessageDialog(null,
                                    "Hubo un error al makeBid el name.",
                                    "Ofrecer name",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    catch (NumberFormatException ex) { //If a number wasn't introduced on the numeric fields
                        JOptionPane.showMessageDialog(null,
                                "Campo debe de contener solamente numeros",
                                "Subastar name",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    catch (IllegalArgumentException ex) { //If there was an error with the data introduced
                        JOptionPane.showMessageDialog(null,
                                ex.getMessage(),
                                "Subastar name",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    finally { //At the end of this operation, restart to default the product information panel
                        view.resetProductPanel();
                    }
                }
            }
            //Product was put on sale before
            else {
                //Notify user of this case
                JOptionPane.showMessageDialog(null,
                        "Producto ya se encuentra en venta.",
                        "Subastar name",
                        JOptionPane.ERROR_MESSAGE);
            }

        }
        //If the product introduced was an empty line
        else {
            //Tell user of the need to introduce a product to put on sale
            JOptionPane.showMessageDialog(null,
                    "Ingrese name a subastar.",
                    "Subastar name",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    /*
    Checks if hours and minutes are greater than the current time by two minutes.
    This is done in order to leave enough time for offers to be made on during the specified time.
    - Gets the current date and time.
    - Checks if the current minutes plus two are less or equals than 59:
        - If true, checks that the introduced hour is equal or greater than the current hour AND
                   if the introduced minutes are less than current minutes plus two.
        - Else, the hour introduced has to be greater than the current hour plus one AND
                the minutes introduced are greater or equals to zero.
     */
    private boolean checkTimeOfOffer(int h, int m) {
        LocalDateTime now = LocalDateTime.now(); //Gets current date and time
        if(now.getMinute() + 2 <= 59) { //If current minutes plus two are LEQT 59
            return h >= now.getHour() && (m < now.getMinute() + 2);
        }
        else { //Else new time has to be made on the next hour
            return (h >= now.getHour() + 1) && m >= 0;
        }
    }

    /*
    Updates list of products that are displayed on the View.
    - Gets the catalog of products that are currently active on the server.
    - Resets the list of descriptions that are on the Controller.
    - Resets the list of products that are displayed on the View.
    - For each product on the catalog retrieved:
        - Adds the product and its description to the list of descriptions.
        - Adds the product to the list that are displayed on View.
     */
    public void updateProductList() throws RemoteException {

        List<Product> catalog = model.getCatalog(); //Get catalog on server
        listDescription = new Hashtable<>(); //Reconstruct list of descriptions
        view.resetProductList(); //Reinitialize the products shown on View

        catalog.forEach(prod -> { //For each product
            listDescription.put(prod.getName(), prod.getDescription()); //Put the name of product and its description on the list
            view.addProduct(prod); //Add product to View to be displayed
        });

    }

    /*
    Puts a new offer on the desired product.
    - If there is a selected product on the View:
        - Get the product selected.
        - Get the amount to be offered.
        - Get the user that is making the offer.
        - Adds bid to the server.
    - Else, there wasn't a selected product:
        - Informs the user that a product on the View needs to be selected to make a new bid.
     */
    public void offerOnProduct() throws RemoteException {
        try {
            Product offer = view.getSelectedProduct(); //Get product currently selected on View
            float amount = view.getAmountBid(); //Get the amount to be offered on this product
            String user = view.getCurrentUser(); //Get current user on View

            model.addOffer(user, offer.getName(), amount); //Add the offer on the server
        }
        catch (IllegalArgumentException ex) {//If there was an error on the introduced amount to offer
            //Displays the user the message of the error caught.
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "Ofrecer en name",
                    JOptionPane.ERROR_MESSAGE);
        }
        catch (NullPointerException ex) { //If no product was selected on view
            //Show user that before offering a product has to be selected
            JOptionPane.showMessageDialog(null,
                    "Debe seleccionar un name para makeBid.",
                    "Ofrecer en name",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
    Changes the description of the View depending on the desired item.
    - Method to be called when an item is selected on the list of products of the View.
     */
    public void changeDescription(Product item) {
        if (item != null) { //If item is not null
            System.out.println(item); //Prints item information
            String context = listDescription.get(item.getName()); //Gets description from list of descriptions
            view.showDescription(context); //Display this description on View
        }
    }

    /*
    Method to be called by Server to update the View. (Callback)
     */
    public void updateView() throws RemoteException {
        this.updateProductList(); //Updates the View of this Controller
    }

    /*
    Disconnects this Controller of the server. (Callback)
     */
    public void disconnect() {
        try{
            model.unsubscribe(this); //Unsubscribe this Controller of server
        }
        catch (RemoteException ex) { //If there was an error connecting to the server
            System.out.println("Error al desconectarse del servidor");
            ex.printStackTrace();
        }
    }

    /*
    Method to be called by server to know the current User of the Controller.
     */
    public String getUser() {
        return view.getCurrentUser(); //Retrieves user from View
    }

    /*
    Notifies the user that the offer made has finished and who was the highest bidder.
    - Gets the name of the product and the information of the client.
    - Constructs a panel where the information of the buyer will be displayed in.
    - Shows the user the contact information of the buyer.
     */
    public void notifyEndBid(User c, Product p) {
        if(c.getName().equalsIgnoreCase(getUser())) { //If the buyer is not the current user
            JPanel panel = new JPanel(); //Creates panel where information will be displayed
            panel.setLayout(new BorderLayout());

            //Header panel
            JPanel north = new JPanel();
            north.setLayout(new GridLayout(2, 1));
            north.add(new JLabel("Subasta para el name" + p.getName() + " finalizó con exito!")); //Shows the product bought
            north.add(new JLabel("Datos del comprador:"));
            panel.add(north, BorderLayout.NORTH); //Adds this panel to the main one.

            //Add information of bidder to the main panel at the center
            panel.add(new JLabel(c.toString()), BorderLayout.CENTER);

            //Displays this panel to user
            JOptionPane.showConfirmDialog(null,
                    panel,
                    "Datos del comprador",
                    JOptionPane.OK_OPTION);
        }
        else { //If the highest bid was made by the current user
            //Inform the user that he/she was the highest bidder
            JOptionPane.showConfirmDialog(null,
                    "Usted quien ofrecio más por " + p.getName(),
                    "Datos del comprador",
                    JOptionPane.OK_OPTION);
        }

    }

}
