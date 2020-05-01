package com.example.comingsoon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {

    Button signupButton;
    Button backButton;
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( SignInActivity.this, SignUpActivity.class);
                startActivity( intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        configureContinueButton();
        configureBackButton();
        configureForgotPasswordButton();
    }

    private void configureContinueButton() {
        Button buttonContinue = (Button) findViewById( R.id.continueButton);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( SignInActivity.this, MapActivity.class) );
            }
        });

    }

    private void configureBackButton() {
        backButton = (Button) findViewById( R.id.signin_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( SignInActivity.this, MainActivity.class) );
            }
        });
    }

    private void configureForgotPasswordButton() {
        forgotPassword = (TextView) findViewById( R.id.forgot_password);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( SignInActivity.this, ForgotPasswordActivity.class) );
            }
        });
    }

}
