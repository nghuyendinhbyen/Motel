package com.example.Motel.Model;

public class LoginRequest {
    private String name;
    private String pass;

    public LoginRequest(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }

    // Getter và Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
