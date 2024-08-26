package com.esplai.flashcards.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.esplai.flashcards.R;
import com.esplai.flashcards.service.cardlogic.CreateCardActivity;
import com.esplai.flashcards.service.collection.AddCollectionActivity;

public class AddActivity extends AppCompatActivity {
    private Button btAddCard, btAddCollection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_layout);
        addFooter(savedInstanceState);
        btAddCard = findViewById(R.id.btCreateCard);
        btAddCollection = findViewById(R.id.btCreateCollection);

        btAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, CreateCardActivity.class);
                startActivity(intent);
            }
        });

        btAddCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, AddCollectionActivity.class);
                startActivity(intent);
            }
        });

    }


    private void addFooter(Bundle savedInstance) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.footer, new Footer(), "FOOTER")
                .disallowAddToBackStack()
                .commit();
    }
}
