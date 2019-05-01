package subasta.common;

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

    private String name;
    private String direction;
    private String email;
    private String phone;
    private String nickname;

    public User(String n, String d, String e, String p, String nn) {
        this.name = n;
        this.direction = d;
        this.email = e;
        this.phone = p;
        this.nickname = nn;
    }

    /*
    Returns this user's name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s\n%s\n%s\n%s", name, direction, email, phone, nickname);
    }

}
