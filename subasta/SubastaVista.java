package subasta;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionListener;

public class SubastaVista {

    JFrame principal;

    JTextField usuario;
    JTextField direccion;
    JTextField email;
    JTextField telefono;
    JTextField nickname;
    JPanel userPanel;

    JTextField producto;
    JTextField descripcion;
    JTextField precioInicial;
    JTextField año;
    JTextField mes;
    JTextField dia;
    JTextField hora;
    JTextField minuto;
    JPanel productPanel;

    JTextField monto;
    DefaultComboBoxModel productos;
    JLabel precioActual;
    JList lista;
    JButton conectar;
    JButton salir;
    JButton ponerALaVenta;
    JButton obtenerLista;
    JButton ofrecer;

    public SubastaVista() {

        // JFrame.setDefaultLookAndFeelDecorated( true );
        Container panel;

        principal = new JFrame("Cliente Subasta");
        panel = principal.getContentPane();
        panel.setLayout(new GridLayout(0, 2));

        usuario = new JTextField();
        panel.add(new JLabel("Nombre del usuario"));
        panel.add(usuario);
        conectar = new JButton("Conectar");

        salir = new JButton("Salir");
        panel.add(conectar);
        panel.add(salir);

        producto = new JTextField();
        precioInicial = new JTextField();
        panel.add(new JLabel("Producto a ofrecer"));
        panel.add(producto);
        panel.add(new JLabel("Precio inicial"));
        panel.add(precioInicial);

        ponerALaVenta = new JButton("Poner a la venta");
        panel.add(ponerALaVenta);
        panel.add(new JLabel());

        productos = new DefaultComboBoxModel();
        lista = new JList(productos); // data has type Object[]
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setLayoutOrientation(JList.VERTICAL);
        JScrollPane listaScroller = new JScrollPane(lista);
        listaScroller.setPreferredSize(new Dimension(250, 80));
        obtenerLista = new JButton("Obtener lista");
        panel.add(obtenerLista);
        panel.add(listaScroller);

        precioActual = new JLabel();
        panel.add(new JLabel("Precio actual"));
        panel.add(precioActual);

        monto = new JTextField();
        ofrecer = new JButton("Ofrecer");
        panel.add(ofrecer);
        panel.add(monto);

        principal.setSize(400, 400);
        principal.setVisible(true);
        principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializatePanels();
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
        descripcion = new JTextField();
        productPanel.add(new JLabel("Descripcion:"));
        productPanel.add(descripcion);
        //precioInicial = new JTextField();
        //productPanel.add(new JLabel("Precio inicial:"));
        //productPanel.add(precioInicial);

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

    public void desplegarPrecio(String precio) {

        precioActual.setText(precio);
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
}