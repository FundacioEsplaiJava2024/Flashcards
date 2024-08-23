package com.esplai.flashcards.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esplai.flashcards.R;
import com.esplai.flashcards.network.ApiCliente;
import com.esplai.flashcards.network.ApiService;
import com.esplai.flashcards.service.cardlogic.CardAdapter;
import com.esplai.flashcards.service.cardlogic.CardModel;
import com.esplai.flashcards.service.entities.Collection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionOthersDetailsActivity extends AppCompatActivity {
    private TextView collectionTitle;
    private TextView collectionDescription;
    private RecyclerView cardsRecyclerView;
    private CardAdapter cardAdapter;
    private List<CardModel> cardList = new ArrayList<>();
    private int collectionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomcollection_detail);
        addFooter(savedInstanceState);

        collectionTitle = findViewById(R.id.tvCollectionTitle);
        collectionDescription = findViewById(R.id.tvCollectionDescription);
        cardsRecyclerView = findViewById(R.id.rvCardList);

        cardAdapter = new CardAdapter(cardList);
        cardsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardsRecyclerView.setAdapter(cardAdapter);

        collectionId = getIntent().getIntExtra("collectionId",-1);
        String title = getIntent().getStringExtra("collectionTitle");
        String description = getIntent().getStringExtra("collectionDescription");


        if (collectionId != -1) {
            collectionTitle.setText(title);
            collectionDescription.setText(description);
            getCardsFromCollection(collectionId);
        } else {
            Toast.makeText(this, "ID de la colección no válida", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void getCardsFromCollection(int collectionId) {
        ApiService apiService = ApiCliente.getClient().create(ApiService.class);

        //Se recupera el token del usuario
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token != null) {
            Call<List<CardModel>> call = apiService.getCollectionDetails("Bearer " + token, collectionId);

            call.enqueue(new Callback<List<CardModel>>() {
                @Override
                public void onResponse(Call<List<CardModel>> call, Response<List<CardModel>> response) {
                    if (response.isSuccessful()) {
                        List<CardModel> cardsResponse = response.body();
                        if (cardsResponse != null && !cardsResponse.isEmpty()) {
                            cardList.clear();
                            cardList.addAll(cardsResponse);
                            cardAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(CollectionOthersDetailsActivity.this, "No se encontraron cartas en esta colección", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            Log.e("CardsError", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(CollectionOthersDetailsActivity.this, "Error al recuperar las cartas", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<CardModel>> call, Throwable t) {
                    Toast.makeText(CollectionOthersDetailsActivity.this, "Error al contactar con el servidor", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No se encontró el token", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveCollection(int collectionId) {
        ApiService apiService = ApiCliente.getClient().create(ApiService.class);

        //Se recupera el token del usuario
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token != null) {
            Call<Collection> call = apiService.saveCollection("Bearer " + token, collectionId);

            call.enqueue(new Callback<Collection>() {
                @Override
                public void onResponse(Call<Collection> call, Response<Collection> response) {
                    if (response.isSuccessful()) {
                        Collection collectionResponse = response.body();
                        if (collectionResponse != null ) {
                            Toast.makeText(CollectionOthersDetailsActivity.this, "Colección guardada", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CollectionOthersDetailsActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            Log.e("CardsError", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(CollectionOthersDetailsActivity.this, "La colección ya estaba guardada", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Collection> call, Throwable t) {
                    Toast.makeText(CollectionOthersDetailsActivity.this, "Error al contactar con el servidor", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No se encontró el token", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.button_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_button) {
            saveCollection(collectionId);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addFooter(Bundle savedInstance) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.footer, new Footer(), "FOOTER")
                .disallowAddToBackStack()
                .commit();
    }
}
