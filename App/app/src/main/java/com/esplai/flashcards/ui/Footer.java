package com.esplai.flashcards.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.esplai.flashcards.MainActivity;
import com.esplai.flashcards.service.cardlogic.AddCardActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.esplai.flashcards.R;

public class Footer extends AppCompatActivity {
    private BottomNavigationView bottomNavView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.footer);

        bottomNavView = findViewById(R.id.bottomNavView);
        fab = findViewById(R.id.fab);

        bottomNavView.post(() -> {
            bottomNavView.setOnItemSelectedListener(item -> {
                switch (item.getItemId()) {
                    case R.id.nav_collections:
                        Log.d("Footer", "Collections button pressed");
                        Intent collectionsIntent = new Intent(Footer.this, CollectionsActivity.class);
                        startActivity(collectionsIntent);
                        return true;
                    case R.id.nav_add_card:
                        Log.d("Footer", "Add Card button pressed");
                        Intent addCardIntent = new Intent(Footer.this, AddCardActivity.class);
                        startActivity(addCardIntent);
                        return true;
                    default:
                        return false;
                }
            });
        });

        fab.post(() -> {
            fab.setOnClickListener(view -> {
                Log.d("Footer", "FAB pressed - Going to HomeActivity");
                Intent intent = new Intent(Footer.this, MainActivity.class);
                startActivity(intent);
            });
        });

    }
}
