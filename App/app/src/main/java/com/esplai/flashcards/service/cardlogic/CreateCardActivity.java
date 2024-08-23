package com.esplai.flashcards.service.cardlogic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.esplai.flashcards.R;
import com.esplai.flashcards.model.Card;
import com.esplai.flashcards.network.ApiCliente;
import com.esplai.flashcards.network.ApiService;
import com.esplai.flashcards.service.collection.AddCollectionActivity;
import com.esplai.flashcards.service.entities.Collection;
import com.esplai.flashcards.ui.Footer;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCardActivity extends AppCompatActivity {

    private EditText etFront;
    private EditText etBackside;
    private Spinner etCollectionId;
    private EditText etHashtag;
    private Button btCreateCard, btCreateColecction;
    private ApiService apiService;
    private List<Collection> collectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_crate);
        addFooter(savedInstanceState);

        // Inicializar los elementos de la UI
        etFront = findViewById(R.id.etFront);
        etBackside = findViewById(R.id.etBackside);
        etCollectionId = findViewById(R.id.etCollectionId);
        etHashtag = findViewById(R.id.etHashtag);
        btCreateCard = findViewById(R.id.btCreateCard);
        btCreateColecction = findViewById(R.id.btCreateColecction);

        apiService = ApiCliente.getClient().create(ApiService.class);

        loadCollections();

        btCreateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCard();
            }
        });

        btCreateColecction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateCardActivity.this, AddCollectionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadCollections() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("AccessToken", "");

        if (accessToken.isEmpty()) {
            Toast.makeText(this, "Error: No se encontró ningún token de acceso. Por favor, inicie sesión de nuevo.", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<List<Collection>> call = apiService.getCollectionsFromUser("Bearer " + accessToken);
        call.enqueue(new Callback<List<Collection>>() {
            @Override
            public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    collectionList = response.body();
                    ArrayAdapter<Collection> adapter = new ArrayAdapter<>(CreateCardActivity.this,
                            android.R.layout.simple_spinner_item, collectionList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    etCollectionId.setAdapter(adapter);
                } else {
                    Toast.makeText(CreateCardActivity.this, "No se pudieron cargar las colecciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Collection>> call, Throwable t) {
                Log.e("CreateCardActivity", "Error: " + t.getMessage(), t);
                Toast.makeText(CreateCardActivity.this, "Error al cargar las colecciones", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void createCard() {
        String front = etFront.getText().toString().trim();
        String backside = etBackside.getText().toString().trim();
        String hashtags = etHashtag.getText().toString().trim();
        Collection selectedCollection = (Collection) etCollectionId.getSelectedItem();

        // Verificar valores antes de la solicitud
        Log.d("CreateCardActivity", "Hashtags capturado: " + hashtags);

        if (front.isEmpty() || backside.isEmpty() || selectedCollection == null || hashtags.isEmpty()) {
            Toast.makeText(this, "Por favor, rellene todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        int collectionId = selectedCollection.getId();

        // Obtener el token de acceso
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("token", null);

        if (accessToken.isEmpty()) {
            Toast.makeText(this, "Error: No se encontró ningún token de acceso. Por favor, inicie sesión de nuevo.", Toast.LENGTH_SHORT).show();
            return;
        }

        Card cardRequest = new Card(front, backside, collectionId, Collections.singletonList(hashtags));

        // Hacer la solicitud de creación de la carta
        Call<Void> call = apiService.createCard("Bearer " + accessToken, cardRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateCardActivity.this, "Tarjeta creada con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateCardActivity.this, "No se pudo crear la tarjeta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CreateCardActivity", "Error: " + t.getMessage(), t);
                Toast.makeText(CreateCardActivity.this, "Se ha producido un error. Por favor, inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
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
