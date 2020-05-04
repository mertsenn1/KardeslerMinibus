package com.example.comingsoon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class DriverInformationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_information);

        configureSpinner();
        configureContinueButton();
    }

    /**
     * This method links back button to Sign Up Activity
     */
    private void configureBackButton() {
        Button backButton = (Button) findViewById( R.id.info_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( DriverInformationActivity.this, SignUpActivity.class) );
            }
        });
    }

    private void configureContinueButton() {
        Button buttonContinue = (Button) findViewById( R.id.driver_continue_button);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( DriverInformationActivity.this, DriverMapActivity.class) );
            }
        });

    }

    public void configureSpinner(){
        Spinner spinner = (Spinner) findViewById(R.id.choose_route_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.route_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource( android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter( adapter);
        spinner.setOnItemSelectedListener( this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition( position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
