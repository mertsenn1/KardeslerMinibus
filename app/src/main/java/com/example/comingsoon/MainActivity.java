package com.example.comingsoon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This is the mainActivity class, the first screen when user opens the app.
 */
public class MainActivity extends AppCompatActivity {

    // Methods
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        setContentView( R.layout.activity_main);

        configurePassengerButton();
        configureDriverButton();
    }

    /**
     * This method links "Passenger Entry" button to Map Activity
     */
    private void configurePassengerButton() {
        Button buttonPassenger = findViewById( R.id.passengerLoginButton);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged( @NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if ( user != null ) {

                    // User is signed in
                    Log.d("APP", "onAuthStateChanged:signed_in: " + user.getUid());
                } else {

                    // User is signed out
                    Log.d("APP", "onAuthStateChanged:signed_out");
                }
            }
        };

        mAuth.addAuthStateListener( authListener);

        //checking the exceptions
        if ( FirebaseAuth.getInstance().getCurrentUser() == null ) {
            mAuth.signInAnonymously()
                    .addOnFailureListener( this, new OnFailureListener() {
                        @Override
                        public void onFailure( @NonNull Exception e) {
                            Log.w( "APP", "signInAnonymously failure: ", e);
                        }
                    })
                    .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete( @NonNull Task<AuthResult> task) {
                            Log.d( "APP", "signInAnonymously:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful()) {
                                Log.w( "APP", "signInAnonymously", task.getException());
                            }
                        }
                    });
        }

        buttonPassenger.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                startActivity( new Intent( MainActivity.this, MapActivity.class));
            }
        });
    }

    /**
     * This method links "Driver Login" button to Sign In Activity
     */
    private void configureDriverButton() {
        Button buttonPassenger = findViewById( R.id.driverLoginButton);
        buttonPassenger.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                startActivity( new Intent( MainActivity.this, SignInActivity.class));
            }
        });
    }
}
