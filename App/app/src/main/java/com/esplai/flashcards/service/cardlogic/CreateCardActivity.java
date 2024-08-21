package com.esplai.flashcards.service.cardlogic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.esplai.flashcards.R;
import com.esplai.flashcards.model.Card;
import com.esplai.flashcards.network.ApiCliente;
import com.esplai.flashcards.network.ApiService;
import com.esplai.flashcards.service.collection.AddCollectionActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCardActivity extends AppCompatActivity {

    private EditText etFront, etBackside, etCollectionId;
    private Button btCreateCard , btCreateColecction;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_crate);

        // Inicializar los elementos de la UI
        etFront = findViewById(R.id.etFront);
        etBackside = findViewById(R.id.etBackside);
        etCollectionId = findViewById(R.id.etCollectionId);
        btCreateCard = findViewById(R.id.btCreateCard);
        btCreateColecction = findViewById(R.id.btCreateColecction);

        // Inicializar ApiService
        apiService = ApiCliente.getClient().create(ApiService.class);

        btCreateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCard();
            }
        });
        // Añadir OnClickListener para el botón btCreateColecction
        btCreateColecction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateCardActivity.this, AddCollectionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createCard() {
        String front = etFront.getText().toString().trim();
        String backside = etBackside.getText().toString().trim();
        String collectionIdStr = etCollectionId.getText().toString().trim();


        if (front.isEmpty() || backside.isEmpty() || collectionIdStr.isEmpty()) {
            Toast.makeText(this, "Por favor, rellene todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        int collectionId;
        try {
            collectionId = Integer.parseInt(collectionIdStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El ID de colección debe ser un número.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el token de acceso
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("AccessToken", "");

        if (accessToken.isEmpty()) {
            Toast.makeText(this, "Error: No se encontró ningún token de acceso. Por favor, inicie sesión de nuevo.", Toast.LENGTH_SHORT).show();
            return;
        }

        Card cardRequest = new Card(front, backside, collectionId);

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
}
