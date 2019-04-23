package subasta;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SubastaControlador implements ActionListener, ListSelectionListener {

    SubastaVista vista;
    Servidor modelo;
    Hashtable<String, String> listaDescripcion;

    public SubastaControlador(SubastaVista v) {
        vista = v;
        try {
            Registry registry = LocateRegistry.getRegistry();
            modelo = (Servidor) registry.lookup("subasta");
        } catch (Exception e) {
            System.err.println("Server error");
        }
    }

    public void actionPerformed(ActionEvent evento) {

        String usuario;
        String producto;
        float monto;

        String printy;

        System.out.println("<<" + evento.getActionCommand() + ">>");

        if (evento.getActionCommand().equals("Salir")) { // Exit app
            // Disconnect from server TO-DO
            System.exit(1);
        } else if (evento.getActionCommand().equals("Conectar")) { // Connect to server
            usuario = vista.getUsuario();
            System.out.println("Registrarse como usuario: " + usuario);

            try {

                if (modelo.existeUsuario(usuario)) {
                    int result = JOptionPane.showConfirmDialog(null, vista.getUserPanel(), "Dar de alta un usuario",
                            JOptionPane.OK_CANCEL_OPTION);
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
                            JOptionPane.showMessageDialog(null, "Cliente dado de alta con éxito!",
                                    "Dar de alta cliente", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Hubo un error al dar de alta el cliente.",
                                    "Dar de alta cliente", JOptionPane.ERROR_MESSAGE);
                        }

                    }
                }

            } catch (RemoteException ex) {
                System.out.println("Fallo en el servidor");
            }

        } else if (evento.getActionCommand().equals("Poner a la venta")) { // Offer a product
            usuario = vista.getUsuario();

            try {

                int result = JOptionPane.showConfirmDialog(null, vista.getProductPanel(), "Datos para ofrecer producto",
                        JOptionPane.OK_CANCEL_OPTION);
                producto = vista.getProducto();
                if (modelo.existeProducto(producto)) {

                    System.out.println("Haciendo oferta del producto: " + producto);

                    if (result == JOptionPane.OK_OPTION) {
                        monto = vista.getPrecioInicial();
                        String descripcion = vista.getDescripcionProducto();
                        int año = vista.getAñoOferta();
                        int mes = vista.getMesOferta();
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

            } catch (RemoteException ex) {
                System.out.println("Fallo en el servidor");
            }

        } else if (evento.getActionCommand().equals("Obtener lista")) { // Get catalog of products
            try {

                Vector<Producto> lista = modelo.obtieneCatalogo();
                Enumeration it;
                Producto info;
                listaDescripcion = new Hashtable();
                vista.reinicializaListaProductos();
                it = lista.elements();
                while (it.hasMoreElements()) {
                    info = (Producto) it.nextElement();
                    listaDescripcion.put(info.producto, info.descripcion);
                    vista.agregaProducto(info);
                }

            } catch (RemoteException ex) {
                System.out.println("Fallo en el servidor");
            }
        } else if (evento.getActionCommand().equals("Ofrecer")) { // Bid for a product
            try {
                Producto offer = vista.getProductoSeleccionado();
                monto = vista.getMontoOfrecido();
                usuario = vista.getUsuario();
                modelo.agregaOferta(usuario, offer.producto, monto);
            } catch (RemoteException ex) {
                System.err.println("Fallo en el servidor");
            }
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            JList lista = (JList) e.getSource();
            Producto item = (Producto) lista.getSelectedValue();
            if (item != null) {
                System.out.println(item);
                String context = (String) listaDescripcion.get(item.producto);
                vista.desplegarDescripcion(context);
            }
        }
    }
}
