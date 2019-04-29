package subasta;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import javax.swing.*;

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

    private static final Font FONT = new Font("Arial", Font.BOLD,14);

    public SubastaVista() {

        try{
            controller = new SubastaControlador(this);
        }
        catch (RemoteException ex) {
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

    public JPanel getUserPanel() {
        return userPanel;
    }

    public String getUsuario() {

        return usuario.getText();
    }

    public String getDireccion() {
        return direccion.getText();
    }

    public String getEmail() {
        return email.getText();
    }

    public String getTelefono() {
        return telefono.getText();
    }

    public String getNickname() {
        return nickname.getText();
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

    public String getDescripcionProducto() {
        return descripcion.getText();
    }

    public float getPrecioInicial() {

        float resultado = 0.0f;

        try {

            resultado = Float.parseFloat(precioInicial.getText());

        } catch (Exception e) {

            System.out.println("Hay problemas con el precio inicial");
        }

        return resultado;
    }

    public int getAñoOferta() {
        int resultado = 0;

        try {

            resultado = Integer.parseInt(año.getText());

        } catch (Exception e) {

            System.out.println("Hay problemas con el año de la oferta");
        }

        return resultado;
    }

    public int getMesOferta() {
        int resultado = 0;

        try {

            resultado = Integer.parseInt(mes.getText());

        } catch (Exception e) {

            System.out.println("Hay problemas con el mes de la oferta");
        }

        return resultado;
    }

    public int getDiaOferta() {
        int resultado = 0;

        try {

            resultado = Integer.parseInt(dia.getText());

        } catch (Exception e) {

            System.out.println("Hay problemas con el dia de la oferta");
        }

        return resultado;
    }

    public int getHoraOferta() {
        int resultado = 0;

        try {

            resultado = Integer.parseInt(hora.getText());

        } catch (Exception e) {

            System.out.println("Hay problemas con el precio inicial");
        }

        return resultado;
    }

    public int getMinutoOferta() {
        int resultado = 0;

        try {

            resultado = Integer.parseInt(minuto.getText());

        } catch (Exception e) {

            System.out.println("Hay problemas con el precio inicial");
        }

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

    public float getMontoOfrecido() {

        float resultado = 0.0f;

        try {

            resultado = Float.parseFloat(monto.getText());

        } catch (Exception e) {

            System.out.println("Hay problemas con el monto ofrecido");
        }

        return resultado;
    }

    public Producto getProductoSeleccionado() {

        return (Producto) lista.getSelectedValue();
    }

    public static void main(String... args) {
        new SubastaVista();
    }

}