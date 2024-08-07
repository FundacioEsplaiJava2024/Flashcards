package com.esplai.flashcards.service.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.esplai.flashcards.MainActivity;
import com.esplai.flashcards.R;

import java.sql.SQLOutput;


public class LoginActivity extends AppCompatActivity {
    AppCompatButton btLogin;
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
    }

    //Se instancia un nuevo onclicklistener al listener con los elementos seteados
    private void prepareListener() {
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == btLogin.getId()){
                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();

                    if (false && (username.equals("") || password.equals(""))) {
                        etUsername.setText("");
                        etPassword.setText("");
                        tvError.setText("Rellena todos los campos");
                        tvError.setVisibility(View.VISIBLE);
                    } else { //se llama al método login, enviando username, password y el contexto de la activity
                        openMainMenuScreen();
                    }
                }//Al pulsar en la tvForgotPwd, se cambia de pantalla a la forgotPasswordActivity
                else if(v.getId()==tvForgotPwd.getId()){
                    //Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class); añadido por si se implementa más adelante
                    //startActivity(intent);
                }
            }
        };
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