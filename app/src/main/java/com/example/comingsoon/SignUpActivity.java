package com.example.comingsoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.collection.LLRBNode;

/**
 * This is sign up page activity class,
 * The user can sign up from this page,
 * Authentication is done by using Firebase
 */
public class SignUpActivity extends AppCompatActivity {

    // Properties
    private ProgressBar progressBar;
    private EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
    private FirebaseAuth mAuth;

    // Methods
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_sign_up);

        progressBar = findViewById( R.id.signup_progress_bar);

        editTextName = findViewById( R.id.signup_name_input);
        editTextEmail = findViewById( R.id.signup_email_input);
        editTextPassword = findViewById( R.id.signup_password_input);
        editTextConfirmPassword = findViewById( R.id.signup_password_confirm_input);

        mAuth = FirebaseAuth.getInstance();

        openDialog();
        configureBackButton();
        configureContinueButton();
        configureSignInButton();
    }

    /**
     * This method open the driver code dialog page
     */
    private void openDialog() {
        DriverCodeDialog driverCodeDialog = new DriverCodeDialog();
        driverCodeDialog.show( getSupportFragmentManager(), "Driver Code Dialog");
    }

    /**
     * This method links "continue" button to Sign In Activity and completes the registration of driver
     */
    private void configureContinueButton() {
        Button buttonContinue = findViewById( R.id.signup_continue_button);
        buttonContinue.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                registerDriver();
            }
        });
    }

    /**
     * This method links "back button" to Main Activity
     */
    private void configureBackButton() {
        Button backButton = findViewById( R.id.signup_back_button);
        backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                startActivity( new Intent( SignUpActivity.this, MainActivity.class));
            }
        });
    }

    /**
     * This method links "Sign In" button to Sign In Activity
     */
    private void configureSignInButton() {
        Button buttonSignIn = findViewById( R.id.signin_switch_button);
        buttonSignIn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                startActivity( new Intent( SignUpActivity.this, SignInActivity.class));
                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    /**
     * This method completes the registration of driver
     */
    private void registerDriver() {

        final String NAME = editTextName.getText().toString().trim();
        final String EMAIL = editTextEmail.getText().toString().trim();
        final String PASSWORD = editTextPassword.getText().toString().trim();
        final String CONFIRM_PASSWORD = editTextConfirmPassword.getText().toString().trim();

        // a progress bar appears while registering driver
        progressBar.setVisibility( View.VISIBLE);

        // *****VALIDATIONS*****
        // checks if name field is empty
        if ( TextUtils.isEmpty( NAME) ) {
            editTextName.setError( "Name required");
            editTextName.requestFocus();
            progressBar.setVisibility( View.GONE);
            return;
        }

        // checks if email field is empty
        else if ( TextUtils.isEmpty( EMAIL) ) {
            editTextEmail.setError( "Email required");
            editTextEmail.requestFocus();
            progressBar.setVisibility( View.GONE);
            return; // this prevents a possible crash if email field is empty in sign up method below.
        }

        // checks if email is valid
        else if ( !Patterns.EMAIL_ADDRESS.matcher( EMAIL).matches() ) {
            editTextEmail.setError( "Please enter a valid email");
            editTextEmail.requestFocus();
            progressBar.setVisibility( View.GONE);
            return;
        }

        // checks if password field is empty
        else if ( TextUtils.isEmpty( PASSWORD) ) {
            editTextPassword.setError( "Password required");
            editTextPassword.requestFocus();
            progressBar.setVisibility( View.GONE);
            return; // this prevents a possible crash if password field is empty in sign up method below.
        }

        // checks if password length is greater than 6
        else if ( PASSWORD.length() < 6 ) {
            editTextPassword.setError( "Password must be at least 6 characters");
            editTextPassword.requestFocus();
            progressBar.setVisibility( View.GONE);
            return;
        }

        // checks if password confirmation matches
        else if ( !CONFIRM_PASSWORD.equals( PASSWORD) ) {
            editTextConfirmPassword.setError( "Password confirmation does not match");
            editTextConfirmPassword.requestFocus();
            progressBar.setVisibility( View.GONE);
            return;
        }

        mAuth.createUserWithEmailAndPassword( EMAIL, PASSWORD).addOnCompleteListener( new SignUpListener( NAME, EMAIL));
    }

    // Inner Classes
    class SignUpListener implements OnCompleteListener {

        // Properties
        String name;
        String email;

        // Constructors
        public SignUpListener( String name, String email) {
            this.name = name;
            this.email = email;
        }

        // Methods
        @Override
        public void onComplete( @NonNull Task task) {

            if ( task.isSuccessful() ) {
                final Driver driver = new Driver( name, email);

                // Saving the information of driver
                FirebaseDatabase.getInstance().getReference( "Drivers")
                        .child( FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue( driver)
                        .addOnCompleteListener( new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete( @NonNull Task<Void> task) {
                                progressBar.setVisibility( View.GONE);

                                // if registration is successful, directs user to sign in page.
                                if ( task.isSuccessful() ) {
                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener( new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete( @NonNull Task<Void> task) {
                                            if ( task.isSuccessful() ) {
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName( driver.getName()).build();

                                                user.updateProfile( profileUpdates);

                                                Toast.makeText(SignUpActivity.this, "Registered successfully, verification mail sent to your email address", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent( SignUpActivity.this, DriverInformationActivity.class);
                                                startActivity( intent);
                                                finish();
                                            } else {
                                                Toast.makeText( SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText( SignUpActivity.this, "Some error occurred!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                progressBar.setVisibility( View.GONE);
                Toast.makeText( SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
