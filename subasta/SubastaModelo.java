package subasta;

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

        Vector resultado;

        resultado = new Vector(productos.values());

        return resultado;
    }
}
