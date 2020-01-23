package erp.domain;

import java.io.Serializable;

public class User implements Serializable {
    private String u_username;
    private String u_password;
    private int u_level;

    public User() {
    }

    public User(String username, String password) {
        this.u_username = username;
        this.u_password = password;
    }

    public int getU_level() {
        return u_level;
    }

    public void setU_level(int u_level) {
        this.u_level = u_level;
    }

    public String getU_username() {
        return u_username;
    }

    public void setU_username(String u_username) {
        this.u_username = u_username;
    }

    public String getU_password() {
        return u_password;
    }

    public void setU_password(String u_password) {
        this.u_password = u_password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + u_username + '\'' +
                ", password='" + u_password + '\'' +
                ", level=" + u_level +
                '}';
    }
}
