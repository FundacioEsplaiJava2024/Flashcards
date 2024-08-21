package com.esplai.flashcards.service.cardlogic;

import java.time.LocalDateTime;

public class CardModel {
    private String front, back, username, collection_title;
    private Boolean favourite = false;
    private LocalDateTime createdAt;


    public CardModel() {

    }

    public CardModel(String front) {
        this.front = front;
    }

    public CardModel(String front, Boolean favourite) {
        this.front = front;
        this.favourite = favourite;
    }

    public CardModel(String front, String back, Boolean favourite, LocalDateTime createdAt, String collection_title, String username) {
        this.front = front;
        this.back = back;
        this.favourite = favourite;
        this.createdAt = createdAt;
        this.collection_title = collection_title;
        this.username = username;
    }

    public CardModel(String front, String back, Boolean favourite) {
        this.front = front;
        this.back = back;
        this.favourite = favourite;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public Boolean getLiked() {
        return favourite;
    }

    public void setLiked(Boolean liked) {
        favourite = liked;
    }
}
