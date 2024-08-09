package com.esplai.flashcards.model;

public class User {
    private String username;
    private String password;
    private String email;


    public User(String username, String password, String email) {
        this.username = this.username;
        this.password = this.password;
        this.email = this.email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
