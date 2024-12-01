package com.example.Motel.Model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UserResponse {
    @SerializedName("accounts")
    private List<User> accounts;

    public List<User> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<User> accounts) {
        this.accounts = accounts;
    }
}
