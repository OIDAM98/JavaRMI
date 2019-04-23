package subasta;

import java.io.Serializable;

/**
 * General information about a client:
 * Name
 * Address
 * Email
 * Phone number
 * Nickname
 */

public class Cliente implements Serializable {

    String nombre;
    String direccion;
    String email;
    String telefono;
    String nickname;

    public Cliente(String nombre, String direccion, String email, String telefono, String nickname) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.email = email;
        this.telefono = telefono;
        this.nickname = nickname;
    }

}
