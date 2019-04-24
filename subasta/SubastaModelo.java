package subasta;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class SubastaModelo implements Servidor {

    Hashtable<String, Cliente> usuarios;
    Hashtable<String, Producto> productos;
    Hashtable<String, Oferta> ofertas;
    List<Controller> clients;

    public SubastaModelo() {

        usuarios = new Hashtable();
        productos = new Hashtable();
        ofertas = new Hashtable();
        clients = new ArrayList<>();

    }

    public void updateClients() {
        if(!obtieneCatalogo().isEmpty()) {
            Controller current = null;
            try {
                for(Controller c : clients) {
                    c.updateView();
                    System.out.println("se actualizo alguien");
                    current = c;
                }
            }
            catch (RemoteException ex) {
                System.out.println("Hubo un error al actualizar cliente " + current);
                this.unsubscribe(current);
            }
        }
    }

    public void subscribe(Controller c) {
        clients.add(c);
        System.out.println("Se suscribio alguien");
        System.out.println(clients);
        updateClients();
    }

    public void unsubscribe(Controller c) {
        System.out.println("removiendo cliente " + clients.indexOf(c));
        clients.remove(c);
        System.out.println("Un cliente se desconecto");
    }

    public boolean registraUsuario(String nombre, Cliente client) {

        if (!usuarios.containsKey(nombre)) {

            System.out.println("Agregando un nuevo usuario: " + nombre);
            usuarios.put(nombre, client);
            return true;

        } else

            return false;
    }

    public boolean existeUsuario(String nombre) {
        return usuarios.containsKey(nombre);
    }

    public boolean agregaProductoALaVenta( String producto, Producto vender) {
        if (!productos.containsKey(producto)) {

            System.out.println("Agregando un nuevo producto: " + producto);
            productos.put(producto, vender);
            this.updateClients();
            return true;

        } else
            return false;
    }

    public boolean existeProducto(String producto) {
        return !productos.containsKey(producto);
    }

    public boolean agregaOferta(String comprador, String producto, float monto) {

        if (productos.containsKey(producto)) {

            Producto infoProd = productos.get(producto);
            Cliente user = usuarios.get(comprador);

            if (infoProd.actualizaPrecio(monto)) {

                ofertas.put(producto + comprador, new Oferta(user, infoProd));
                this.updateClients();
                return true;

            } else

                return false;

        } else

            return false;
    }

    public Vector obtieneCatalogo() {

        return new Vector(productos.values());

    }

    public static void main(String... args) {
        try {

            SubastaModelo obj = new SubastaModelo();
            Servidor stub = (Servidor) UnicastRemoteObject.exportObject(obj, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.bind("subasta", stub);

            System.out.println("Server ready");
        }
        catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
