package subasta;

import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.awt.event.ActionEvent;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class SubastaControlador implements ActionListener, ListSelectionListener {

    SubastaVista vista;
    SubastaModelo modelo;
    Hashtable listaConPrecios;

    public SubastaControlador(SubastaVista v, SubastaModelo m) {
        vista = v;
        modelo = m;
    }

    public void actionPerformed(ActionEvent evento) {

        String usuario;
        String producto;
        float monto;

        String printy;

        System.out.println("<<" + evento.getActionCommand() + ">>");

        if (evento.getActionCommand().equals("Salir")) { //Exit app
            //Disconnect from server TO-DO
            System.exit(1);
        } else if (evento.getActionCommand().equals("Conectar")) { //Connect to server
            usuario = vista.getUsuario();
            System.out.println("Registrarse como usuario: " + usuario);
            if (modelo.existeUsuario(usuario)) {
                int result = JOptionPane.showConfirmDialog(null, vista.getUserPanel(),
                        "Dar de alta un usuario", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String direccion = vista.getDireccion();
                    String email = vista.getEmail();
                    String telefono = vista.getTelefono();
                    String nickname = vista.getNickname();
                    vista.resetUserPanel();
                    Cliente nuevoCliente = new Cliente(usuario, direccion, email, telefono, nickname);
                    printy = String.format("%s %s %s %s %s", usuario, direccion, email, telefono, nickname);
                    System.out.println(printy);

                    if (modelo.registraUsuario(usuario, nuevoCliente)) {
                        JOptionPane.showMessageDialog(null, "Cliente dado de alta con éxito!", "Dar de alta cliente",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Hubo un error al dar de alta el cliente.",
                                "Dar de alta cliente", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }

        } else if (evento.getActionCommand().equals("Poner a la venta")) { //Offer a product
            usuario = vista.getUsuario();
            producto = vista.getProducto();
            System.out.println("Haciendo oferta del producto: " + producto);
            if (true) {
                int result = JOptionPane.showConfirmDialog(null, vista.getProductPanel(),
                        "Datos para ofrecer producto", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    monto = vista.getPrecioInicial();
                    String descripcion = vista.getDescripcionProducto();
                    int año = vista.getAñoOferta();
                    int mes = vista.getMesOferta() ;
                    int dia = vista.getDiaOferta();
                    int hora = vista.getHoraOferta();
                    int minutos = vista.getMinutoOferta();
                    vista.resetProductPanel();
                    LocalDateTime fecha = LocalDateTime.of(año, mes, dia, hora, minutos);
                    Producto ofrecer = new Producto(usuario, producto, descripcion, monto, fecha);

                    if (modelo.agregaProductoALaVenta(producto, ofrecer)) {
                        JOptionPane.showMessageDialog(null, "Producto ofrecido con éxito!", "Ofrecer producto",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Hubo un error al ofrecer el producto.",
                                "Ofrecer producto", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }

        } else if (evento.getActionCommand().equals("Obtener lista")) { //Get catalog of products
            Vector<Producto> lista = modelo.obtieneCatalogo();
            Enumeration it;
            Producto info;
            listaConPrecios = new Hashtable();
            vista.reinicializaListaProductos();
            it = lista.elements();
            while (it.hasMoreElements()) {
                info = (Producto) it.nextElement();
                listaConPrecios.put(info.producto, String.valueOf(info.precioActual));
                vista.agregaProducto(info);
            }
        } else if (evento.getActionCommand().equals("Ofrecer")) { //Bid for a product
            Producto offer = vista.getProductoSeleccionado();
            monto = vista.getMontoOfrecido();
            usuario = vista.getUsuario();
            modelo.agregaOferta(usuario, offer.producto, monto);
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            JList lista = (JList) e.getSource();
            Producto item = (Producto) lista.getSelectedValue();
            if (item != null) {
                System.out.println(item);
                String precio = (String) listaConPrecios.get(item.producto);
                vista.desplegarPrecio(precio);
            }
        }
    }
}
