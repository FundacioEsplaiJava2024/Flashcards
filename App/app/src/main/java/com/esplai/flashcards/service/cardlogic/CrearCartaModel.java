package com.esplai.flashcards.service.cardlogic;

import java.util.Collections;
import java.util.List;

public class CrearCartaModel {
    private String front;
    private String backside;
    private int collection_id;
    private List<String> hashtags;



    public <T> CrearCartaModel(String front, String backside, int collectionId, List<T> ts) {
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
