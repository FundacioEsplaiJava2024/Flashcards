package com.esplai.flashcards.service.cardlogic;

public class CardModel {
    private String text, backside;
    private Boolean isLiked = false;


    public CardModel() {

    }

    public CardModel(String text) {
        this.text = text;
    }

    public CardModel(String text, Boolean isLiked) {
        this.text = text;
        this.isLiked = isLiked;
    }

    public CardModel(String text, String backside, Boolean isLiked) {
        this.text = text;
        this.backside = backside;
        this.isLiked = isLiked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getBackside() {
        return backside;
    }

    public void setBackside(String backside) {
        this.backside = backside;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }
}
