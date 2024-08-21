package com.esplai.flashcards.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionDetailActivity extends AppCompatActivity {

    private TextView collectionTitle;
    private TextView collectionDescription;
    private RecyclerView cardsRecyclerView;
    private CardAdapter cardAdapter;
    private List<CardModel> cardList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_detail);
        addFooter(savedInstanceState);

        collectionTitle = findViewById(R.id.tvCollectionTitle);
        collectionDescription = findViewById(R.id.tvCollectionDescription);
        cardsRecyclerView = findViewById(R.id.rvCardList);

        cardAdapter = new CardAdapter(cardList);
        cardsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardsRecyclerView.setAdapter(cardAdapter);

        int collectionId = getIntent().getIntExtra("collectionId",-1);
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
                        Log.d("API Response", "Cards size: " + (cardsResponse != null ? cardsResponse.size() : 0));


                        if (cardsResponse != null && !cardsResponse.isEmpty()) {
                            cardList.clear();
                            cardList.addAll(cardsResponse);
                            cardAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(CollectionDetailActivity.this, "No se encontraron cartas en esta colección", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            Log.e("CardsError", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(CollectionDetailActivity.this, "Error al recuperar las cartas", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<CardModel>> call, Throwable t) {
                    Toast.makeText(CollectionDetailActivity.this, "Error al contactar con el servidor", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No se encontró el token", Toast.LENGTH_SHORT).show();
        }
    }

    private void addFooter(Bundle savedInstance) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.footer, new Footer(), "FOOTER")
                .disallowAddToBackStack()
                .commit();
    }
}
