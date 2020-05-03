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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressBar = (ProgressBar) findViewById( R.id.signup_progress_bar);

        editTextName = (EditText) findViewById( R.id.signup_name_input);
        editTextEmail = (EditText) findViewById( R.id.signup_email_input);
        editTextPassword = (EditText) findViewById( R.id.signup_password_input);
        editTextConfirmPassword = (EditText) findViewById( R.id.signup_password_confirm_input);

        mAuth = FirebaseAuth.getInstance();

        configureBackButton();
        configureContinueButton();
        configureSignInButton();

    }

    /**
     * This method links "continue" button to Sign In Activity and completes the registration of driver
     */
    private void configureContinueButton() {
        Button buttonContinue = (Button) findViewById( R.id.signup_continue_button);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerDriver();
            }
        });
    }

    /**
     * This method links "back button" to Main Activity
     */
    private void configureBackButton() {
        Button backButton = (Button) findViewById( R.id.signup_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( SignUpActivity.this, MainActivity.class) );
            }
        });
    }

    /**
     * This method links "Sign In" button to Sign In Activity
     */
    private void configureSignInButton() {
        Button buttonSignIn = (Button) findViewById( R.id.signin_switch_button);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( SignUpActivity.this, SignInActivity.class) );
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    /**
     * This method completes the registration of driver
     */
    private void registerDriver(){

        final String NAME = editTextName.getText().toString().trim();
        final String EMAIL = editTextEmail.getText().toString().trim();
        final String PASSWORD = editTextPassword.getText().toString().trim();
        final String CONFIRM_PASSWORD = editTextConfirmPassword.getText().toString().trim();

        // a progress bar appears while registering driver
        progressBar.setVisibility( View.VISIBLE);

        // *****VALIDATIONS*****
        // checks if name field is empty
        if ( TextUtils.isEmpty( NAME) ){
            editTextName.setError( "Name required");
            editTextName.requestFocus();
            progressBar.setVisibility( View.GONE);
            return;
        }

        // checks if email field is empty
        else if ( TextUtils.isEmpty( EMAIL) ){
            editTextEmail.setError( "Email required");
            editTextEmail.requestFocus();
            progressBar.setVisibility( View.GONE);
            return; // this prevents a possible crash if email field is empty in sign up method below.
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
            return; // this prevents a possible crash if password field is empty in sign up method below.
        }

        // checks if password length is greater than 6
        else if ( PASSWORD.length() < 6 ){
            editTextPassword.setError( "Password must be at least 6 characters");
            editTextPassword.requestFocus();
            progressBar.setVisibility( View.GONE);
            return;
        }

        // checks if password confirmation matches
        else if ( !CONFIRM_PASSWORD.equals( PASSWORD) ){
            editTextConfirmPassword.setError( "Password confirmation does not match");
            editTextConfirmPassword.requestFocus();
            progressBar.setVisibility( View.GONE);
            return;
        }

        mAuth.createUserWithEmailAndPassword( EMAIL, PASSWORD).addOnCompleteListener( new SignUpListener( NAME, EMAIL));
    }

    // inner class
    class SignUpListener implements OnCompleteListener{

        // properties
        String name;
        String email;

        // constructor
        public SignUpListener( String name, String email){
            this.name = name;
            this.email = email;
        }

        // methods
        @Override
        public void onComplete(@NonNull Task task) {
            progressBar.setVisibility( View.GONE);

            if ( task.isSuccessful() ){
                Driver driver = new Driver( name, email);

                // Saving the information of driver
                FirebaseDatabase.getInstance().getReference( "Drivers")
                        .child( FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue( driver)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // if registration is successful, directs user to sign in page.
                                if ( task.isSuccessful() ) {
                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SignUpActivity.this, "Registered succesfully, verification mail sent to your address", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                                // if the drivers press the back button, they do not turn back to sign up page.
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else{
                                    Toast.makeText( SignUpActivity.this, "Some error occurred!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
            else{
                Toast.makeText( SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


}
