package com.example.comingsoon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SigninActivity extends AppCompatActivity {
    Button signupButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( SigninActivity.this, SignupActivity.class);
                startActivity( intent);
            }
        });



        };
    }

