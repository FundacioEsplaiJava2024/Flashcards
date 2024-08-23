package com.esplai.flashcards.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.esplai.flashcards.MainActivity;
import com.esplai.flashcards.R;
import com.esplai.flashcards.service.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000; // Duración de la Splash Screen en milisegundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.ivLogo);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1500);
        fadeIn.setFillAfter(true);

        logo.startAnimation(fadeIn);

        //Pasar a la siguiente actividad después de que se complete la Splash Screen
        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);
            if (token!=null){
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}
