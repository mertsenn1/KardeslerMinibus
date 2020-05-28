package com.example.comingsoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

/**
 * This is simple sign in activity class for the drivers
 */
public class SignInActivity extends AppCompatActivity {

    // Properties
    private Button backButton;
    private TextView forgotPassword;
    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    // Methods
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById( R.id.signin_email_text_input);
        editTextPassword = findViewById( R.id.signin_password_input);
        progressBar = findViewById( R.id.signin_progress_bar);

        configureContinueButton();
        configureBackButton();
        configureForgotPasswordButton();
        configureSignUpButton();

        editTextEmail.setOnFocusChangeListener(new FocusListener());
        editTextPassword.setOnFocusChangeListener(new FocusListener());
    }

    /**
     * This method links "Continue" button to Map Activity
     */
    private void configureContinueButton() {
        Button buttonContinue = findViewById( R.id.signin_continue_button);
        buttonContinue.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                driverLogin();
            }
        });

    }

    /**
     * This method links back button to Main Activity
     */
    private void configureBackButton() {
        backButton = findViewById( R.id.signin_back_button);
        backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                startActivity( new Intent( SignInActivity.this, MainActivity.class));
            }
        });
    }

    /**
     * This method links "Forgot Password?" text view to Forgot Password Activity
     */
    private void configureForgotPasswordButton() {
        forgotPassword = findViewById( R.id.forgot_password_textview);
        forgotPassword.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                startActivity( new Intent( SignInActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    /**
     * This method links "Sign Up" button to Sign Up Activit
     */
    private void configureSignUpButton() {
        Button buttonSignIn = findViewById( R.id.signup_switch_button);
        buttonSignIn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                startActivity( new Intent( SignInActivity.this, SignUpActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }


    /**
     * This method completes the login of the driver.
     */
    private void driverLogin() {

        final String EMAIL = editTextEmail.getText().toString().trim();
        final String PASSWORD = editTextPassword.getText().toString().trim();

        // a progress bar appears while logging in.
        progressBar.setVisibility(View.VISIBLE);

        // *****VALIDATIONS*****
        // checks if email field is empty
        if ( TextUtils.isEmpty(EMAIL) ) {
            editTextEmail.setError( "Email required");
            editTextEmail.requestFocus();
            progressBar.setVisibility( View.GONE);
            return; // this prevents a possible crash if email field is empty in sign in method below.
        }

        // checks if email is valid
        else if ( !Patterns.EMAIL_ADDRESS.matcher(EMAIL).matches())  {
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
            return; // this prevents a possible crash if password field is empty in sign in method below.
        }

        // checks if password is greater than 6
        else if ( PASSWORD.length() < 6 ) {
            editTextPassword.setError( "Password must be at least 6 characters");
            editTextPassword.requestFocus();
            progressBar.setVisibility( View.GONE);
            return;
        }

        progressBar.setVisibility( View.VISIBLE);

        mAuth.signInWithEmailAndPassword( EMAIL, PASSWORD).addOnCompleteListener( new SignInListener());
    }

    // Inner Classes
    class SignInListener implements OnCompleteListener {

        // Constructors
        public SignInListener(){

        }

        // Methods
        @Override
        public void onComplete( @NonNull Task task) {
            progressBar.setVisibility( View.GONE);

            // if logging in is successful, directs driver to map activity.
            if ( task.isSuccessful() ) {
                if ( mAuth.getCurrentUser().isEmailVerified() ) {

                    // For now it directs the user to driver profile activity
                    Intent intent = new Intent( SignInActivity.this, DriverMapActivity.class);
                    startActivity( intent);
                    finish();
                } else {
                    Toast.makeText( getApplicationContext(), "Please verify your email address", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText( getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class FocusListener implements View.OnFocusChangeListener {

        // Constructors
        public FocusListener() {

        }

        // Methods
        @Override
        public void onFocusChange (View v, boolean hasFocus) {
            if ( !hasFocus ) {
                hideKeyboard( v);
            }
        }
    }

    public void hideKeyboard( View view) {
        InputMethodManager inputMethodManager = ( InputMethodManager ) getSystemService( Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow( view.getWindowToken(), 0);
    }
}