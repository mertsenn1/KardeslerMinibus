package com.example.comingsoon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configurePassengerButton();
        configureDriverButton();
    }

    private void configurePassengerButton() {
        Button buttonPassenger = (Button) findViewById( R.id.passengerLoginButton);
        buttonPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( MainActivity.this, SignUpActivity.class) );
            }
        });
    }
    private void configureDriverButton() {
        Button buttonDriver = (Button) findViewById( R.id.driverLoginButton);
        buttonDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( MainActivity.this, SearchTab.class) );
            }
        });
    }
}
