package com.example.comingsoon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PassengerProfileActivity extends AppCompatActivity {

    Button backButton;
    TextView changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_profile);

        configureBackButton();
        configureChangePasswordButton();
    }


    private void configureBackButton() {
        backButton = (Button) findViewById( R.id.back_button3);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( PassengerProfileActivity.this, SettingsActivity.class) );
            }
        });
    }

    private void configureChangePasswordButton() {
        changePassword = (TextView) findViewById( R.id.changePasswordButton);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( PassengerProfileActivity.this, ChangePasswordActivity.class) );
            }
        });
    }
}
