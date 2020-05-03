package com.example.comingsoon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DriverProfileActivity extends AppCompatActivity {

    Button backButton;
    TextView changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);

        configureBackButton();
        configureChangePasswordButton();
    }

    /**
     * This method links back button to Settings Activity
     */
    private void configureBackButton() {
        backButton = (Button) findViewById( R.id.back_button3);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( DriverProfileActivity.this, DriverSettingsActivity.class) );
            }
        });
    }

    /**
     * This method links "change password" button to Change Password Activity
     */
    private void configureChangePasswordButton() {
        changePassword = (TextView) findViewById( R.id.changePasswordButton);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( DriverProfileActivity.this, ChangePasswordActivity.class) );
            }
        });
    }
}
