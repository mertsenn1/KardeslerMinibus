package com.example.comingsoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class JourneyInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_info);
        BottomNavigationView bottomNav = findViewById( R.id.driver_bottom_navigation);
        bottomNav.setSelectedItemId( R.id.journey);

        /**
         * Bottom navigation control code
         */
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch ( item.getItemId()) {
                    case R.id.journey:
                        return true;
                    case R.id.map_driver:
                        startActivity( new Intent( getApplicationContext(), DriverMapActivity.class));
                        overridePendingTransition( 0, 0);
                        return true;
                    case R.id.driver_settings:
                        startActivity( new Intent( getApplicationContext(), DriverSettingsActivity.class));
                        overridePendingTransition( 0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}
