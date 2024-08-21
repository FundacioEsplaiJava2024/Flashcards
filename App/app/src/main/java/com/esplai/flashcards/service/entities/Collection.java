package com.esplai.flashcards.service.entities;

import com.esplai.flashcards.service.cardlogic.CardModel;

import java.util.List;

public class Collection {
    private int collId;
    private String title, description;
    private List<CardModel> cardList;
    private boolean isPublic;

    public Collection(String title) {
        this.title = title;
    }

    public Collection(String title, List<CardModel> cards) {
        this.title = title;
        this.cardList = cards;
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

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }


}
