package subasta;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

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

    DefaultComboBoxModel productos;
    JLabel precioActual;
    JTextArea descripcionProd;
    JList lista;
    JButton conectar;
    JButton salir;
    JButton ponerALaVenta;
    JButton obtenerLista;
    JButton ofrecer;

    public SubastaVista() {

        controller = new SubastaControlador(this);

        // JFrame.setDefaultLookAndFeelDecorated( true );
        Container panel;

        principal = new JFrame("Cliente Subasta");
        panel = principal.getContentPane();
        panel.setLayout(new BorderLayout());
        // panel.setLayout(new GridLayout(0, 2));

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

        productos = new DefaultComboBoxModel();
        lista = new JList(productos); // data has type Object[]
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setLayoutOrientation(JList.VERTICAL);
        JScrollPane listaScroller = new JScrollPane(lista);
        listaScroller.setPreferredSize(new Dimension(250, 80));
        panel.add(listaScroller, BorderLayout.CENTER);

        // producto = new JTextField();
        // precioInicial = new JTextField();
        // panel.add(new JLabel("Producto a ofrecer"));
        // panel.add(producto);
        // panel.add(new JLabel("Precio inicial"));
        // panel.add(precioInicial);

        JPanel south = new JPanel();
        south.setLayout(new BorderLayout());
        JPanel productoInfo = new JPanel();
        productoInfo.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 5));
        productoInfo.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        JPanel ofrecerProd = new JPanel();
        ofrecerProd.setLayout(new GridLayout(1, 2));

        obtenerLista = new JButton("Obtener lista");
        productoInfo.add(new JLabel("Descripcion: "));
        descripcionProd = new JTextArea();
        JScrollPane scroll = new JScrollPane(descripcionProd);
        descripcionProd.setEditable(false);
        descripcionProd.setLineWrap(true);
        descripcionProd.setWrapStyleWord(true);
        // scroll.setPreferredSize(new Dimension(usuario.getWidth(),
        // usuario.getHeight()));

        productoInfo.add(obtenerLista);
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

        principal.setSize(400, 400);
        principal.setVisible(true);
        principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializatePanels();

        asignarActionListener(controller);
        asignarListSelectionListener(controller);
    }

    public void asignarActionListener(ActionListener controlador) {

        conectar.addActionListener(controlador);
        salir.addActionListener(controlador);
        ponerALaVenta.addActionListener(controlador);
        obtenerLista.addActionListener(controlador);
        ofrecer.addActionListener(controlador);

    }

    public void asignarListSelectionListener(ListSelectionListener controlador) {

        lista.addListSelectionListener(controlador);
    }

    private void initializatePanels() {
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
        String emailTest = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

        if (!usuario.getText().matches(emailTest)) {
            JOptionPane.showMessageDialog(huehuenose, "Error en email! Insertar email correcto");
        }

        return email.getText();
    }

    public String getTelefono() {

        int resultado = 0;

        try {

            resultado = Integer.parseInt(telefono.getText());
            if (resultado < 0) {
                throw new Exception();
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(huehuenose, "Error en tel! Insertar numero correcto");
            System.out.println("Hay problemas con el año de la oferta");

        }

        return resultado;
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
            if (resultado) {
                throw new Exception();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(huehuenose, "Error en precio inical! Insertar numero correcto");
            System.out.println("Hay problemas con el precio inicial");
        }

        return resultado;
    }

    public int getAñoOferta() {
        int resultado = 0;

        try {

            resultado = Integer.parseInt(año.getText());
            if (resultado < 2019) {
                throw new Exception();
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(huehuenose, "Error en a;o! Insertar numero correcto");
            System.out.println("Hay problemas con el año de la oferta");

        }

        return resultado;
    }

    public int getMesOferta() {
        int resultado = 0;

        try {

            resultado = Integer.parseInt(mes.getText());
            if (resultado < 0 || resultado > 12) {
                throw new Exception();
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(huehuenose, "Error en Mes! Insertar numero correcto");
            System.out.println("Hay problemas con el mes de la oferta");
        }

        return resultado;
    }

    public int getDiaOferta() {
        int resultado = 0;

        try {

            resultado = Integer.parseInt(dia.getText());
            if (resutlado < 0 || resultado > 31) {
                throw new Exception();
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(huehuenose, "Error en dia! Insertar numero correcto");
            System.out.println("Hay problemas con el dia de la oferta");
        }

        return resultado;
    }

    public int getHoraOferta() {
        int resultado = 0;

        try {

            resultado = Integer.parseInt(hora.getText());
            if (resultado < 0 || resutlado > 12) {
                throw new Exception();
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(huehuenose, "Error en hora! Insertar numero correcto");
            System.out.println("Hay problemas con el precio inicial");
        }

        return resultado;
    }

    public int getMinutoOferta() {
        int resultado = 0;

        try {

            resultado = Integer.parseInt(minuto.getText());
            if (resultado < 0 || resultado > 60) {
                throw new Exception();
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(huehuenose, "Error en minuto! Insertar numero correcto");
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