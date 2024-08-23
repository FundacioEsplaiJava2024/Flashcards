package com.esplai.flashcards.model;

import java.util.Collections;
import java.util.List;

public class Card {

    private String front;
    private String backside;
    private int collection_id;
    private List<String> hashtags;

    public Card(String front, String backside, int collectionId, List<String>hashtags) {
        this.front = front;
        this.backside = backside;
        this.collection_id = collectionId;
        this.hashtags = hashtags;
    }

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

    public String getHashtags() {
        return hashtags.toString();
    }

    public void setHashtags(String hashtags) {
        this.hashtags = Collections.singletonList(hashtags);
    }
}
