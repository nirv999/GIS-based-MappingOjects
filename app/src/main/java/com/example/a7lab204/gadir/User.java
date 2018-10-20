package com.example.a7lab204.gadir;

/**
 * Created by slavik on 20/03/2018.
 */

public class User {
    private String username , password, auth, id;
    public User(){};

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

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String username, String password, String auth, String id) {

        this.username = username;
        this.password = password;
        this.auth = auth;
        this.id = id;
    }
    public String toString()
    {
        return this.getUsername();
    }
}
