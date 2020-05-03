package com.example.comingsoon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configurePassengerButton();
        configureDriverButton();
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
    /**
     * This method links "Driver login" button to Sign In Activity
     */
    private void configureDriverButton(){
        Button buttonDriver = (Button) findViewById( R.id.driverLoginButton);
        buttonDriver.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick( View view){
                startActivity( new Intent( MainActivity.this, SignInActivity.class));
            }
        });
    }
}
