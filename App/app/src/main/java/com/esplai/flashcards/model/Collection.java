package com.esplai.flashcards.model;

public class Collection {
    private String title;

    public Collection(String title, boolean isPublic) {
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String isPublic;
}
