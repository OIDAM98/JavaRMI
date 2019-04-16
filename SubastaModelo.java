
import java.util.Date;
import java.time.Instant;
import java.util.Hashtable;
import java.util.Vector;

public class SubastaModelo {

    Hashtable usuarios;
    Hashtable productos;
    Hashtable ofertas;

    public SubastaModelo() {

        usuarios = new Hashtable();
        productos = new Hashtable();
        ofertas = new Hashtable();
    }

    public boolean registraUsuario( String nombre ) {

        if( !usuarios.containsKey(nombre) ) {

            System.out.println( "Agregando un nuevo usuario: " + nombre );
            usuarios.put( nombre, nombre );
            return true;

        } else

            return false;
    } 

    public boolean agregaProductoALaVenta( String vendedor, String producto,
                                           float precioInicial ) {
        if( !productos.containsKey(producto) ) {

            System.out.println( "Agregando un nuevo producto: " + producto );
            productos.put( producto,
                           new Producto( vendedor,
						    producto,
						    precioInicial ) );
            return true;

        } else

            return false;
    }

    public boolean agregaOferta( String comprador, String producto,
                                 float monto ) {

        if( productos.containsKey(producto) ) {

            Producto infoProd;
            infoProd = (Producto) productos.get(producto);

            if( infoProd.actualizaPrecio(monto) ) {

                ofertas.put( producto + comprador,
                        new Historico( comprador,
                                producto,
                                Date.from(Instant.now()),
                                monto) );
                return true;

            } else

                return false;

        } else

            return false;
    }

    public Vector obtieneCatalogo() {

	Vector resultado;

	resultado = new Vector( productos.values() );

	return resultado;
    }
}
