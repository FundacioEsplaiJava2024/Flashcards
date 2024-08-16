package com.esplai.flashcards.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.esplai.flashcards.MainActivity;
import com.esplai.flashcards.service.cardlogic.AddCardActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.esplai.flashcards.R;

public class Footer extends Fragment {
    private BottomNavigationView bottomNavView;
    private FloatingActionButton fab;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.footer, container, false);

        bottomNavView = view.findViewById(R.id.bottomNavView);
        fab = view.findViewById(R.id.fab);

        bottomNavView.post(() -> {
            bottomNavView.setOnItemSelectedListener(item -> {
                switch (item.getItemId()) {
                    case R.id.nav_collections:
                        Log.d("Footer", "Collections button pressed");
                        Intent collectionsIntent = new Intent(Footer.this.getActivity(), CollectionsActivity.class);
                        startActivity(collectionsIntent);
                        return true;
                    case R.id.nav_add_card:
                        Log.d("Footer", "Add Card button pressed");
                        Intent addCardIntent = new Intent(Footer.this.getActivity(), AddCardActivity.class);
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
                Intent intent = new Intent(Footer.this.getActivity(), MainActivity.class);
                startActivity(intent);
            });
        });

        return view;
    }
}
