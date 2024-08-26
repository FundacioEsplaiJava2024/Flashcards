package com.esplai.flashcards.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.esplai.flashcards.MainActivity;
import com.esplai.flashcards.R;
import com.esplai.flashcards.service.cardlogic.CreateCardActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Footer extends Fragment {
    private BottomNavigationView bottomNavView;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.footer, container, false);

        bottomNavView = view.findViewById(R.id.bottomNavView);
        fab = view.findViewById(R.id.fabF);

        // Configura el listener para el BottomNavigationView
        bottomNavView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_collections:
                        Log.d("Footer", "Collections button pressed");
                        startActivity(new Intent(getActivity(), CollectionsActivity.class));
                        return true;
                    case R.id.nav_add_card:
                        Log.d("Footer", "Add Card button pressed");
                        startActivity(new Intent(getActivity(), AddActivity.class));
                        return true;
                    default:
                        return false;
                }
            }
        });

        fab.setOnClickListener(v -> {
            Log.d("Footer", "FAB pressed");
            if(getActivity() instanceof MainActivity)
                return;
            startActivity(new Intent(getActivity(), MainActivity.class));
        });

        return view;
    }
}
