package com.example.comingsoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";

    private LinearLayout privacySettings;
    private BottomNavigationView bottomNav;
    private LinearLayout notificationLayout;
    private LinearLayout locationSettings;
    private Button profileButton;
    private LinearLayout helpSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bottomNav = findViewById( R.id.bottom_navigation);
        bottomNav.setSelectedItemId( R.id.settings);
        notificationLayout = (LinearLayout) findViewById(R.id.notificationLayout);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch ( item.getItemId()) {
                    case R.id.search:
                        startActivity( new Intent( getApplicationContext(), SearchTab.class));
                        overridePendingTransition( 0, 0);
                        return true;
                    case R.id.map:
                        startActivity( new Intent( getApplicationContext(), MapActivity.class));
                        overridePendingTransition( 0, 0);
                        return true;
                    case R.id.settings:
                        return true;
                }
                return false;
            }
        });

        configurePrivacySettingsButton();
        configureLocationSettingsButton();
        configureProfileButton();
        configureHelpButton();
    }

    private void configurePrivacySettingsButton() {
        privacySettings = (LinearLayout) findViewById( R.id.privacySettings);
        privacySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( SettingsActivity.this, PrivacySettingsActivity.class) );
            }
        });
    }

    private void configureLocationSettingsButton() {
        locationSettings = (LinearLayout) findViewById( R.id.locationSettings);
        locationSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( SettingsActivity.this, LocationSettingsActivity.class) );
            }
        });
    }

    private void configureProfileButton() {
        profileButton = (Button) findViewById( R.id.profile_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( SettingsActivity.this, PassengerProfileActivity.class) );
            }
        });
    }

    private void configureHelpButton() {
        helpSettings = (LinearLayout) findViewById( R.id.helpSettings);
        helpSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( SettingsActivity.this, HelpSettingsActivity.class) );
            }
        });
    }

}
