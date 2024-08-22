package com.esplai.flashcards.model;

public class Card {

    private String front;
    private String backside;
    private int collection_id;

    public Card(String front, String backside, int collectionId) {
        this.front = front;
        this.backside = backside;
        this.collection_id = collectionId;
    }


    // Getters y Setters
    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBackside() {
        return backside;
    }

    public void setBackside(String backside) {
        this.backside = backside;
    }

    public int getCollectionId() {
        return collection_id;
    }

    public void setCollectionId(int collection_id) {
        this.collection_id = collection_id;
    }
}
