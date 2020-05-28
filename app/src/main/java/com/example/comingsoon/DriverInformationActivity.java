package com.example.comingsoon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This class is a medium activity for the user, starts different activities depends on users.
 * Obtains drivers' route information and integrates it with the user.
 */
public class DriverInformationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Properties
    private int route;
    private boolean isRouteSelected;

    // Methods
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_driver_information);

        isRouteSelected = false;

        configureSpinner();
        configureContinueButton();
        configureBackButton();
    }

    /**
     * This method links back button to Sign Up Activity
     */
    private void configureBackButton() {
        Button backButton = findViewById( R.id.info_back);
        backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                startActivity( new Intent( DriverInformationActivity.this, SignUpActivity.class));
            }
        });
    }

    /**
     * This method gets drivers route and update the drivers information on the firebase and starts and new activity.
     */
    private void configureContinueButton() {
        Button buttonContinue = findViewById( R.id.driver_continue_button);

        buttonContinue.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                if ( isRouteSelected ) {
                    FirebaseDatabase.getInstance().getReference().child( "Drivers")
                            .child( FirebaseAuth.getInstance().getCurrentUser().getUid()).child( "route").setValue( route);
                    startActivity( new Intent( DriverInformationActivity.this, DriverMapActivity.class));
                } else {
                    Toast.makeText( DriverInformationActivity.this, "Please choose a route.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Handles the spinner for the driver to choose route.
     */
    public void configureSpinner() {
        Spinner spinner = findViewById( R.id.choose_route_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( this, R.array.route_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource( android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter( adapter);
        spinner.setOnItemSelectedListener( this);
    }

    @Override
    public void onItemSelected( AdapterView<?> parent, View view, int position, long id) {
        route = position;

        if ( position != 0 ) {
            isRouteSelected = true;
        }
    }

    @Override
    public void onNothingSelected( AdapterView<?> parent) {

    }
}
