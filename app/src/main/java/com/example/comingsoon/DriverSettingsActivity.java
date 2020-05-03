package com.example.comingsoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DriverSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_settings);

        BottomNavigationView bottomNav = findViewById( R.id.driver_bottom_navigation);
        bottomNav.setSelectedItemId( R.id.driver_settings);


        /**
         * Bottom navigation control code
         */
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch ( item.getItemId()) {
                    case R.id.journey:
                        startActivity( new Intent( getApplicationContext(), JourneyInfoActivity.class));
                        overridePendingTransition( 0, 0);
                        return true;
                    case R.id.map_driver:
                        startActivity( new Intent( getApplicationContext(), DriverMapActivity.class));
                        overridePendingTransition( 0, 0);
                        return true;
                    case R.id.driver_settings:
                        return true;
                }
                return false;
            }
        });

        configureProfileButton();
    }

    /**
     * This method links "profile" layout to Driver Profile Activity
     */
    private void configureProfileButton() {
        LinearLayout profileSettings = (LinearLayout) findViewById( R.id.profile_settings);
        profileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( DriverSettingsActivity.this, DriverProfileActivity.class) );
            }
        });
    }
}
