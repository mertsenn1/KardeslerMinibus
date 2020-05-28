package com.example.comingsoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This is a simple activity class.
 * In this activity, driver can see his route and change it.
 * Changing process is done by using firebase.
 */
public class JourneyInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Properties
    private int route;

    // Methods
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_journey_info);

        BottomNavigationView bottomNav = findViewById( R.id.driver_bottom_navigation);
        bottomNav.setSelectedItemId( R.id.journey);

        /**
         * Bottom navigation control code
         */
        bottomNav.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( @NonNull MenuItem item) {
                switch ( item.getItemId() ) {
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

        configureChangeRouteButton();
        configureSpinner();
    }

    /**
     * This method assigns a listener for the changes on the Firebase Database.
     */
    public void configureSpinner() {
        final Spinner spinner = findViewById( R.id.current_route_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( this, R.array.route_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource( android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter( adapter);
        spinner.setOnItemSelectedListener( this);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child( "Drivers")
                .child( FirebaseAuth.getInstance().getCurrentUser().getUid()).child( "route");
        databaseReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot) {
                int route = Math.toIntExact( ( Long ) dataSnapshot.getValue());
                spinner.setSelection( route);
            }

            @Override
            public void onCancelled( @NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemSelected( AdapterView<?> parent, View view, int position, long id) {
         route = position;
    }

    @Override
    public void onNothingSelected( AdapterView<?> parent) {

    }

    /**
     * This method simply changes the route of the driver.
     * After update the route of the driver, it gives a message as toast.
     */
    public void configureChangeRouteButton() {
        Button changeRoute = findViewById( R.id.change_route);

        changeRoute.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                FirebaseDatabase.getInstance().getReference().child( "Drivers")
                        .child( FirebaseAuth.getInstance().getCurrentUser().getUid()).child( "route").setValue( route);
                Toast.makeText( JourneyInfoActivity.this, "Route changed", Toast.LENGTH_SHORT);
            }
        });
    }
}
