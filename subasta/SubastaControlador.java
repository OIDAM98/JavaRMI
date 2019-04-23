package subasta;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;

public class SubastaControlador implements Controller {

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

    public void disconnectUser() throws RemoteException {
        System.exit(1);
    }

    public void connectUser() throws RemoteException {
        String usuario = vista.getUsuario();
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
                    String printy = String.format("%s %s %s %s %s", usuario, direccion, email, telefono, nickname);
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

        } catch (RemoteException ex) {
            System.out.println("Fallo en el servidor");
        }
    }

    public void putOnSale() throws RemoteException {
        String usuario = vista.getUsuario();

        int result = JOptionPane.showConfirmDialog(null, vista.getProductPanel(), "Datos para ofrecer producto",
                JOptionPane.OK_CANCEL_OPTION);
        String producto = vista.getProducto();
        if (modelo.existeProducto(producto)) {

            System.out.println("Haciendo oferta del producto: " + producto);

            if (result == JOptionPane.OK_OPTION) {
                float monto = vista.getPrecioInicial();
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
                    JOptionPane.showMessageDialog(null, "Hubo un error al ofrecer el producto.", "Ofrecer producto",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        }
    }

    public void updateProductList() throws RemoteException {

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
    }

    public void offerOnProduct() throws RemoteException {
        Producto offer = vista.getProductoSeleccionado();
        float monto = vista.getMontoOfrecido();
        String usuario = vista.getUsuario();
        modelo.agregaOferta(usuario, offer.producto, monto);
    }

    public void changeDescription(Producto item) throws RemoteException {
        if (item != null) {
            System.out.println(item);
            String context = (String) listaDescripcion.get(item.producto);
            vista.desplegarDescripcion(context);
        }
    }

    public void updateView() throws RemoteException {
        this.updateProductList();
    }

}
