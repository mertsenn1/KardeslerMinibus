package com.example.comingsoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

/**
 * This class is an activity to show drivers information.
 */
public class DriverProfileActivity extends AppCompatActivity {

    // Properties
    private Button backButton;
    private LinearLayout logoutButton;
    private TextView changePassword, nameField, emailField, driverGreeting;
    private FirebaseAuth mAuth;

    // Methods
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_driver_profile);

        nameField = findViewById( R.id.name_field);
        emailField = findViewById (R.id.email_field);
        driverGreeting = findViewById( R.id.driver_greeting);

        mAuth = FirebaseAuth.getInstance();
        emailField.setText( Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
        nameField.setText( mAuth.getCurrentUser().getDisplayName()); //Doesn't work?
        driverGreeting.setText( "Hi, " + mAuth.getCurrentUser().getDisplayName() + "!");

        configureBackButton();
        configureChangePasswordButton();
        configureLogoutButton();
    }

    /**
     * This method links back button to Settings Activity
     */
    private void configureBackButton() {
        backButton = findViewById( R.id.back_button3);
        backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                startActivity( new Intent( DriverProfileActivity.this, DriverSettingsActivity.class));
            }
        });
    }

    /**
     * This method links "change password" button to Change Password Activity
     */
    private void configureChangePasswordButton() {
        changePassword = findViewById( R.id.changePasswordButton);
        changePassword.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                startActivity( new Intent( DriverProfileActivity.this, ChangePasswordActivity.class));
            }
        });
    }

    /**
     * This method performs logout process
     */
    private void configureLogoutButton() {
        logoutButton = findViewById( R.id.logout_layout);
        logoutButton.setOnClickListener( new LogoutListener());
    }

    // Inner Classes
    /**
     * This listener class is attached to the button which gets driver signed out.
     * Starts the sign in page activity.
     */
    class LogoutListener implements View.OnClickListener{

        // Constructors
        public LogoutListener(){

        }

        // Methods
        @Override
        public void onClick( View v) {
            mAuth.signOut();
            finish();
            Toast.makeText( getApplicationContext(), "Signed out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent( DriverProfileActivity.this, SignInActivity.class);
            startActivity( intent);
            finish();
        }
    }
}
