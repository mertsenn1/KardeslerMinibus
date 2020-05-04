package com.example.comingsoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        notificationLayout = (LinearLayout) findViewById(R.id.notification_layout);

        /**
         * Bottom navigation control
         */
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
        configureHelpButton();
    }

    /**
     * This method links "privacy settings" layout to Privacy Settings Activity
     */
    private void configurePrivacySettingsButton() {
        privacySettings = (LinearLayout) findViewById( R.id.privacy_settings);
        privacySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( SettingsActivity.this, PrivacySettingsActivity.class) );
            }
        });
    }

    /**
     * This method links "location settings" layout to Location Settings Activity
     */
    private void configureLocationSettingsButton() {
        locationSettings = (LinearLayout) findViewById( R.id.location_settings);
        locationSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( SettingsActivity.this, LocationSettingsActivity.class) );
            }
        });
    }

    /**
     * This method links "help & feedback" layout to Help Settings Activity
     */
    private void configureHelpButton() {
        helpSettings = (LinearLayout) findViewById( R.id.help_settings);
        helpSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( SettingsActivity.this, HelpSettingsActivity.class) );
            }
        });
    }

}
