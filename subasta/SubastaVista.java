package subasta;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

public class SubastaVista {

    SubastaControlador controller;

    JFrame principal;

    JTextField usuario;
    JTextField direccion;
    JTextField email;
    JTextField telefono;
    JTextField nickname;
    JPanel userPanel;

    JTextField producto;
    JTextField monto;
    JTextField descripcion;
    JTextField precioInicial;
    JTextField año;
    JTextField mes;
    JTextField dia;
    JTextField hora;
    JTextField minuto;
    JPanel productPanel;

    DefaultListModel<Producto> productos;
    JTextArea descripcionProd;
    JList lista;
    JButton conectar;
    JButton salir;
    JButton ponerALaVenta;
    JButton obtenerLista;
    JButton ofrecer;

    private static final Font FONT = new Font("Arial", Font.BOLD, 14);

    public SubastaVista() {

        try {
            controller = new SubastaControlador(this);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        Container panel;

        principal = new JFrame("Cliente Subasta");
        panel = principal.getContentPane();
        panel.setLayout(new BorderLayout());

        JPanel north = new JPanel();
        north.setLayout(new BorderLayout());
        JPanel usuarioDatos = new JPanel();
        usuarioDatos.setLayout(new GridLayout(1, 2));
        JPanel accionesUsuario = new JPanel();
        accionesUsuario.setLayout(new GridLayout(2, 1));

        usuario = new JTextField();
        usuarioDatos.add(new JLabel("Nombre del usuario"));
        usuarioDatos.add(usuario);

        conectar = new JButton("Conectar");
        accionesUsuario.add(conectar);
        ponerALaVenta = new JButton("Poner a la venta");
        accionesUsuario.add(ponerALaVenta);
        ponerALaVenta.setEnabled(false);

        north.add(usuarioDatos, BorderLayout.NORTH);
        north.add(accionesUsuario, BorderLayout.SOUTH);
        panel.add(north, BorderLayout.NORTH);

        productos = new DefaultListModel<>();
        lista = new JList(productos); // data has type Object[]
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setLayoutOrientation(JList.VERTICAL);
        lista.setFont(FONT);
        JScrollPane listaScroller = new JScrollPane(lista);
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
        descripcionProd = new JTextArea();
        JScrollPane scroll = new JScrollPane(descripcionProd);
        descripcionProd.setEditable(false);
        descripcionProd.setLineWrap(true);
        descripcionProd.setWrapStyleWord(true);
        scroll.setPreferredSize(new Dimension(principal.getWidth(), 30));

        productoInfo.add(scroll);
        south.add(productoInfo, BorderLayout.NORTH);

        monto = new JTextField();
        ofrecer = new JButton("Ofrecer");
        ofrecerProd.add(ofrecer);
        ofrecer.setEnabled(false);
        ofrecerProd.add(monto);
        south.add(ofrecerProd, BorderLayout.CENTER);

        salir = new JButton("Salir");
        south.add(salir, BorderLayout.SOUTH);
        panel.add(south, BorderLayout.SOUTH);

        createUserPanel();
        createProductPanel();
        initializeListeners();

        principal.setSize(500, 500);
        principal.setMinimumSize(new Dimension(600, 500));
        principal.setVisible(true);
        principal.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        principal.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApp();
            }
        });

    }

    private void createUserPanel() {
        userPanel = new JPanel();
        userPanel.setLayout(new GridLayout(0, 2));
        direccion = new JTextField();
        userPanel.add(new JLabel("Direccion:"));
        userPanel.add(direccion);
        email = new JTextField();
        userPanel.add(new JLabel("Correo:"));
        userPanel.add(email);
        telefono = new JTextField();
        userPanel.add(new JLabel("Telefono:"));
        userPanel.add(telefono);
        nickname = new JTextField();
        userPanel.add(new JLabel("Nickname:"));
        userPanel.add(nickname);
    }

    private void createProductPanel() {

        productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(0, 2));
        producto = new JTextField();
        productPanel.add(new JLabel("Nombre:"));
        productPanel.add(producto);
        precioInicial = new JTextField();
        productPanel.add(new JLabel("Precio inicial:"));
        productPanel.add(precioInicial);
        descripcion = new JTextField();
        productPanel.add(new JLabel("Descripcion:"));
        productPanel.add(descripcion);
        productPanel.add(new JLabel("Fecha de Cierre"));
        productPanel.add(new JLabel(""));

        año = new JTextField();
        productPanel.add(new JLabel("Año:"));
        productPanel.add(año);
        mes = new JTextField();
        productPanel.add(new JLabel("Mes (1 a 12):"));
        productPanel.add(mes);
        dia = new JTextField();
        productPanel.add(new JLabel("Dia (1 a 31):"));
        productPanel.add(dia);

        productPanel.add(new JLabel("Tiempo de Cierre"));
        productPanel.add(new JLabel(""));

        hora = new JTextField();
        productPanel.add(new JLabel("Hora (0 a 23):"));
        productPanel.add(hora);
        minuto = new JTextField();
        productPanel.add(new JLabel("Minutos (0 a 59):"));
        productPanel.add(minuto);
    }

    private void initializeListeners() {

        conectar.addActionListener(e -> {
            try {
                controller.connectUser();
            } catch (RemoteException ex) {
                System.out.println("Error al conectar usuario");
            }
        });

        salir.addActionListener(e -> exitApp());

        ponerALaVenta.addActionListener(e -> {
            try {
                controller.putOnSale();
            } catch (RemoteException ex) {
                System.out.println("Error al poner en venta un producto");
            }
        });

        ofrecer.addActionListener(e -> {
            try {
                controller.offerOnProduct();
            } catch (RemoteException ex) {
                System.out.println("Error al ofrecer en producto");
            }
        });

        lista.addListSelectionListener(e -> {
            try {

                if (e.getValueIsAdjusting() == false) {
                    JList lista = (JList) e.getSource();
                    Producto item = (Producto) lista.getSelectedValue();
                    controller.changeDescription(item);

                }
            } catch (RemoteException ex) {
                System.out.println("Error al cambiar la descripcion");
            }
        });
    }

    private void exitApp() {

        controller.disconnect();

        System.exit(1);

    }

    public void activateButtons() {
        ofrecer.setEnabled(true);
        ponerALaVenta.setEnabled(true);
        conectar.setEnabled(false);
    }

    public JPanel getUserPanel() {
        return userPanel;
    }

    public String getUsuario() {
        String txt = usuario.getText();
        boolean vacio = false;
        if(txt.length() == 0) {
            vacio = true;
            throw new IllegalArgumentException("Usuario no puede estar vacío, intente otra vez.");
        }

        return vacio ? txt : "";
    }

    public String getDireccion() {
        String txt = direccion.getText();
        boolean vacio = false;
        if(txt.length() == 0) {
            vacio = true;
            throw new IllegalArgumentException("Direccion no puede estar vacía, intente otra vez.");
        }

        return vacio ? txt : "";
    }

    public String getEmail() {
        String txt = email.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Correo no puede estar vacío, intente otra vez.");

        String emailTest = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        boolean notPass = !txt.matches(emailTest);
        if (notPass) throw new IllegalArgumentException("Correo incorrecto, intente otra vez.");


        return notPass ? txt : "";
    }

    public String getTelefono() throws IllegalArgumentException{
        String txt = telefono.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Telefono no puede estar vacío, intente otra vez.");
        if(txt.length() > 10) throw new IllegalArgumentException("Numero de telefono no puede ser mayor a 10 digitos");

        String resultado = "";
        long phone = Long.parseLong(txt);
        resultado = String.valueOf(phone);

        return resultado;
    }

    public String getNickname() throws IllegalArgumentException {
        String txt = nickname.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Nickname no puede estar vacía, intente otra vez.");

        return txt;
    }

    public void resetUserPanel() {
        direccion.setText("");
        email.setText("");
        telefono.setText("");
        nickname.setText("");
    }

    public JPanel getProductPanel() {
        return productPanel;
    }

    public String getProducto() {
        return producto.getText();
    }

    public String getDescripcionProducto() throws IllegalArgumentException {
        String txt = descripcionProd.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("La descripción no puede estar vacía, intente otra vez.");
        if(txt.length() > 30) throw new IllegalArgumentException("La descripción del producto no puede ser mayor a 30 digitos");

        return txt;
    }

    public float getPrecioInicial() throws IllegalArgumentException {
        String txt = precioInicial.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Precio no puede estar vacío, intente otra vez.");
      
        float resultado = 0;
        resultado = Float.parseFloat(txt);
        if (resultado < 0) throw new IllegalArgumentException("Precio no puede ser menor a 0, intente otra vez.");

        return resultado;
    }

    public int getAñoOferta() throws IllegalArgumentException {
        String txt = año.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Año no puede estar vacío, intente otra vez.");

        int resultado = 0;
        resultado = Integer.parseInt(txt);
        if (resultado < 2019) {
            throw new IllegalArgumentException("Año no puede ser anterior al actual.");
        }

        return resultado;
    }

    public int getMesOferta() throws IllegalArgumentException {
        String txt = mes.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Mes no puede estar vacío, intente otra vez.");

        int resultado = 0;
        resultado = Integer.parseInt(txt);
        if (resultado < 0 || resultado > 12) throw new IllegalArgumentException("Mes debe estar entre 0 y 12");

        return resultado;
    }

    public int getDiaOferta() throws IllegalArgumentException {
        String txt = dia.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Dia no puede estar vacío, intente otra vez.");

        int resultado = 0;
        resultado = Integer.parseInt(txt);
        if (resultado < 0 || resultado > 31) throw new IllegalArgumentException("Dia debe de estar entre 0 y 31.");

        return resultado;
    }

    public int getHoraOferta() throws IllegalArgumentException {
        String txt = hora.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Hora no puede estar vacia, intente otra vez.");

        int resultado = 0;
        resultado = Integer.parseInt(txt);
        if (resultado < 0 || resultado > 12) throw new IllegalArgumentException("Hora debe estar entre 0 y 12.");

        return resultado;
    }

    public int getMinutoOferta() throws IllegalArgumentException {
        String txt = minuto.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Minutos no pueden estar vacíos, intente otra vez.");

        int resultado = 0;
        resultado = Integer.parseInt(txt);
        if (resultado < 0 || resultado > 60) throw new IllegalArgumentException("Minutos deben de estar entre 0 y 59.");

        return resultado;
    }

    public void resetProductPanel() {
        producto.setText("");
        descripcion.setText("");
        precioInicial.setText("");
        año.setText("");
        mes.setText("");
        dia.setText("");
        hora.setText("");
        minuto.setText("");
    }

    public void reinicializaListaProductos() {

        productos.removeAllElements();
        descripcionProd.setText("");

    }

    public void agregaProducto(Producto prod) {

        productos.addElement(prod);

    }

    public void desplegarDescripcion(String context) {

        descripcionProd.setText(context);

    }

    public float getMontoOfrecido() throws IllegalArgumentException {
        String txt = minuto.getText();
        if(txt.length() == 0) throw new IllegalArgumentException("Campo no puede estar vacío, intente otra vez.");

        float resultado = 0.0f;
        resultado = Float.parseFloat(monto.getText());
        if (resultado < 0) throw new IllegalArgumentException("Precio no puede ser menor a 0, intente otra vez.");

        return resultado;
    }

    public Producto getProductoSeleccionado() throws NullPointerException {

        return (Producto) lista.getSelectedValue();
    }

    public static void main(String... args) {
        new SubastaVista();
    }

}