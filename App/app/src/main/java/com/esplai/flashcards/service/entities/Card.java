package com.esplai.flashcards.service.entities;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.esplai.flashcards.R;

public class Card {
    private int imageResId;
    private String name;
    private String text;
    private String hashtag;
    private boolean isPrivate;

    // Constructor
    public Card(int imageResId, String name, String text, String hashtag, boolean isPrivate) {
        this.imageResId = imageResId;
        this.name = name;
        this.text = text;
        this.hashtag = hashtag;
        this.isPrivate = isPrivate;
    }

    // Getters y Setters
    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener las referencias de los elementos UI
        EditText eCardName = findViewById(R.id.eCardName);
        EditText eCardText = findViewById(R.id.eCardText);
        EditText eHashtag = findViewById(R.id.eHashtag);
        CheckBox pCard = findViewById(R.id.pCard);

        if (eCardName != null && eCardText != null && eHashtag != null && pCard != null) {
            Card card = new Card(
                    R.drawable.flash_card,
                    eCardName.getText().toString(),
                    eCardText.getText().toString(),
                    eHashtag.getText().toString(),
                    pCard.isChecked()
            );
        } else {
            throw new RuntimeException("Una o más vistas no se encontraron. Revisa tus IDs y el layout.");
        }
    }
    // faltaria conectarlo a a la base de datos
}

