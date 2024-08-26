package com.esplai.flashcards.service.cardlogic;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;
import java.util.List;

public class CardModel {
    private int id;
    private String front;

    private String back;

    private int collectionId;

    private List<String> hashtags;

    private Boolean favourite = false;
    private LocalDateTime createdAt;
    private String collection_title;
    private String username;

    private int user_id;

    // Add getter and setter methods for user_id
    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }


    public CardModel() {}

    public CardModel(String front, String back, int collectionId, List<String> hashtags) {
        this.front = front;
        this.back = back;
        this.collectionId = collectionId;
        this.hashtags = hashtags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCollectionTitle() {
        return collection_title;
    }

    public void setCollectionTitle(String collection_title) {
        this.collection_title = collection_title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getLiked() {
        return favourite;
    }

    public void setLiked(Boolean liked) {
        favourite = liked;
    }
}
