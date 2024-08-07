package com.esplai.flashcards.service.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.esplai.flashcards.service.login.LoginActivity;
import com.esplai.flashcards.MainActivity;
import com.esplai.flashcards.R;

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

    private void prepareListener() {
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == btCreate.getId()) {
                    String username = etUser.getText().toString();
                    String password = etContra.getText().toString();
                    String password1 = etRPcontra.getText().toString();
                    String mail = etMail.getText().toString();
                    //login
                    if (username.equals("") || mail.equals("") || password.equals("") || password1.equals("")) {
                        etUser.setText("");
                        etRPcontra.setText("");
                        etContra.setText("");
                        //se tiene que hacer una verificacion de si es un mail
                        etMail.setText("");
                        tvError.setText("Rellena todos los campos");
                        tvError.setVisibility(View.VISIBLE);
                    }
                    //verificacio de contra
                    else if (! password.equals(password1)){
                        etContra.setText("");
                        etRPcontra.setText("");
                        tvError.setText(("contra no identico"));
                        tvError.setVisibility(View.VISIBLE);
                    }else { //se llama al método login, enviando username, password y el contexto de la activity
                        openLoginScreen();
                    }

                } else if (v.getId() == tvForgotPwd.getId()) {

                } else if (v.getId() == btLogin.getId()) {
                    openLoginScreen();
                }
            }
        };
    }


    private void openLoginScreen() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
