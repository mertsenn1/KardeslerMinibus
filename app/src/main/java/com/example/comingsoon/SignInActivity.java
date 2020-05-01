package com.example.comingsoon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {

    Button backButton;
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


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
                startActivity( new Intent( SignInActivity.this, MapActivity.class) );
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
     * This method links "Sign Up" button to Sign Up Activity
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

}
