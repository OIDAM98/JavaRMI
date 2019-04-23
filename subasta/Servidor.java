package subasta;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface Servidor extends Remote {

    boolean registraUsuario(String nombre, Cliente client) throws RemoteException;
    boolean existeUsuario(String nombre) throws RemoteException;
    boolean agregaProductoALaVenta( String producto, Producto vender) throws RemoteException;
    boolean existeProducto(String producto) throws RemoteException;
    boolean agregaOferta(String comprador, String producto, float monto) throws RemoteException;
    Vector obtieneCatalogo() throws RemoteException;

}
