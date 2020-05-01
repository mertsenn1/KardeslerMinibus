package com.example.comingsoon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        configureBackButton();
        configureContinueButton();
        configureSignInButton();
    }

    /**
     * This method links "continue" button to Map Activity
     */
    private void configureContinueButton() {
        Button buttonContinue = (Button) findViewById( R.id.signup_continue_button);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( SignUpActivity.this, MapActivity.class) );
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
}
