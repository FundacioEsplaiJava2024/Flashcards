package com.esplai.flashcards.service.collection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.esplai.flashcards.R;
import com.esplai.flashcards.model.Collection;
import com.esplai.flashcards.network.ApiCliente;
import com.esplai.flashcards.network.ApiService;
import com.esplai.flashcards.service.login.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCollectionActivity extends AppCompatActivity {

    private EditText etTitle;
    private CheckBox checkboxPublic;
    private Button btCreateCollection;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_layout);

        // Inicializar los elementos de la UI
        etTitle = findViewById(R.id.etTitle);
        checkboxPublic = findViewById(R.id.checkboxPublic);
        btCreateCollection = findViewById(R.id.btCreateCollection);

        // Inicializar ApiService
        apiService = ApiCliente.getClient().create(ApiService.class);

        // Configurar el listener para el botón
        btCreateCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCollection();
            }
        });

        // Verificar si el token está presente
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String Token = sharedPreferences.getString("AccessToken", ""); // Debe ser "AccessToken"

        if (Token == null || Token.isEmpty()) {
            // Redirige al Login si no hay token
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }
    }


    private void createCollection() {
        String title = etTitle.getText().toString().trim();
        boolean isPublic = checkboxPublic.isChecked();

        if (title.isEmpty()) {
            Toast.makeText(this, "Por favor, introduce un título para la colección.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Recuperar el token de acceso almacenado
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("AccessToken", "");

        if (accessToken.isEmpty()) {
            Toast.makeText(this, "Error: No se encontró el token de acceso. Por favor, inicia sesión de nuevo.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crea el objeto de solicitud
        Collection collectionRequest = new Collection(title, isPublic);

        // Realiza la llamada a la API
        Call<Void> call = apiService.createCollection("Bearer " + accessToken, collectionRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddCollectionActivity.this, "Colección creada con éxito.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddCollectionActivity.this, "Error al crear la colección.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("AddCollectionActivity", "Error en la solicitud", t);
                Toast.makeText(AddCollectionActivity.this, "Ocurrió un error al comunicarse con la API.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
