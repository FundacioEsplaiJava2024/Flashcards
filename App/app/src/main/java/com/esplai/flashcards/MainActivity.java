package com.esplai.flashcards;

import android.content.Context;
import android.content.Intent;
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
import com.esplai.flashcards.ui.CollectionDetailActivity;
import com.esplai.flashcards.ui.Footer;
import com.esplai.flashcards.ui.RandomCollectionsActivity;
import com.esplai.flashcards.ui.SavedCollectionsActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
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
    private List<CardModel> cardList;
    private ActivityMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private boolean isLoadingMoreCards = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addFooter(savedInstanceState);

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

                //Si llegamos al final de la lista, se cargan más cartas
                if (manager.getTopPosition() == adapter.getItemCount() && !isLoadingMoreCards) {
                    loadCards();
                }
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
                R.id.nav_collections) //Añadir aquí las siguientes opciones del menú
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_collections) {
                    Intent intent = new Intent(MainActivity.this, RandomCollectionsActivity.class);
                    startActivity(intent);
                }
                if (id == R.id.nav_savedCollections) {
                    Intent intent = new Intent(MainActivity.this, SavedCollectionsActivity.class);
                    startActivity(intent);
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

        });
    }

    //Método que configura el swipe de las cartas. Para más info, mirar el repo de yuyakaido cardstackview
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

        loadCards();
    }

    private void loadCards() {
        if (isLoadingMoreCards) return; //Evita llamadas múltiples
        isLoadingMoreCards = true;

        ApiService apiService = ApiCliente.getClient().create(ApiService.class);

        //Se recupera el token del user
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token != null) {
            Call<List<CardModel>> call = apiService.getRandomCards("Bearer " + token);

            call.enqueue(new Callback<List<CardModel>>() {
                @Override
                public void onResponse(Call<List<CardModel>> call, Response<List<CardModel>> response) {
                    isLoadingMoreCards = false; // Restablecer el estado de carga

                    if (response.isSuccessful()) {
                        List<CardModel> cards = response.body();
                        if (cards != null && !cards.isEmpty()) {
                            cardList.addAll(cards); // Agregar más cartas a la lista
                            adapter.notifyDataSetChanged(); // Notificar al adaptador
                        } else {
                            Toast.makeText(MainActivity.this, "No se encontraron más cartas", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            Log.e("MainActivity", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MainActivity.this, "Error al recuperar las cartas", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<CardModel>> call, Throwable t) {
                    isLoadingMoreCards = false;
                    Log.e("MainActivity", "Error: " + t.getMessage());
                    Toast.makeText(MainActivity.this, "Error al contactar con el servidor", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            isLoadingMoreCards = false;
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
                // Acción a realizar cuando se presiona el botón de búsqueda
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Acción a realizar cuando el texto de la búsqueda cambia
                return false;
            }
        });
        MenuItem menuItem = menu.findItem(R.id.action_open_drawer);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DrawerLayout drawerLayout = binding.drawerLayout;
                drawerLayout.openDrawer(GravityCompat.START); // Abre el Navigation Drawer
                return true;
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

