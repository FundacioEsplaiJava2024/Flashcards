package com.esplai.flashcards.service.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.esplai.flashcards.MainActivity;
import com.esplai.flashcards.R;
import com.esplai.flashcards.model.AccesToken;
import com.esplai.flashcards.model.LoginUser;
import com.esplai.flashcards.network.ApiCliente;
import com.esplai.flashcards.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class LoginActivity extends AppCompatActivity {
    AppCompatButton btLogin;
    AppCompatButton btRegister;
    EditText etUsername, etPassword;
    TextView tvError, tvForgotPwd;
    View.OnClickListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareStatusBar();
        setContentView(R.layout.login_layout);
        instantiateElements();
        prepareListener();
        setElementsToListener();
    }

    //Se instancian los elementos de la pantalla
    private void instantiateElements() {
        btLogin = findViewById(R.id.btLogin);
        btRegister = findViewById((R.id.btRegister));
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvError = findViewById(R.id.tvError);
        tvForgotPwd = findViewById(R.id.tvForgotPwd);
        tvError.setVisibility(View.INVISIBLE);
    }

    //Se asigna el botón y la textview al OnClickListener
    private void setElementsToListener() {
        btLogin.setOnClickListener(listener);
        tvForgotPwd.setOnClickListener(listener);
        btRegister.setOnClickListener(listener);
    }

    //Se instancia un nuevo onclicklistener al listener con los elementos seteados
    private void prepareListener() {
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == btLogin.getId()){
                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();

                    if ((username.equals("") || password.equals(""))) {
                        etUsername.setText("");
                        etPassword.setText("");
                        tvError.setText("Rellena todos los campos");
                        tvError.setVisibility(View.VISIBLE);
                    } else { //se llama al método login, enviando username, password y el contexto de la activity
                        loginUser(username, password);
                    }

                }
                //Al pulsar en la tvForgotPwd, se cambia de pantalla a la forgotPasswordActivity
                else if (v.getId() == btRegister.getId()) {
                    openMainRegisterScreen();
                } else if (v.getId() == tvForgotPwd.getId()) {
                    // Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class); // Añadido por si se implementa más adelante
                    // startActivity(intent);
                }
            }
        };
    }

    private void loginUser(String username, String password) {
        ApiService apiService = ApiCliente.getClient().create(ApiService.class);
        LoginUser loginUser = new LoginUser(username, password);

        Call<AccesToken> call = apiService.loginUser(loginUser);
        call.enqueue(new Callback<AccesToken>() {
            @Override
            public void onResponse(Call<AccesToken> call, Response<AccesToken> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();

                    // Guardar el token en SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("AccessToken", token); // Asegúrate de que la clave sea "AccessToken"
                    editor.apply();

                    // Mostrar mensaje de éxito y abrir la pantalla principal
                    Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    openMainMenuScreen();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        tvError.setText("Error: " + errorBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                        tvError.setText("Error desconocido. Intenta nuevamente.");
                    }
                    tvError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AccesToken> call, Throwable t) {
                // Mostrar mensaje de error en caso de fallo de red o servidor
                tvError.setText("Error de red: " + t.getMessage());
                tvError.setVisibility(View.VISIBLE);
            }
        });
    }

    //metod para abrir el register
    public void openMainRegisterScreen(){
        Intent intent = new Intent(LoginActivity.this , RegisterActivity.class );
        startActivity(intent);
    }

    //Método que abre la ventana del menú principal
    public void openMainMenuScreen(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //region Barra superior de estado transparente
    private void prepareStatusBar() {
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}