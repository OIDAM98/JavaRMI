package subasta;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JOptionPane;

public class SubastaControlador extends UnicastRemoteObject implements Controller {

    SubastaVista vista;
    Servidor modelo;
    Hashtable<String, String> listaDescripcion;

    public SubastaControlador(SubastaVista v) throws RemoteException {
        vista = v;
        try {
            Registry registry = LocateRegistry.getRegistry();
            modelo = (Servidor) registry.lookup("subasta");
        } catch (Exception e) {
            System.err.println("Server error");
        }
    }

    public void connectUser() {

        try {

            String usuario = vista.getUsuario();
            System.out.println("Registrarse como usuario: " + usuario);

            if (!modelo.existeUsuario(usuario)) {
                int result = JOptionPane.showConfirmDialog(null,
                        vista.getUserPanel(),
                        "Dar de alta un usuario",
                        JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        String direccion = vista.getDireccion();
                        String email = vista.getEmail();
                        String telefono = vista.getTelefono();
                        String nickname = vista.getNickname();
                        Cliente nuevoCliente = new Cliente(usuario, direccion, email, telefono, nickname);
                        String printy = String.format("%s %s %s %s %s", usuario, direccion, email, telefono, nickname);
                        System.out.println(printy);

                        if (modelo.registraUsuario(usuario, nuevoCliente)) {
                            modelo.subscribe(this);
                            JOptionPane.showMessageDialog(null,
                                    "Cliente dado de alta con éxito!",
                                    "Dar de alta cliente",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Hubo un error al dar de alta el cliente.",
                                    "Dar de alta cliente",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null,
                                "Campo debe de contener solamente numeros",
                                "Dar de alta cliente",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(null,
                                ex.getMessage(),
                                "Dar de alta cliente",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    finally {
                        vista.resetUserPanel();
                    }

                }
            }
            else {
                modelo.subscribe(this);
            }

        }
        catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "Dar de alta cliente",
                    JOptionPane.ERROR_MESSAGE);
        }
        catch (RemoteException ex) {
            JOptionPane.showMessageDialog(null,
                    "Fallo en el servidor",
                    "Dar de alta cliente",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void putOnSale() throws RemoteException {
        String usuario = vista.getUsuario();

        int result = JOptionPane.showConfirmDialog(null, vista.getProductPanel(), "Datos para ofrecer producto",
                JOptionPane.OK_CANCEL_OPTION);
        String producto = vista.getProducto();

        if(producto.length() != 0) {

            if (!modelo.existeProducto(producto)) {

                System.out.println("Haciendo oferta del producto: " + producto);

                if (result == JOptionPane.OK_OPTION) {
                    try{
                        float monto = vista.getPrecioInicial();
                        String descripcion = vista.getDescripcionProducto();
                        int año = vista.getAñoOferta();
                        int mes = vista.getMesOferta();
                        int dia = vista.getDiaOferta();
                        int hora = vista.getHoraOferta();
                        int minutos = vista.getMinutoOferta();

                        LocalDateTime now = LocalDateTime.now();
                        if(minutos <= now.getMinute() + 1)
                            throw new IllegalArgumentException("La subasta debe de tener al menos dos minutos de tiempo activo");

                        LocalDateTime fecha = LocalDateTime.of(año, mes, dia, hora, minutos);
                        Producto ofrecer = new Producto(usuario, producto, descripcion, monto, fecha);

                        if (modelo.agregaProductoALaVenta(usuario, producto, ofrecer)) {
                            JOptionPane.showMessageDialog(null,
                                    "Producto ofrecido con éxito!",
                                    "Ofrecer producto",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Hubo un error al ofrecer el producto.",
                                    "Ofrecer producto",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null,
                                "Campo debe de contener solamente numeros",
                                "Subastar producto",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(null,
                                ex.getMessage(),
                                "Subastar producto",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    finally {
                        vista.resetProductPanel();
                    }
                }
            }
            else {
                JOptionPane.showMessageDialog(null,
                        "Producto ya se encuentra en venta.",
                        "Subastar producto",
                        JOptionPane.ERROR_MESSAGE);
            }

        }
        else {
            JOptionPane.showMessageDialog(null,
                    "Ingrese producto a subastar.",
                    "Subastar producto",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public void updateProductList() throws RemoteException {

        List<Producto> lista = modelo.obtieneCatalogo();
        listaDescripcion = new Hashtable<>();
        vista.reinicializaListaProductos();

        lista.forEach(prod -> {
            listaDescripcion.put(prod.producto, prod.descripcion);
            vista.agregaProducto(prod);
        });

    }

    public void offerOnProduct() throws RemoteException {
        try {
            Producto offer = vista.getProductoSeleccionado();
            float monto = vista.getMontoOfrecido();
            String usuario = vista.getUsuario();

            modelo.agregaOferta(usuario, offer.producto, monto);
        }
        catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(null,
                    "Debe seleccionar un producto para ofrecer.",
                    "Ofrecer en producto",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void changeDescription(Producto item) {
        if (item != null) {
            System.out.println(item);
            String context = listaDescripcion.get(item.producto);
            vista.desplegarDescripcion(context);
        }
    }

    public void updateView() throws RemoteException {
        this.updateProductList();
    }

    public void disconnect() {
        try{
            modelo.unsubscribe(this);
        }
        catch (RemoteException ex) {
            System.out.println("Error al desconectarse del servidor");
            ex.printStackTrace();
        }
    }

}
