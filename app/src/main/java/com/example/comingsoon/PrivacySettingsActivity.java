package com.example.comingsoon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * This is simple activity class that adjust the privacy settings.
 */
public class PrivacySettingsActivity extends AppCompatActivity {

    // Properties
    private Button backButton;
    private SharedPreferences sharedPreferences;
    private Switch allowLocationSwitch;

    // Methods
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_privacy_settings);

        configureBackButton();
    }

    /**
     * This method links back button to Settings Activity
     */
    private void configureBackButton() {
        backButton = findViewById( R.id.privacy_back_button);
        backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                startActivity( new Intent( PrivacySettingsActivity.this, SettingsActivity.class));
            }
        });
    }

}
