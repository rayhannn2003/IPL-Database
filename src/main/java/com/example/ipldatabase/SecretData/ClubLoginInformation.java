package com.example.ipldatabase.SecretData;

import java.io.Serializable;

public class ClubLoginInformation implements Serializable {
    private String username;
    private String password;

    public ClubLoginInformation(String username, String password) {
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

