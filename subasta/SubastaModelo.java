package subasta;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Vector;

public class SubastaModelo implements Servidor {

    Hashtable<String, Cliente> usuarios;
    Hashtable<String, Producto> productos;
    Hashtable<String, Oferta> ofertas;

    public SubastaModelo() {

        usuarios = new Hashtable();
        productos = new Hashtable();
        ofertas = new Hashtable();
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
        return !usuarios.containsKey(nombre);
    }

    public boolean agregaProductoALaVenta( String producto, Producto vender) {
        if (!productos.containsKey(producto)) {

            System.out.println("Agregando un nuevo producto: " + producto);
            productos.put(producto, vender);
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
