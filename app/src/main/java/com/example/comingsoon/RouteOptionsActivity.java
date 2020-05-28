package com.example.comingsoon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * This is simple activity class that adjust route options
 */
public class RouteOptionsActivity extends AppCompatActivity {

    // Properties
    private Button backButton;

    // Methods
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_route_options);

        configureBackButton();
    }

    /**
     * This method links back button to Settings Activity
     */
    private void configureBackButton() {
        backButton = findViewById( R.id.location_back_button);
        backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                startActivity( new Intent( RouteOptionsActivity.this, SettingsActivity.class));
            }
        });
    }
}
