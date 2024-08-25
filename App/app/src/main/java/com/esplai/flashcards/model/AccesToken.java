package com.esplai.flashcards.model;

import com.google.gson.annotations.SerializedName;

public class AccesToken {
    @SerializedName("token")
    private static String token;
    public static String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
