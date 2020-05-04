package com.example.comingsoon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class HelpSettingsActivity extends AppCompatActivity {

    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_settings);

        configureBackButton();
    }

    /**
     * This method links back button to Settings Activity
     */
    private void configureBackButton() {
        backButton = (Button) findViewById( R.id.help_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( HelpSettingsActivity.this, SettingsActivity.class) );
            }
        });
    }
}
