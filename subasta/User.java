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

public class User implements Serializable {

    String name;
    String direction;
    String email;
    String phone;
    String nickname;

    public User(String nombre, String direccion, String email, String telefono, String nickname) {
        this.name = nombre;
        this.direction = direccion;
        this.email = email;
        this.phone = telefono;
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s\n%s\n%s\n%s", name, direction, email, phone, nickname);
    }

}
