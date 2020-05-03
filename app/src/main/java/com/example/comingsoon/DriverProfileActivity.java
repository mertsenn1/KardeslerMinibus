package com.example.comingsoon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class DriverProfileActivity extends AppCompatActivity {

    private Button backButton, logoutButton;
    private TextView changePassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);

        mAuth = FirebaseAuth.getInstance();

        configureBackButton();
        configureChangePasswordButton();
        configureLogoutButton();
    }

    /**
     * This method links back button to Settings Activity
     */
    private void configureBackButton() {
        backButton = (Button) findViewById( R.id.back_button3);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( DriverProfileActivity.this, SettingsActivity.class) );
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
    /**
     * This method performs logout process
     */
    private void configureLogoutButton(){
        logoutButton = (Button) findViewById( R.id.logout_button);
        logoutButton.setOnClickListener( new LogoutListener());
    }

    // inner class
    class LogoutListener implements View.OnClickListener{

        // constructor
        public LogoutListener(){

        }

        // methods
        @Override
        public void onClick(View v) {
            mAuth.signOut();
            finish();
            Toast.makeText( getApplicationContext(), "Signed out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent( DriverProfileActivity.this, MainActivity.class);
            intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity( intent);
        }
    }
}
