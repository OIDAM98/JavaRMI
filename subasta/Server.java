package subasta;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Server extends Remote {

    boolean registerUser(String name, User client) throws RemoteException;
    boolean userExists(String name) throws RemoteException;
    boolean addProductToAuction(String user, String product, Product sell) throws RemoteException;
    boolean productExists(String product) throws RemoteException;
    boolean addOffer(String buyer, String product, float amount) throws RemoteException;
    List<Product> getCatalog() throws RemoteException;
    void updateClients() throws RemoteException;
    void subscribe(Controller c) throws RemoteException;
    void unsubscribe(Controller c) throws RemoteException;

}
