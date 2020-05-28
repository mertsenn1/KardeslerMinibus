package com.example.comingsoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This class simply changes the drivers password by using Firebase.
 */
public class ChangePasswordActivity extends AppCompatActivity {

    // Properties
    private EditText oldPasswordText;
    private EditText newPasswordText;
    private EditText confirmNewPasswordText;
    private ProgressBar progressBar;

    // Methods
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_change_password);

        progressBar = findViewById( R.id.change_password_progress_bar);

        oldPasswordText = findViewById( R.id.old_password_input);
        newPasswordText = findViewById( R.id.new_password_input);
        confirmNewPasswordText = findViewById( R.id.confirm_new_password_input);

        configureBackButton();
        configureChangePasswordButton();
    }

    /**
     * This method links back button to Settings Activity
     */
    private void configureBackButton() {
        Button backButton;

        backButton = findViewById( R.id.back_button7);
        backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                startActivity( new Intent( ChangePasswordActivity.this, DriverProfileActivity.class));
            }
        });
    }

    /**
     * This method completes the password change process
     */
    private void configureChangePasswordButton() {
        Button changePasswordButton;
        changePasswordButton = findViewById( R.id.change_password_button);
        changePasswordButton.setOnClickListener( new ChangePasswordListener());
    }

    // Inner Classes
    /**
     * This listener class helps to achieve changing password
     */
    class ChangePasswordListener implements View.OnClickListener {

        // Constructors
        public ChangePasswordListener() {

        }

        // Methods
        @Override
        // When the user clicks the change button
        public void onClick( View v) {
            progressBar.setVisibility( View.VISIBLE);

            final String oldPassword, newPassword, confirmNewPassword;

            oldPassword = oldPasswordText.getText().toString().trim();
            newPassword = newPasswordText.getText().toString().trim();
            confirmNewPassword = confirmNewPasswordText.getText().toString().trim();

            // The conditions to get password properly
            if ( TextUtils.isEmpty( oldPassword) ){
                oldPasswordText.setError( "Password is required");
                oldPasswordText.requestFocus();
                progressBar.setVisibility( View.GONE);
            } else if ( TextUtils.isEmpty( newPassword) ) {
                newPasswordText.setError( "Password is required");
                newPasswordText.requestFocus();
                progressBar.setVisibility( View.GONE);
            } else if ( oldPassword.length() < 6 ) {
                oldPasswordText.setError( "Password must be at least 6 characters");
                oldPasswordText.requestFocus();
                progressBar.setVisibility( View.GONE);
            } else if ( newPassword.length() < 6 ) {
                newPasswordText.setError( "Password must be at least 6 characters");
                newPasswordText.requestFocus();
                progressBar.setVisibility( View.GONE);
            } else if ( !confirmNewPassword.equals( newPassword) ) {
                confirmNewPasswordText.setError( "Password confirmation does not match");
                confirmNewPasswordText.requestFocus();
                progressBar.setVisibility( View.GONE);
            } else {
                final FirebaseUser USER = FirebaseAuth.getInstance().getCurrentUser();
                AuthCredential credential = EmailAuthProvider.getCredential( USER.getEmail(), oldPassword);

                USER.reauthenticate( credential).addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    /**
                     * If the firebase access is successful and password is changed gives a message and starts a new intent. Otherwise gives error message as toast.
                     */
                    public void onComplete( @NonNull Task<Void> task) {
                        if ( task.isSuccessful() ) {
                            USER.updatePassword( newPassword).addOnCompleteListener( new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete( @NonNull Task<Void> task) {
                                    if ( task.isSuccessful() ) {
                                        progressBar.setVisibility( View.GONE);
                                        Toast.makeText( ChangePasswordActivity.this, "Password has been changed", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent( ChangePasswordActivity.this, DriverProfileActivity.class);
                                        startActivity( intent);
                                        finish();

                                    } else {
                                        progressBar.setVisibility( View.GONE);
                                        Toast.makeText( ChangePasswordActivity.this, "Password could not be changed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            progressBar.setVisibility( View.GONE);
                            Toast.makeText( ChangePasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }}
    }}
