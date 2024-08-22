package com.esplai.flashcards.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.esplai.flashcards.R;
import com.esplai.flashcards.network.ApiCliente;
import com.esplai.flashcards.network.ApiService;
import com.esplai.flashcards.service.entities.Collection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RandomCollectionsActivity extends AppCompatActivity {
    private boolean isLoadingCollections = false;
    private GridLayout collectionsContainer;
    private List<Collection> collectionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_screen);
        addFooter(savedInstanceState);

        collectionsContainer = findViewById(R.id.collectionsContainer);

        getCollectionsFromServer();
    }

    //Método que realiza la petición al servidor y actualiza la interfaz con las colecciones recibidas
    private void getCollectionsFromServer() {
        if (isLoadingCollections) return;
        isLoadingCollections = true;

        ApiService apiService = ApiCliente.getClient().create(ApiService.class);

        //Recupera el token del usuario
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token != null) {
            Call<List<Collection>> call = apiService.getCollectionsFromUser("Bearer " + token);

            call.enqueue(new Callback<List<Collection>>() {
                @Override
                public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response) {
                    isLoadingCollections = false;

                    if (response.isSuccessful()) {
                        List<Collection> collectionsResponse = response.body();
                        if (collectionsResponse != null && !collectionsResponse.isEmpty()) {
                            collectionList.addAll(collectionsResponse);
                            for (Collection collection : collectionList) {
                                addCollectionView(collection);
                            }
                        } else {
                            Toast.makeText(RandomCollectionsActivity.this, "No se encontraron colecciones, prueba a crear o a guardar unas", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            Log.e("Collections", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(RandomCollectionsActivity.this, "Error al recuperar las colecciones", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Collection>> call, Throwable t) {
                    isLoadingCollections = false;
                    Log.e("Collections", "Error: " + t.getMessage());
                    Toast.makeText(RandomCollectionsActivity.this, "Error al contactar con el servidor", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            isLoadingCollections = false;
            Log.d("MyApp", "No se encontró ningún token");
        }
    }

    //Agregar las vistas de las colecciones dinámicamente
    private void addCollectionView(Collection collection) {
        View collectionView = LayoutInflater.from(this).inflate(R.layout.collection_item, collectionsContainer, false);

        ImageView collectionIcon = collectionView.findViewById(R.id.collection_image);
        TextView collectionName = collectionView.findViewById(R.id.collection_title);

        collectionIcon.setImageResource(R.drawable.album_colored); // o una imagen dinámica si tienes una URL
        collectionName.setText(collection.getTitle());

        // Ajusta los parámetros de layout para que se distribuyan correctamente en el GridLayout
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(8, 8, 8, 8);
        collectionView.setLayoutParams(params);

        collectionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RandomCollectionsActivity.this, CollectionDetailActivity.class);
                intent.putExtra("collectionId", collection.getId());
                intent.putExtra("collectionTitle", collection.getTitle());
                intent.putExtra("collectionDescription", collection.getDescription());
                startActivity(intent);
            }
        });

        collectionsContainer.addView(collectionView);
    }

    private void addFooter(Bundle savedInstance) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.footer, new Footer(), "FOOTER")
                .disallowAddToBackStack()
                .commit();
    }
}

