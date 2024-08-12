package com.esplai.flashcards.service.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.esplai.flashcards.model.User;
import com.esplai.flashcards.network.ApiService;
import com.esplai.flashcards.network.ApiCliente;
import com.esplai.flashcards.MainActivity;
import com.esplai.flashcards.R;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    AppCompatButton btCreate, btLogin;
    EditText etUser, etMail, etContra, etRPcontra;
    TextView tvError, tvForgotPwd;
    View.OnClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_sreen);
        instantiateElements();
        prepareListener();
        setElementsToListener();
    }

    private void instantiateElements() {
        btLogin = findViewById(R.id.btLogin);
        btCreate = findViewById(R.id.btCreate);
        etUser = findViewById(R.id.etUser);
        etMail = findViewById(R.id.etMail);
        etContra = findViewById(R.id.etContra);
        etRPcontra = findViewById(R.id.etRPcontra);
        tvError = findViewById(R.id.tvError);
        tvForgotPwd = findViewById(R.id.tvForgotPwd);
        tvError.setVisibility(View.INVISIBLE);
    }

    private void setElementsToListener() {
        btLogin.setOnClickListener(listener);
        tvForgotPwd.setOnClickListener(listener);
        btCreate.setOnClickListener(listener);
    }

    public void prepareListener() {
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == btCreate.getId()) {
                    String username = etUser.getText().toString();
                    String password = etContra.getText().toString();
                    String password1 = etRPcontra.getText().toString();
                    String email = etMail.getText().toString();
                    // Verifica si algún campo está vacío
                    if (username.equals("") || email.equals("") || password.equals("") || password1.equals("")) {
                        etUser.setText("");
                        etRPcontra.setText("");
                        etContra.setText("");
                        etMail.setText("");
                        tvError.setText("Rellena todos los campos");
                        tvError.setVisibility(View.VISIBLE);
                    }
                    // Verificación de si es un correo válido
                    else if (!isValidEmail(email)) {
                        etMail.setText("");
                        tvError.setText("Correo no válido");
                        tvError.setVisibility(View.VISIBLE);
                    }
                    // Verificación de la longitud de la contraseña
                    else if (password.length() < 8 || password1.length() < 8) {
                        etContra.setText("");
                        etRPcontra.setText("");
                        tvError.setText("Minimo 8 caracteres");
                        tvError.setVisibility(View.VISIBLE);
                    }
                    // Verificación de si las contraseñas coinciden
                    else if (!password.equals(password1)) {
                        etContra.setText("");
                        etRPcontra.setText("");
                        tvError.setText("Las contraseñas no son identicas");
                        tvError.setVisibility(View.VISIBLE);
                    } else {
                        // Llamar al método para registrar el usuario
                        registerUser(username, password, email);
                    }
                } else if (v.getId() == tvForgotPwd.getId()) {
                    // Acciones para cuando se presiona "Olvidé mi contraseña"
                } else if (v.getId() == btLogin.getId()) {
                    openLoginScreen();
                }
            }
        };
    }

    // Verificación del formato de correo mediante un pattern
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"; // Este código es para verificar el mail, que tenga el formato correcto
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void openLoginScreen() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void registerUser(String username, String password, String email) {
        ApiService apiService = ApiCliente.getClient().create(ApiService.class);
        User newUser = new User(username, password, email);
        Call<User> call = apiService.createUser(newUser);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show();
                    openLoginScreen();
                } else {
                    tvError.setText("Error al crear usuario");
                    tvError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                tvError.setText("Fallo en la solicitud: " + t.getMessage());
                tvError.setVisibility(View.VISIBLE);
                Log.e("RegisterActivity", "Error: " + t.getMessage());
            }
        });
    }
}
