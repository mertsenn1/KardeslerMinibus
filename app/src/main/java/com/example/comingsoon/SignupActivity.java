package com.example.comingsoon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignupActivity extends AppCompatActivity {

    Button signinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signinButton = findViewById(R.id.signinButton2);

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( SignupActivity.this, SigninActivity.class);
                startActivity( intent);
            }
        });
    }
}
