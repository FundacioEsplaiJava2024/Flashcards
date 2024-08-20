package com.esplai.flashcards;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esplai.flashcards.model.User;
import com.esplai.flashcards.network.ApiCliente;
import com.esplai.flashcards.network.ApiService;
import com.esplai.flashcards.service.cardlogic.CardAdapter;
import com.esplai.flashcards.service.cardlogic.CardModel;
import com.esplai.flashcards.service.login.RegisterActivity;
import com.esplai.flashcards.ui.Footer;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.esplai.flashcards.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CardStackLayoutManager manager;
    private CardAdapter adapter;
    private List<CardModel> cardList; // Lista de cartas
    private ActivityMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addFooter(savedInstanceState);

        // Inicializar la lista de cartas y el adaptador
        cardList = new ArrayList<>();
        adapter = new CardAdapter(cardList);

        CardStackView cardStackView = findViewById(R.id.card_stack_view);
        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d-"+ direction.name() + " ratio-"+ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG,"onCardSwiped: p-"+manager.getTopPosition()+" d-"+direction);
            }

            @Override
            public void onCardRewound() {
                Log.d(TAG, "onCardRewound "+manager.getTopPosition());
            }

            @Override
            public void onCardCanceled() {
                Log.d(TAG, "onCardCanceled "+manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {
                Log.d(TAG, "onCardAppeared "+position);
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                Log.d(TAG, "onCardDisappeared "+position);
            }
        });

        loadCardSettings(cardStackView);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    public void loadCardSettings(CardStackView cardStackView) {
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(10);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.FREEDOM);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());

        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());

        // Cargar las cartas
        loadCards();
    }

    private void loadCards() {
        ApiService apiService = ApiCliente.getClient().create(ApiService.class);

        // Obtener el token de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token != null) {
            Call<List<CardModel>> call = apiService.getRandomCards("Bearer " + token);

            call.enqueue(new Callback<List<CardModel>>() {
                @Override
                public void onResponse(Call<List<CardModel>> call, Response<List<CardModel>> response) {
                    if (response.isSuccessful()) {
                        Log.d("MainActivity", "Response JSON: " + new Gson().toJson(response.body()));
                        List<CardModel> cards = response.body();
                        if (cards != null && !cards.isEmpty()) {
                            cardList.clear();
                            cardList.addAll(cards);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity.this, "No se encontraron cartas", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Error al recuperar las cartas", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<CardModel>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Error al contactar con el servidor", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Log.d("MyApp", "No se encontró ningún token");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Acción a realizar cuando se presiona el botón de búsqueda
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Acción a realizar cuando el texto de la búsqueda cambia
                return false;
            }
        });

        return true;
    }

    private void addFooter(Bundle savedInstance) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.footer, new Footer(), "FOOTER")
                .disallowAddToBackStack()
                .commit();
    }
}
