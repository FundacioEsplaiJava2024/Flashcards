package com.esplai.flashcards.service.entities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

import com.esplai.flashcards.MainActivity;
import com.esplai.flashcards.R;


public class AddCardActivity extends MainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            throw new RuntimeException("Una o más vistas no se encontraron.");
        }
    }
}
