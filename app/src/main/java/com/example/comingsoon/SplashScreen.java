package com.example.comingsoon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    // Properties

    // Methods
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_splash_screen);

        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                startActivity( new Intent( SplashScreen.this, MainActivity.class));
                finish();
            }
        }, 3000);
    }
}
