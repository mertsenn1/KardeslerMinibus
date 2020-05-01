package com.example.comingsoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchTab extends AppCompatActivity {

        BottomNavigationView bottomNav;
        Button route1;
        //BottomSheetDialog mapSearchDialog;
        //Button newButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tab);

        bottomNav = findViewById( R.id.bottom_navigation);
        bottomNav.setSelectedItemId( R.id.search);
        route1 = findViewById(R.id.route_1);

        /**
         * Bottom navigation control code
         */
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch ( item.getItemId()) {
                    case R.id.search:
                        return true;
                    case R.id.map:
                        startActivity( new Intent( getApplicationContext(), MapActivity.class));
                        overridePendingTransition( 0, 0);
                        return true;
                    case R.id.settings:
                        startActivity( new Intent( getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition( 0, 0);
                        return true;
                }
                return false;
            }
        });

    }
}
