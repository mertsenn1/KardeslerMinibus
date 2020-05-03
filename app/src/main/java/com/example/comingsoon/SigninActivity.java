package com.example.comingsoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
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

public class SignInActivity extends AppCompatActivity {

    private Button backButton;
    private TextView forgotPassword;
    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById( R.id.signin_email_text_input);
        editTextPassword = (EditText) findViewById( R.id.signin_password_input);
        progressBar = (ProgressBar) findViewById( R.id.signin_progress_bar);

        configureContinueButton();
        configureBackButton();
        configureForgotPasswordButton();
        configureSignUpButton();

    }

    /**
     * This method links "Continue" button to Map Activity
     */
    private void configureContinueButton() {
        Button buttonContinue = (Button) findViewById( R.id.signin_continue_button);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    driverLogin();
            }
        });

    }

    /**
     * This method links back button to Main Activity
     */
    private void configureBackButton() {
        backButton = (Button) findViewById( R.id.signin_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( SignInActivity.this, MainActivity.class) );
            }
        });
    }

    /**
     * This method links "Forgot Password?" text view to Forgot Password Activity
     */
    private void configureForgotPasswordButton() {
        forgotPassword = (TextView) findViewById( R.id.forgot_password_textview);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( SignInActivity.this, ForgotPasswordActivity.class) );
            }
        });
    }

    /**
     * This method links "Sign Up" button to Sign Up Activit
     */
    private void configureSignUpButton() {
        Button buttonSignIn = (Button) findViewById( R.id.signup_switch_button);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( SignInActivity.this, SignUpActivity.class) );
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
        progressBar.setVisibility( View.VISIBLE);

        // *****VALIDATIONS*****
        // checks if email field is empty
        if ( TextUtils.isEmpty( EMAIL) ){
            editTextEmail.setError( "Email required");
            editTextEmail.requestFocus();
            progressBar.setVisibility( View.GONE);
            return; // this prevents a possible crash if email field is empty in sign in method below.
        }

        // checks if email is valid
        else if ( !Patterns.EMAIL_ADDRESS.matcher( EMAIL).matches() ){
            editTextEmail.setError( "Please enter a valid email");
            editTextEmail.requestFocus();
            progressBar.setVisibility( View.GONE);
            return;
        }

        // checks if password field is empty
        else if ( TextUtils.isEmpty( PASSWORD) ){
            editTextPassword.setError( "Password required");
            editTextPassword.requestFocus();
            progressBar.setVisibility( View.GONE);
            return; // this prevents a possible crash if password field is empty in sign in method below.
        }

        // checks if password is greater than 6
        else if ( PASSWORD.length() < 6 ) {
            editTextPassword.setError("Password must be at least 6 characters");
            editTextPassword.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        progressBar.setVisibility( View.VISIBLE);

        mAuth.signInWithEmailAndPassword( EMAIL, PASSWORD).addOnCompleteListener( new SignInListener());

    }

    // inner class
    class SignInListener implements OnCompleteListener{

        // constructor
        public SignInListener(){

        }

        // methods
        @Override
        public void onComplete(@NonNull Task task) {
            progressBar.setVisibility(View.GONE);

            // if logging in is successful, directs driver to map activity.
            if (task.isSuccessful()) {
                if (mAuth.getCurrentUser().isEmailVerified()) {
                    // For now it directs the user to driver profile activity
                    Intent intent = new Intent(SignInActivity.this, DriverProfileActivity.class);
                    // if the drivers press the back button, they do not turn back to login page.
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please verify your email address", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

}