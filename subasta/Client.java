package subasta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

public class Client {

    private AuctionController controller;

    private JFrame mainFrame;

    private JTextField userField;
    private JTextField directionField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField nicknameField;
    private JPanel userPanel;

    private JTextField productField;
    private JTextField descriptionField;
    private JTextField priceField;
    private JTextField yearField;
    private JTextField monthField;
    private JTextField dayField;
    private JTextField hourField;
    private JTextField minuteField;
    private JPanel productPanel;
    private JTextField amountField;

    private DefaultListModel<Product> products;
    private JTextArea descriptionText;
    private JList list;
    private JButton connect;
    private JButton exit;
    private JButton putOnSale;
    private JButton makeBid;

    private static final Font FONT = new Font("Arial", Font.BOLD, 14);

    public Client() {

        //Try to initialize Controller
        try {
            controller = new AuctionController(this);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        Container panel;

        mainFrame = new JFrame("Cliente Subasta"); //Constructing frame
        panel = mainFrame.getContentPane();
        panel.setLayout(new BorderLayout()); //Main frame has BorderLayout

        //Header panel
        JPanel north = new JPanel();
        north.setLayout(new BorderLayout());
        JPanel userData = new JPanel(); //Panel with user's options
        userData.setLayout(new GridLayout(1, 2));
        JPanel userActions = new JPanel();
        userActions.setLayout(new GridLayout(2, 1));

        userField = new JTextField();
        userData.add(new JLabel("Nombre del usuario:"));
        userData.add(userField);

        connect = new JButton("Conectar");
        userActions.add(connect);
        putOnSale = new JButton("Poner a la venta");
        userActions.add(putOnSale);
        putOnSale.setEnabled(false);

        north.add(userData, BorderLayout.NORTH);
        north.add(userActions, BorderLayout.SOUTH);
        panel.add(north, BorderLayout.NORTH);

        products = new DefaultListModel<>();
        list = new JList(products); // data has type Object[]
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setFont(FONT);
        JScrollPane scrollList = new JScrollPane(list);
        scrollList.setPreferredSize(new Dimension(250, 80));
        panel.add(scrollList, BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setLayout(new BorderLayout());
        JPanel prodInfoPanel = new JPanel();
        prodInfoPanel.setLayout(new GridLayout(1, 2));
        JPanel offerPanel = new JPanel();
        offerPanel.setLayout(new GridLayout(1, 2));

        JLabel descLabel = new JLabel("Descripcion: ");
        prodInfoPanel.add(descLabel);
        descriptionText = new JTextArea();
        JScrollPane scrollDescription = new JScrollPane(descriptionText);
        descriptionText.setEditable(false);
        descriptionText.setLineWrap(true);
        descriptionText.setWrapStyleWord(true);
        scrollDescription.setPreferredSize(new Dimension(mainFrame.getWidth(), 30));

        prodInfoPanel.add(scrollDescription);
        south.add(prodInfoPanel, BorderLayout.NORTH);

        amountField = new JTextField();
        makeBid = new JButton("Ofrecer");
        offerPanel.add(makeBid);
        makeBid.setEnabled(false);
        offerPanel.add(amountField);
        south.add(offerPanel, BorderLayout.CENTER);

        exit = new JButton("Salir");
        south.add(exit, BorderLayout.SOUTH);
        panel.add(south, BorderLayout.SOUTH);

        createUserPanel();
        createProductPanel();
        initializeListeners();

        mainFrame.setSize(600, 500);
        mainFrame.setMinimumSize(new Dimension(600, 500));
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApp();
            }
        });

    }
    /**
     * Creates panel to be shown when a client is to be added to the current session.
     */
    private void createUserPanel() {
        userPanel = new JPanel();
        userPanel.setLayout(new GridLayout(0, 2));
        directionField = new JTextField();
        userPanel.add(new JLabel("Direccion:"));
        userPanel.add(directionField);
        emailField = new JTextField();
        userPanel.add(new JLabel("Correo:"));
        userPanel.add(emailField);
        phoneField = new JTextField();
        userPanel.add(new JLabel("Telefono:"));
        userPanel.add(phoneField);
        nicknameField = new JTextField();
        userPanel.add(new JLabel("Nickname:"));
        userPanel.add(nicknameField);

        userPanel.setPreferredSize(new Dimension(300, 450));
    }

    /**
     * Creates panel to be shown when a product is to be added to the auction.
     */
    private void createProductPanel() {

        productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(0, 2)); //Layout, infinite rows, only 2 columns
        productField = new JTextField(); //Initialize product name field
        productPanel.add(new JLabel("Nombre:"));
        productPanel.add(productField); //Adds product name field
        priceField = new JTextField(); //Initialize price field
        productPanel.add(new JLabel("Precio inicial:"));
        productPanel.add(priceField); //Adds price field
        descriptionField = new JTextField(); //Initialize description field
        productPanel.add(new JLabel("Descripcion:"));
        productPanel.add(descriptionField); //Adds description field
        productPanel.add(new JLabel("Fecha de Cierre"));
        productPanel.add(new JLabel("")); //Empty label to fill grid

        yearField = new JTextField();
        productPanel.add(new JLabel("Año:"));
        productPanel.add(yearField);
        monthField = new JTextField();
        productPanel.add(new JLabel("Mes (1 a 12):"));
        productPanel.add(monthField);
        dayField = new JTextField();
        productPanel.add(new JLabel("Dia (1 a 31):"));
        productPanel.add(dayField);

        productPanel.add(new JLabel("Tiempo de Cierre"));
        productPanel.add(new JLabel(""));//Empty label to fill grid

        hourField = new JTextField();
        productPanel.add(new JLabel("Hora (0 a 23):"));
        productPanel.add(hourField);
        minuteField = new JTextField();
        productPanel.add(new JLabel("Minutos (0 a 59):"));
        productPanel.add(minuteField);

        productPanel.setPreferredSize(new Dimension(350, 500));
    }

    /**
     * Adds an ActionListener to each button.
     *     - Connect button => Add a user to the session or connect a returning user.
     *     - Exit button => Close the application.
     *     - putOnSale button => Display panel to input information of product to be added to the auction.
     *     - makeBid button => Makes a new bid on the selected product.
     * Adds a ListSelectionListener to the JList where products are displayed
     *     - Displays the description of the selected product.
     */
    private void initializeListeners() {

        connect.addActionListener(e -> controller.connectUser() ); //Calls connectUser method on Controller

        exit.addActionListener(e -> exitApp()); //Exits the app

        //Put on sale action perform
        putOnSale.addActionListener(e -> {
            try {
                controller.putOnSale(); //Calls putOnSale method on Controller
            } catch (RemoteException ex) {
                System.out.println("Error al poner en venta un name");
            }
        });

        makeBid.addActionListener(e -> {
            try {
                controller.offerOnProduct(); //Calls offerOnProduct method on Controller
            } catch (RemoteException ex) {
                System.out.println("Error al makeBid en name");
            }
        });

        list.addListSelectionListener(e -> {

            if (e.getValueIsAdjusting() == false) {
                JList list = (JList) e.getSource(); //Gets the source and casts it as a JList
                Product item = (Product) list.getSelectedValue(); //Gets selected value, casting to Product class
                controller.changeDescription(item); //Calls changeDescription method on Controller

            }

        });
    }

    /**
     * Method to be called when closing the application
     *     - Disconnects the Controller from the Server
     *     - Exits the application with System Exit
     */
    private void exitApp() {

        controller.disconnect(); //Calls disconnect method on Controller
        System.exit(0); //Exit application

    }

    /**
     * Enables desired buttons and disables connect button, when a client successfully connects to session.
     *     - Enables following buttons:
     *         - Poner a la venta ({@code putOnSale})
     *         - Ofrecer ({@code makeBid})
     *     - Disables "Conectar" ({@code connect}) button
     */
    public void activateButtons() {
        makeBid.setEnabled(true); //Enable "Ofrecer" button
        putOnSale.setEnabled(true); //Enable "Poner a la venta" button
        connect.setEnabled(false); //Disable "Conectar" button
    }

    /**
     * Returns the panel of user information to be displayed when a new client is to be added.
     * This panel contains all the fields corresponding to the information of a user.
     * @return panel of user information
     */
    public JPanel getUserPanel() {
        return userPanel;
    }

    /**
     * Returns the name of the client that is connected.
     *     - Retrieves and returns user from userField.
     * @return the name of the client
     * @throws IllegalArgumentException if userField text is empty
     */
    public String getCurrentUser() throws IllegalArgumentException {
        String txt = userField.getText();
        //If field was left empty throw a new exception since this field cannot be left empty
        if(txt.length() == 0)
            throw new IllegalArgumentException("Usuario no puede estar vacío, intente otra vez.");


        return txt;
    }

    /**
     * Returns the direction of the user, asked when a new client is subscribing.
     *     - Retrieves text from {@code directionField}.
     * @return the direction of the client
     * @throws IllegalArgumentException if {@code directionField} text is empty
     */
    public String getDirection() throws IllegalArgumentException {
        String txt = directionField.getText();
        //If field was left empty throw a new exception since this field cannot be left empty
        if(txt.length() == 0)
            throw new IllegalArgumentException("Direccion no puede estar vacía, intente otra vez.");


        return txt;
    }

    /**
     * Returns the email of the client, asked when a new client is subscribing.
     *     - Checks emailField text against the next regex {@code "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"}
     * @return the email of the client
     * @throws IllegalArgumentException if {@code emailField} is empty
     *  or input text doesn't match the specified RegEx.
     */
    public String getEmail() throws IllegalArgumentException {
        String txt = emailField.getText(); //Get text from emailField
        //If field was left empty throw a new exception since this field cannot be left empty
        if(txt.length() == 0) throw new IllegalArgumentException("Correo no puede estar vacío, intente otra vez.");

        //Regular Expression of an email
        String emailTest = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        //Check if input text matches the RegEx specified
        boolean pass = txt.matches(emailTest);
        //If input text didn't match the Regular Expression, throw new exception
        if (!pass) throw new IllegalArgumentException("Texto introducido no es un correo valido, intente otra vez.");

        //If matching was found, return value
        return txt;
    }

    /**
     * Returns the phone of the client, asked when a new client is subscribing.
     *     - Retrieves value from {@code phoneField}.
     *     - Parses value to long.
     *     - If success, returns value from {@code phoneField}
     * @return the phone number of the client
     * @throws IllegalArgumentException if {@code phoneField} is empty
     *  or if the value entered is longer than 10 characters
     */
    public String getPhone() throws IllegalArgumentException {
        String txt = phoneField.getText(); //Retrieves input from phoneField
        //If field was left empty throw a new exception since this field cannot be left empty
        if(txt.length() == 0) throw new IllegalArgumentException("Telefono no puede estar vacío, intente otra vez.");
        //If input text is greater than 10, throws new exception since a phone number isn't greater than 10
        if(txt.length() > 10) throw new IllegalArgumentException("Numero de phone no puede ser mayor a 10 digitos");

        String resultado = "";
        long phone = Long.parseLong(txt); //Tries to parse to long text from phoneField
        resultado = String.valueOf(phone); //Returns parsed value to String

        //If parsing was made without any error, return original value
        return resultado;
    }

    public String getNickname() throws IllegalArgumentException {
        String txt = nicknameField.getText(); //Fetches text from nicknameField
        //If field was left empty throw a new exception since this field cannot be left empty
        if(txt.length() == 0) throw new IllegalArgumentException("Nickname no puede estar vacío, intente otra vez.");

        //Returns fetched value
        return txt;
    }

    /**
     * Resets all fields that are used when adding a client.
     */
    public void resetUserPanel() {
        directionField.setText("");
        emailField.setText("");
        phoneField.setText("");
        nicknameField.setText("");
    }

    /**
     * Returns the panel of product information to be displayed when a new product is to be added.
     * This panel contains all the fields corresponding to the information of a product.
     * @return panel of production information
     */
    public JPanel getProductPanel() {
        return productPanel;
    }

    /**
     * Returns the name of the product, asked when a new {@code Product} is added to the {@code Server}.
     * @return the name of the product
     */
    public String getProductName() {
        return productField.getText();
    }

    /**
     * Returns the description of the product, asked when a new {@code Product} is added to the {@code Server}.
     * @return the description of the product
     * @throws IllegalArgumentException if {@code descriptionField} text is empty
     * or if the text is more than 20 characters
     */
    public String getProductDescription() throws IllegalArgumentException {
        String txt = descriptionField.getText(); //Retrieves text from descriptionField
        //If field was left empty throw a new exception since this field cannot be left empty
        if(txt.length() == 0) throw new IllegalArgumentException("La descripción no puede estar vacía, intente otra vez.");
        //If field has more than 20 characters throw a new exception since a lot of information was provided
        if(txt.length() > 20) throw new IllegalArgumentException("La descripción del producto no puede ser mayor a 30 digitos");

        return txt;
    }

    /**
     * Returns the price of the product, asked when a new {@code Product} is added to the {@code Server}.
     * @return the price of the product
     * @throws IllegalArgumentException if {@code priceField} text is empty
     * or if the value is negative
     */
    public float getProductPrice() throws IllegalArgumentException {
        String txt = priceField.getText();
        //If field was left empty throw a new exception since this field cannot be left empty
        if(txt.length() == 0) throw new IllegalArgumentException("Precio no puede estar vacío, intente otra vez.");

        float resultado = 0;
        resultado = Float.parseFloat(txt); //Tries to parse to float text from amountField
        //If value es less than zero (negative), throws new exception
        if (resultado < 0) throw new IllegalArgumentException("Precio no puede ser menor a 0, intente otra vez.");

        //If parsing was made without any error, return parsed value
        return resultado;
    }

    /**
     * Returns the year of the product's offer limit date,
     * asked when a new {@code Product} is added to the {@code Server}.
     *     - Retrieves text from {@code yearField}.
     *         - Cannot be empty.
     *     - Parses to integer text retrieved.
     *         - Parsed value must be between 0 and 12.
     *     - Returns parsed value.
     * @return the ending date's year of the offer, parsed to integer
     * @throws IllegalArgumentException if {@code yearField} text is empty
     * or if the parsed value is less than {@code LocalDateTime.now().getYear()}
     */
    public int getOfferYear() throws IllegalArgumentException {
        String txt = yearField.getText(); //Retrieves text from yearField
        //If field was left empty throw a new exception since this field cannot be left empty
        if(txt.length() == 0) throw new IllegalArgumentException("Año no puede estar vacío, intente otra vez.");

        int resultado = 0;
        resultado = Integer.parseInt(txt); //Tries to parse to integer text from yearField
        //If parsed value is less than actual year.
        if (resultado < LocalDateTime.now().getYear())
            throw new IllegalArgumentException("Año no puede ser anterior al actual.");

        //If parsing was made without any error, return parsed value
        return resultado;
    }

    /**
     * Returns the month of the product's offer limit date,
     * asked when a new {@code Product} is added to the {@code Server}.
     *     - Retrieves text from {@code monthField}.
     *         - Cannot be empty.
     *     - Parses to integer text retrieved.
     *         - Parsed value must be between 0 and 12.
     *     - Returns parsed value.
     * @return the month of the ending date of the offer, parsed to integer
     * @throws IllegalArgumentException if {@code monthField} text is empty
     * or if the parsed value is not between 0 and 12
     */
    public int getOfferMonth() throws IllegalArgumentException {
        String txt = monthField.getText(); //Gets text from monthField
        //If field was left empty throw a new exception since this field cannot be left empty
        if(txt.length() == 0) throw new IllegalArgumentException("Mes no puede estar vacío, intente otra vez.");

        int resultado = 0;
        resultado = Integer.parseInt(txt); //Tries to parse to integer text from monthField
        //If value isn't between 0 and 12, throws new exception
        if (resultado < 0 || resultado > 12) throw new IllegalArgumentException("Mes debe estar entre 0 y 12");

        //If parsing was made without any error, return parsed value
        return resultado;
    }

    /**
     * Returns the day of the product's offer limit date,
     * asked when a new {@code Product} is added to the {@code Server}.
     *     - Retrieves text from {@code dayField}.
     *         - Cannot be empty.
     *     - Parses to integer text retrieved.
     *         - Value must be between 0 and 31.
     *     - Returns parsed value.
     * @return
     * @throws IllegalArgumentException if {@code dayField} text is empty
     * or if the parsed value is not between 0 and 31
     */
    public int getOfferDay() throws IllegalArgumentException {
        String txt = dayField.getText(); //Retrieves text from dayField
        //If field was left empty throw a new exception since this field cannot be left empty
        if(txt.length() == 0) throw new IllegalArgumentException("Dia no puede estar vacío, intente otra vez.");

        int resultado = 0;
        resultado = Integer.parseInt(txt); //Tries to parse to integer text from dayField
        //If value isn't between 0 and 31, throws new exception.
        if (resultado < 0 || resultado > 31) throw new IllegalArgumentException("Dia debe de estar entre 0 y 31.");

        //If parsing was made without any error, return parsed value
        return resultado;
    }


    /**
     * Returns the hour of the product's offer limit date,
     * asked when a new {@code Product} is added to the {@code Server}.
     *     - Retrieves text from {@code hourField}.integer
     *         - Cannot be empty.
     *     - Parses to integer.
     *         - Value must be between 0 and 23.
     *     - Returns parsed value.
     * @return the hours of ending date of offer made, parsed to integer
     * @throws IllegalArgumentException if the {@code hourField} text is empty
     * or the parsed value is not between 0 and 23
     */
    public int getOfferHour() throws IllegalArgumentException {
        String txt = hourField.getText(); //Gets text from hourField
        //If field was left empty throw a new exception since this field cannot be left empty
        if(txt.length() == 0) throw new IllegalArgumentException("Hora no puede estar vacia, intente otra vez.");

        int resultado = 0;
        resultado = Integer.parseInt(txt); //Tries to parse to integer text from hourField
        //If parsed value is not between 0 and 23, throws new exception
        if (resultado < 0 || resultado > 24) throw new IllegalArgumentException("Hora debe estar entre 0 y 23.");

        //If parsing was made without any error, return parsed value
        return resultado;
    }

    /**
     * Returns the hour of the product's offer limit date,
     * asked when a new {@code Product} is added to the {@code Server}.
     *     - Retrieves text from {@code minuteField}.
     *         - Cannot be empty.
     *     - Parses to integer.
     *         - Value must be between 0 and 59
     *     - Returns parsed value.
     * @return
     * @throws IllegalArgumentException if the {@code minuteField} text is empty
     * or if the parsed value is not between 0 and 59
     */
    public int getOfferMinutes() throws IllegalArgumentException {
        String txt = minuteField.getText(); //Gets text from minuteField
        //If field was left empty throw a new exception since this field cannot be left empty
        if(txt.length() == 0) throw new IllegalArgumentException("Minutos no pueden estar vacíos, intente otra vez.");

        int resultado = 0;
        resultado = Integer.parseInt(txt); //Tries to parse to integer text from minuteField
        //If input text is not between 0 and 59, throws exception
        if (resultado < 0 || resultado > 60) throw new IllegalArgumentException("Minutos deben de estar entre 0 y 59.");

        //If parsing was done without any errors, return text parsed.
        return resultado;
    }

    /**
     * Resets all fields that are used when adding a product.
     */
    public void resetProductPanel() {
        productField.setText(""); //Set product's name field as empty
        descriptionField.setText(""); //Set product's description field as empty
        priceField.setText(""); //Set product's price field as empty
        yearField.setText(""); //Set product's ending date year field as empty
        monthField.setText(""); //Set product's ending date month field as empty
        dayField.setText(""); //Set product's ending date day field as empty
        hourField.setText(""); //Set product's ending time hour field as empty
        minuteField.setText(""); //Set product's ending time minutes field as empty
    }

    /**
     * Removes all products being currently displayed and resets description text.
     */
    public void resetProductList() {

        products.removeAllElements(); //Remove all elements from List Model
        descriptionText.setText(""); //Sets description text as empty

    }

    /**
     * Adds a product to be displayed on View.
     * @param prod the product to be added for displaying
     */
    public void addProduct(Product prod) {

        products.addElement(prod); //Adds element to List Model

    }

    /**
     * Sets specified text as a description of the product that is currently selected.
     * @param context the string to be shown as description
     */
    public void showDescription(String context) {

        descriptionText.setText(context); //Sets description text as received String

    }

    /**
     * Returns the amount offered for the current product,
     * asked when a new {@code Bid} wants to be offered to a {@code Product}
     * and is added to the {@code Server}.
     *     - Gets the text from {@code amountField}.
     *         - Cannot be empty.
     *     - Parses it to float.
     *         - Value must not be negative.
     *     - Returns parsed value.
     * @return the amount parsed to float
     * @throws IllegalArgumentException if the {@code amountField} text is empty
     *  or if the parsed value is negative
     */
    public float getAmountBid() throws IllegalArgumentException {
        String txt = amountField.getText(); //Retrieve text from amountField
        //If field was left empty throw a new exception since this field cannot be left empty
        if(txt.length() == 0) throw new IllegalArgumentException("Campo no puede estar vacío, intente otra vez.");

        float resultado = 0.0f;
        resultado = Float.parseFloat(txt); //Tries to parse to float text from amountField
        //If the value parsed was less than zero, throw exception
        if (resultado < 0) throw new IllegalArgumentException("Precio no puede ser menor a 0, intente otra vez.");

        //If parsing was made without any error, return it
        return resultado;
    }

    /**
     * Return selected value of Client. Method to be called by {@code Controller}.
     * @return the product that is currently selected (can be null).
     */
    public Product getSelectedProduct() {

        return (Product) list.getSelectedValue(); //Return selected value of JList
    }

    public static void main(String... args) {
        new Client();
    }

}