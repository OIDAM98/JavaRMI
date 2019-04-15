import java.util.Date;

public class Historico {

    String comprador;
    String producto;
    float monto;
    Date fecha;

    public Historico(String c, String p, Date d, float m ) {

        comprador = c;
        producto = p;
        fecha = d;
        monto = m;

   }
}
