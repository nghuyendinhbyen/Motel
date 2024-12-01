package com.example.Motel.Model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("name")
    private String name;

    @SerializedName("pass")
    private String pass;

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
