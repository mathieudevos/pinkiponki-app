package com.mattikettu.pinkiponki.objects;

/**
 * Created by mathieu on 15/06/2016.
 */
public class LoginUser {

    String username;

    String password;

    public LoginUser(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
