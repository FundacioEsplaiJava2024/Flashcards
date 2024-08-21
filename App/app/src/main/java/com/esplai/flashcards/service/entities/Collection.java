package com.esplai.flashcards.service.entities;

import com.esplai.flashcards.service.cardlogic.CardModel;

import java.time.LocalDateTime;
import java.util.List;

public class Collection {
    private int collId;
    private String title, description, username;
    private List<CardModel> cardList;
    private boolean publicCollection;
    private LocalDateTime createdAt;

    public Collection(String title) {
        this.title = title;
    }

    public Collection(String title, List<CardModel> cards) {
        this.title = title;
        this.cardList = cards;
    }

    public Collection(int collId, String title, String description, boolean publicCollection, LocalDateTime createdAt, String username) {
        this.collId = collId;
        this.title = title;
        this.description = description;
        this.publicCollection = publicCollection;
        this.createdAt = createdAt;
        this.username = username;
    }

    public int getCollId() {
        return collId;
    }

    public void setCollId(int collId) {
        this.collId = collId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CardModel> getCardList() {
        return cardList;
    }

    public void setCardList(List<CardModel> cardList) {
        this.cardList = cardList;
    }

    public boolean isPublicCollection() {
        return publicCollection;
    }

    public void setPublicCollection(boolean publicCollection) {
        this.publicCollection = publicCollection;
    }


}
