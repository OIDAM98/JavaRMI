package subasta;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class SubastaModelo implements Servidor {

    Hashtable<String, Cliente> usuarios;
    Hashtable<String, Producto> productos;
    List<Bid> bidHistory;
    List<Controller> clients;

    public SubastaModelo() {

        usuarios = new Hashtable<>();
        productos = new Hashtable<>();
        bidHistory = new ArrayList<>();
        clients = new ArrayList<>();

    }

    public void updateClients() {
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

    public boolean agregaProductoALaVenta( String usuario, String producto, Producto vender) {
        if (!productos.containsKey(producto)) {

            System.out.println("Agregando un nuevo producto: " + producto);
            productos.put(producto, vender);
            bidHistory.add(new Bid(usuario, producto, vender.precioInicial));

            ZoneId znid = ZoneId.systemDefault();
            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = vender.fechaCierre;
            Instant endTime = end.atZone(znid).toInstant();
            long startTime = Instant.now().toEpochMilli();
            long durationSecond = endTime.minusMillis(startTime).toEpochMilli();

            System.out.println("start: " + start + " to milli " + startTime);
            System.out.println("end: " + end + " to milli " + endTime.toEpochMilli());
            System.out.println("duration " + durationSecond);

            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {

                    System.out.println("task monsta");
                    vender.setActive(false);
                    System.out.println("actualizando clientes");
                    updateClients();
                    System.out.println("se actualizaron c:");
                    timer.cancel();
                    timer.purge();

                }
            };

            timer.schedule(task, durationSecond, 1000);

            this.updateClients();
            return true;

        } else
            return false;
    }

    public boolean existeProducto(String producto) {
        return productos.containsKey(producto);
    }

    public boolean agregaOferta(String comprador, String producto, float monto) {

        if (productos.containsKey(producto)) {

            Producto infoProd = productos.get(producto);
            Cliente user = usuarios.get(comprador);

            if (infoProd.actualizaPrecio(monto, user.nombre)) {

                bidHistory.add(new Bid(user.nombre, infoProd.producto, infoProd.precioActual));
                this.updateClients();
                return true;

            } else

                return false;

        } else

            return false;
    }

    public List<Producto> obtieneCatalogo() {

        return productos
                .values()
                .stream()
                .filter(p -> p.isActive())
                .collect(Collectors.toList());

    }

    public List<Bid> getBidHistory() {
        List<Bid> sorted = new ArrayList<>(bidHistory);
        sorted.sort(
                Comparator.comparing(Bid::getProduct)
                        .thenComparing(Bid::getPrice)
        );
        return sorted;
    }

    public void exitApp() {
        System.out.println("Ofertas durante el periodo");
        this.getBidHistory().forEach(System.out::println);
        try{
            PrintWriter outFile = new PrintWriter("historial.txt");
            this.getBidHistory().forEach(outFile::println);
            outFile.close();
        }
        catch (IOException ex) {
            System.out.println("Problema al escribir al archivo");
        }
        if(!clients.isEmpty()) {
            clients.forEach(this::unsubscribe);
        }
    }

    public static void main(String... args) {
        try {

            SubastaModelo obj = new SubastaModelo();
            Servidor stub = (Servidor) UnicastRemoteObject.exportObject(obj, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.bind("subasta", stub);

            System.out.println("Server ready");

            Runtime.getRuntime().addShutdownHook(new Thread( obj::exitApp ));

        }
        catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
