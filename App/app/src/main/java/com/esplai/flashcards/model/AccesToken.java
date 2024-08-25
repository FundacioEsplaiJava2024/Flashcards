package com.esplai.flashcards.model;

import com.google.gson.annotations.SerializedName;

public class AccesToken {
    @SerializedName("token")
    private String token;
    private String username;
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
