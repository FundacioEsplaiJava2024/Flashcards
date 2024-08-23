package com.esplai.flashcards.service.cardlogic;

import java.time.LocalDateTime;
import java.util.List;

public class CardModel {
    private String front, back, username, collection_title;
    private Boolean favourite = false;
    private LocalDateTime createdAt;
    private List<String> hashtags;
    private int id;


    public CardModel() {

    }

    public CardModel(String front) {
        this.front = front;
    }

    public CardModel(String front, Boolean favourite) {
        this.front = front;
        this.favourite = favourite;
    }

    public CardModel(String front, Boolean favourite, int id) {
        this.front = front;
        this.favourite = favourite;
        this.id = id;
    }

    public CardModel(String front, String back, Boolean favourite, LocalDateTime createdAt, String collection_title, String username) {
        this.front = front;
        this.back = back;
        this.favourite = favourite;
        this.createdAt = createdAt;
        this.collection_title = collection_title;
        this.username = username;
    }
    public CardModel(String front, String back, Boolean favourite, LocalDateTime createdAt, List<String> hashtags,String collection_title, String username) {
        this.front = front;
        this.back = back;
        this.favourite = favourite;
        this.createdAt = createdAt;
        this.hashtags = hashtags;
        this.collection_title = collection_title;
        this.username = username;
    }

    public CardModel(String front, String back, Boolean favourite) {
        this.front = front;
        this.back = back;
        this.favourite = favourite;
    }

    public int getId(){return id;}

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
