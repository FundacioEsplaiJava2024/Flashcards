package com.esplai.flashcards.model;

import com.google.gson.annotations.SerializedName;

public class Collection {
    private int id; // Agregar el campo ID
    private String title;
    private String description;

    @SerializedName("public")
    private Boolean isPublic;

    public Collection(String title, String description, boolean isPublic) {
        this.id = id; // Inicializar el campo ID
        this.title = title;
        this.description = description;
        this.isPublic = isPublic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    @Override
    public String toString() {
        return title; // Mostrar el título en el Spinner
    }
}
