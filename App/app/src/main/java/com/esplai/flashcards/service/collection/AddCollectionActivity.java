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
import com.esplai.flashcards.network.ApiCliente;
import com.esplai.flashcards.network.ApiService;
import com.esplai.flashcards.service.cardlogic.CreateCardActivity;
import com.esplai.flashcards.service.entities.Collection;
import com.esplai.flashcards.service.login.LoginActivity;
import com.esplai.flashcards.ui.Footer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCollectionActivity extends AppCompatActivity {

    private EditText etTitle , etdescription;
    private CheckBox checkboxPublic;
    private Button btCreateCollection;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_layout);
        addFooter(savedInstanceState);

        etTitle = findViewById(R.id.etTitle);
        etdescription = findViewById(R.id.etdescription);
        checkboxPublic = findViewById(R.id.checkboxPublic);
        btCreateCollection = findViewById(R.id.btCreateCollection);

        apiService = ApiCliente.getClient().create(ApiService.class);

        btCreateCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCollection();
            }
        });

    }

    private void createCollection() {
        String title = etTitle.getText().toString().trim();
        String description = etdescription.getText().toString().trim();
        boolean isPublic = checkboxPublic.isChecked();

        if (title.isEmpty()) {
            Toast.makeText(this, "Por favor, introduce un título para la colección.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Recuperar el token de acceso almacenado
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("token", null);

        //acceso al token
        if (accessToken.isEmpty()) {
            Toast.makeText(this, "Error: No se encontró ningún verificasion. Por favor, inicie sesión de nuevo.", Toast.LENGTH_SHORT).show();
            return;
        }
        Collection collectionRequest = new Collection(title,description, isPublic);

        // Realiza la llamada a la API
        Call<Void> call = apiService.createCollection("Bearer " + accessToken, collectionRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            if (response.isSuccessful()) {
                Toast.makeText(AddCollectionActivity.this, "Coleccion creada con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddCollectionActivity.this, "No se pudo crear la Collecion", Toast.LENGTH_SHORT).show();
            }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("AddCollectionActivity", "Error en la solicitud", t);
                Toast.makeText(AddCollectionActivity.this, "Ocurrió un error al comunicarse con la API.", Toast.LENGTH_SHORT).show();
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
