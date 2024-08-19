package com.esplai.flashcards.model;

import com.google.gson.annotations.SerializedName;

public class AccesToken {
    @SerializedName("token")
    private String token;
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
