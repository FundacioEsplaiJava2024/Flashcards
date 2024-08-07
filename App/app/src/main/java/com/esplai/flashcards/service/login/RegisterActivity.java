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

import java.util.regex.Pattern;
import java.util.regex.Matcher;


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
                    //register
                    if (username.equals("") || mail.equals("") || password.equals("") || password1.equals("")) {
                        etUser.setText("");
                        etRPcontra.setText("");
                        etContra.setText("");
                        etMail.setText("");
                        tvError.setText("Rellena todos los campos");
                        tvError.setVisibility(View.VISIBLE);
                    }
                    //verifcacion de si es un mail
                    else if (!isValidEmail(mail)) {
                        etMail.setText("");
                        tvError.setText("Correo no válido");
                        tvError.setVisibility(View.VISIBLE);
                    }
                    //verificacion de la lonitut de la contraseña
                    else if (password.length() < 8 || password1.length() < 8) {
                        etContra.setText("");
                        etRPcontra.setText("");
                        tvError.setText("Minimo 8 caracteres");
                        tvError.setVisibility(View.VISIBLE);
                    }

                    //verificacio de contra
                    else if (! password.equals(password1)){
                        etContra.setText("");
                        etRPcontra.setText("");
                        tvError.setText(("Las contraseñas no son identicas"));
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

    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }



    private void openLoginScreen() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
