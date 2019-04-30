package subasta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

public class Client {

    AuctionController controller;

    JFrame mainFrame;

    JTextField userField;
    JTextField directionField;
    JTextField emailField;
    JTextField phoneField;
    JTextField nicknameField;
    JPanel userPanel;

    JTextField productField;
    JTextField descriptionField;
    JTextField priceField;
    JTextField yearField;
    JTextField monthField;
    JTextField dayField;
    JTextField hourField;
    JTextField minuteField;
    JPanel productPanel;
    JTextField amountField;

    DefaultListModel<Product> products;
    JTextArea descriptionText;
    JList list;
    JButton connect;
    JButton exit;
    JButton putOnSale;
    JButton makeBid;

    private static final Font FONT = new Font("Arial", Font.BOLD, 14);

    public Client() {

        try {
            controller = new AuctionController(this);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        Container panel;

        mainFrame = new JFrame("Cliente Subasta");
        panel = mainFrame.getContentPane();
        panel.setLayout(new BorderLayout());

        JPanel north = new JPanel();
        north.setLayout(new BorderLayout());
        JPanel usuarioDatos = new JPanel();
        usuarioDatos.setLayout(new GridLayout(1, 2));
        JPanel accionesUsuario = new JPanel();
        accionesUsuario.setLayout(new GridLayout(2, 1));

        userField = new JTextField();
        usuarioDatos.add(new JLabel("Nombre del userField"));
        usuarioDatos.add(userField);

        connect = new JButton("Conectar");
        accionesUsuario.add(connect);
        putOnSale = new JButton("Poner a la venta");
        accionesUsuario.add(putOnSale);
        putOnSale.setEnabled(false);

        north.add(usuarioDatos, BorderLayout.NORTH);
        north.add(accionesUsuario, BorderLayout.SOUTH);
        panel.add(north, BorderLayout.NORTH);

        products = new DefaultListModel<>();
        list = new JList(products); // data has type Object[]
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setFont(FONT);
        JScrollPane listaScroller = new JScrollPane(list);
        listaScroller.setPreferredSize(new Dimension(250, 80));
        panel.add(listaScroller, BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setLayout(new BorderLayout());
        JPanel productoInfo = new JPanel();
        productoInfo.setLayout(new GridLayout(1, 2));
        JPanel ofrecerProd = new JPanel();
        ofrecerProd.setLayout(new GridLayout(1, 2));

        JLabel desc = new JLabel("Descripcion: ");
        productoInfo.add(desc);
        descriptionText = new JTextArea();
        JScrollPane scroll = new JScrollPane(descriptionText);
        descriptionText.setEditable(false);
        descriptionText.setLineWrap(true);
        descriptionText.setWrapStyleWord(true);
        scroll.setPreferredSize(new Dimension(mainFrame.getWidth(), 30));

        productoInfo.add(scroll);
        south.add(productoInfo, BorderLayout.NORTH);

        amountField = new JTextField();
        makeBid = new JButton("Ofrecer");
        ofrecerProd.add(makeBid);
        makeBid.setEnabled(false);
        ofrecerProd.add(amountField);
        south.add(ofrecerProd, BorderLayout.CENTER);

        exit = new JButton("Salir");
        south.add(exit, BorderLayout.SOUTH);
        panel.add(south, BorderLayout.SOUTH);

        createUserPanel();
        createProductPanel();
        initializeListeners();

        mainFrame.setSize(500, 500);
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
    }

    private void createProductPanel() {

        productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(0, 2));
        productField = new JTextField();
        productPanel.add(new JLabel("Nombre:"));
        productPanel.add(productField);
        priceField = new JTextField();
        productPanel.add(new JLabel("Precio inicial:"));
        productPanel.add(priceField);
        descriptionField = new JTextField();
        productPanel.add(new JLabel("Descripcion:"));
        productPanel.add(descriptionField);
        productPanel.add(new JLabel("Fecha de Cierre"));
        productPanel.add(new JLabel(""));

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
        productPanel.add(new JLabel(""));

        hourField = new JTextField();
        productPanel.add(new JLabel("Hora (0 a 23):"));
        productPanel.add(hourField);
        minuteField = new JTextField();
        productPanel.add(new JLabel("Minutos (0 a 59):"));
        productPanel.add(minuteField);
    }

    private void initializeListeners() {

        connect.addActionListener(e -> controller.connectUser() );

        exit.addActionListener(e -> exitApp());

        putOnSale.addActionListener(e -> {
            try {
                controller.putOnSale();
            } catch (RemoteException ex) {
                System.out.println("Error al poner en venta un name");
            }
        });

        makeBid.addActionListener(e -> {
            try {
                controller.offerOnProduct();
            } catch (RemoteException ex) {
                System.out.println("Error al makeBid en name");
            }
        });

        list.addListSelectionListener(e -> {

            if (e.getValueIsAdjusting() == false) {
                JList lista = (JList) e.getSource();
                Product item = (Product) lista.getSelectedValue();
                controller.changeDescription(item);

            }

        });
    }

    private void exitApp() {

        controller.disconnect();

        System.exit(1);

    }

    public void activateButtons() {
        makeBid.setEnabled(true);
        putOnSale.setEnabled(true);
        connect.setEnabled(false);
    }

    public JPanel getUserPanel() {
        return userPanel;
    }

    public String getCurrentUser() {
        String txt = userField.getText();
        if(txt.length() == 0)
            throw new IllegalArgumentException("Usuario no puede estar vacío, intente otra vez.");


        return txt;
    }

    public String getDirection() {
        String txt = directionField.getText();
        if(txt.length() == 0)
            throw new IllegalArgumentException("Direccion no puede estar vacía, intente otra vez.");


        return txt;
    }

    public String getEmail() {
        String txt = emailField.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Correo no puede estar vacío, intente otra vez.");

        String emailTest = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        boolean pass = txt.matches(emailTest);
        if (!pass) throw new IllegalArgumentException("Correo incorrecto, intente otra vez.");


        return txt;
    }

    public String getPhone() throws IllegalArgumentException{
        String txt = phoneField.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Telefono no puede estar vacío, intente otra vez.");
        if(txt.length() > 10) throw new IllegalArgumentException("Numero de phone no puede ser mayor a 10 digitos");

        String resultado = "";
        long phone = Long.parseLong(txt);
        resultado = String.valueOf(phone);

        return resultado;
    }

    public String getNickname() throws IllegalArgumentException {
        String txt = nicknameField.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Nickname no puede estar vacía, intente otra vez.");

        return txt;
    }

    public void resetUserPanel() {
        directionField.setText("");
        emailField.setText("");
        phoneField.setText("");
        nicknameField.setText("");
    }

    public JPanel getProductPanel() {
        return productPanel;
    }

    public String getProductName() {
        return productField.getText();
    }

    public String getProductDescription() throws IllegalArgumentException {
        String txt = descriptionField.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("La descripción no puede estar vacía, intente otra vez.");
        if(txt.length() > 30) throw new IllegalArgumentException("La descripción del name no puede ser mayor a 30 digitos");

        return txt;
    }

    public float getProductPrice() throws IllegalArgumentException {
        String txt = priceField.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Precio no puede estar vacío, intente otra vez.");
      
        float resultado = 0;
        resultado = Float.parseFloat(txt);
        if (resultado < 0) throw new IllegalArgumentException("Precio no puede ser menor a 0, intente otra vez.");

        return resultado;
    }

    public int getOfferYear() throws IllegalArgumentException {
        String txt = yearField.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Año no puede estar vacío, intente otra vez.");

        int resultado = 0;
        resultado = Integer.parseInt(txt);
        if (resultado < 2019) {
            throw new IllegalArgumentException("Año no puede ser anterior al actual.");
        }

        return resultado;
    }

    public int getOfferMonth() throws IllegalArgumentException {
        String txt = monthField.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Mes no puede estar vacío, intente otra vez.");

        int resultado = 0;
        resultado = Integer.parseInt(txt);
        if (resultado < 0 || resultado > 12) throw new IllegalArgumentException("Mes debe estar entre 0 y 12");

        return resultado;
    }

    public int getOfferDay() throws IllegalArgumentException {
        String txt = dayField.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Dia no puede estar vacío, intente otra vez.");

        int resultado = 0;
        resultado = Integer.parseInt(txt);
        if (resultado < 0 || resultado > 31) throw new IllegalArgumentException("Dia debe de estar entre 0 y 31.");

        return resultado;
    }

    public int getOfferHour() throws IllegalArgumentException {
        String txt = hourField.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Hora no puede estar vacia, intente otra vez.");

        int resultado = 0;
        resultado = Integer.parseInt(txt);
        if (resultado < 0 || resultado > 23) throw new IllegalArgumentException("Hora debe estar entre 0 y 23.");

        return resultado;
    }

    public int getOfferMinutes() throws IllegalArgumentException {
        String txt = minuteField.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Minutos no pueden estar vacíos, intente otra vez.");

        int resultado = 0;
        resultado = Integer.parseInt(txt);
        if (resultado < 0 || resultado > 60) throw new IllegalArgumentException("Minutos deben de estar entre 0 y 59.");

        return resultado;
    }

    public void resetProductPanel() {
        productField.setText("");
        descriptionField.setText("");
        priceField.setText("");
        yearField.setText("");
        monthField.setText("");
        dayField.setText("");
        hourField.setText("");
        minuteField.setText("");
    }

    public void resetProductList() {

        products.removeAllElements();
        descriptionText.setText("");

    }

    public void addProduct(Product prod) {

        products.addElement(prod);

    }

    public void showDescription(String context) {

        descriptionText.setText(context);

    }

    public float getAmountBid() throws IllegalArgumentException {
        String txt = amountField.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Campo no puede estar vacío, intente otra vez.");

        float resultado = 0.0f;
        resultado = Float.parseFloat(amountField.getText());
        if (resultado < 0) throw new IllegalArgumentException("Precio no puede ser menor a 0, intente otra vez.");

        return resultado;
    }

    public Product getSelectedProduct() {

        return (Product) list.getSelectedValue();
    }

    public static void main(String... args) {
        new Client();
    }

}