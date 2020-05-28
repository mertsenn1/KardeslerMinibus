package com.example.comingsoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * This is activity class that show settings page
 */
public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";

    private LinearLayout privacySettings;
    private BottomNavigationView bottomNav;
    private LinearLayout notificationLayout;
    private LinearLayout locationSettings;
    private LinearLayout helpSettings;
    private SharedPreferences sharedPreferences;

    // Methods
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_settings);

        bottomNav = findViewById( R.id.bottom_navigation);
        bottomNav.setSelectedItemId( R.id.settings);


        /**
         * Bottom navigation control
         */
        bottomNav.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( @NonNull MenuItem item) {
                switch ( item.getItemId() ) {
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
        configureNotificationSettingsButton();
    }

    /**
     * This method links "privacy settings" layout to Privacy Settings Activity
     */
    private void configurePrivacySettingsButton() {
        notificationLayout = findViewById( R.id.notification_layout);
        notificationLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                startActivity( new Intent( SettingsActivity.this, NotificationSettingsActivity.class));
            }
        });
    }

    /**
     * This method links "notification settings" layout to Notification Settings Activity
     */
    private void configureNotificationSettingsButton() {
        privacySettings = findViewById( R.id.privacy_settings);
        privacySettings.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                startActivity( new Intent( SettingsActivity.this, PrivacySettingsActivity.class));
            }
        });
    }

    /**
     * This method links "location settings" layout to Location Settings Activity
     */
    private void configureLocationSettingsButton() {
        locationSettings = findViewById( R.id.location_settings);
        locationSettings.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                startActivity( new Intent( SettingsActivity.this, RouteOptionsActivity.class));
            }
        });
    }

    /**
     * This method links "help & feedback" layout to Help Settings Activity
     */
    private void configureHelpButton() {
        helpSettings = findViewById( R.id.help_settings);
        helpSettings.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                startActivity( new Intent( SettingsActivity.this, HelpSettingsActivity.class));
            }
        });
    }
}
