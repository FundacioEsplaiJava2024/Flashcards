package com.esplai.flashcards.service.entities;

public class Collection {
    private int id;
    private String name;

    public Collection(int id, String name) {

        this.id = id;
        this.name = name;

    }

    public int getId() {
        return id;
    }
    public String getName(){
        return name;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return "Collection{" +
                "id = " + id +
                ", name = " + name + "}";

    }


}
