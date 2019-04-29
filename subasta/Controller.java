package subasta;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Controller extends Remote {

  void connectUser() throws RemoteException;

  void putOnSale() throws RemoteException;

  void updateProductList() throws RemoteException;

  void offerOnProduct() throws RemoteException;

  void changeDescription(Producto item) throws RemoteException;

  void updateView() throws RemoteException;

  void disconnect() throws RemoteException;

  String getUser() throws RemoteException;

  void notifyEndBid(Cliente client, Producto prod) throws RemoteException;

}
