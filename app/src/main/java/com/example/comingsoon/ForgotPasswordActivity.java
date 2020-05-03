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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button backButton, resetPasswordButton;
    private EditText email;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById( R.id.forgot_password_email);

        progressBar = (ProgressBar) findViewById( R.id.forgot_password_progress_bar);

        configureBackButton();
        configureResetPasswordButton();
    }

    /**
     * This method links back button to Sign In Activity
     */
    private void configureBackButton() {
        backButton = (Button) findViewById( R.id.forgot_password_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( ForgotPasswordActivity.this, SignInActivity.class) );
            }
        });
    }

    /**
     * This method sends email to user to reset the password
     */
    private void configureResetPasswordButton(){
        resetPasswordButton = (Button) findViewById( R.id.reset_password_button);
        resetPasswordButton.setOnClickListener( new ResetPasswordListener());
    }

    // inner class
    class ResetPasswordListener implements View.OnClickListener{

        // constructor
        public ResetPasswordListener(){

        }

        // methods
        @Override
        public void onClick(View v) {
            String userEmail = email.getText().toString().trim();
            progressBar.setVisibility(View.VISIBLE);

            if (TextUtils.isEmpty( userEmail)){
                Toast.makeText( ForgotPasswordActivity.this, "Please enter your registered email", Toast.LENGTH_SHORT).show();
            }
            else{
                mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if ( task.isSuccessful() ){
                            progressBar.setVisibility( View.GONE);
                            Toast.makeText( ForgotPasswordActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent( ForgotPasswordActivity.this, SignInActivity.class);
                            intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity( intent);
                        }
                        else{
                            progressBar.setVisibility( View.GONE);
                            Toast.makeText( ForgotPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}
