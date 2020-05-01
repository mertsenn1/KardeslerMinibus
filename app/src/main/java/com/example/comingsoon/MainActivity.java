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
    }

    /**
     * This method links "Passenger Entry" button to Map Activity
     */
    private void configurePassengerButton() {
        Button buttonPassenger = (Button) findViewById( R.id.passengerLoginButton);
        buttonPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( MainActivity.this, MapActivity.class) );
            }
        });
    }
}
