package com.example.comingsoon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * This class verifies the drivers register code in order to prevent any inconvenience.
 */
public class DriverCodeDialog extends AppCompatDialogFragment {

    // Properties
    private EditText editTextCode;
    private boolean isValid;
    private Map<String, Object> currentList;
    private DatabaseReference rootRef;
    private DatabaseReference codeRef;

    // Methods
    @Override
    public Dialog onCreateDialog( @Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder;
        LayoutInflater inflater;
        View view;

        isValid = false;

        rootRef = FirebaseDatabase.getInstance().getReference();
        codeRef = rootRef.child( "Driver Code");

        ValueEventListener eventListener = new codeDialogListener();
        codeRef.addListenerForSingleValueEvent( eventListener);

        builder = new AlertDialog.Builder( getActivity());
        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate( R.layout.driver_code_dialog, null);
        setCancelable( false);
        builder.setView( view);

        editTextCode = view.findViewById( R.id.driver_code);
        configureBackButton( view);
        configureContinueButton( view);

        return builder.create();
    }

    /**
     * This method deletes the data having this value
     * @param value is the code entered by user
     */
    private void deleteDataWithValue( String value){

        String key;

        key = "";

        for ( String k : currentList.keySet() ) {
            if ( value.equals( currentList.get( k)) ) {
                key = k;
                if ( isValid ) {
                    codeRef.child( key).setValue( null); // deleting data
                }

                return; // exits loop after finding the key
            }
        }
    }

    /**
     * This method directs user to sign up page if the code is valid
     * @param view is View object generated from driver_code_dialog
     */
    private void configureContinueButton( View view) {
        ImageView continueButton;

        continueButton = view.findViewById( R.id.driver_code_continue_button);
        continueButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                // getting the register code from driver and check its validity with using firebase.
                final String CODE = editTextCode.getText().toString().trim();
                if ( TextUtils.isEmpty( CODE) ) {
                    editTextCode.setError( "Code required");
                    editTextCode.requestFocus();
                } else if ( currentList.containsValue( CODE) ) {
                    dismiss();
                    Toast.makeText( getActivity(), "Code is valid. You can register.", Toast.LENGTH_SHORT).show();
                    isValid = true;
                    deleteDataWithValue( CODE); // deleting code from database not to reuse the same code
                } else if ( !isValid ) {
                    Toast.makeText( getActivity(), "Code is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * This method directs user to sign in page
     * @param view is View object generated from driver_code_dialog
     */
    private void configureBackButton( View view){
        ImageView backButton;

        backButton = view.findViewById( R.id.driver_code_back_button);
        backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                startActivity( new Intent( getActivity(), SignInActivity.class));
            }
        });
    }

    // Inner Classes
    /**
     * This listener class gets information from Firebase Database whenever a data change on Firebase.
     * In any error, gives a message.
     */
    public class codeDialogListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            // all codes are listed in the format of ( (key)data1: (value)1234 )
            currentList = ( Map<String, Object> ) dataSnapshot.getValue();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText( getActivity(), "Some error occurred", Toast.LENGTH_SHORT).show();
        }
    }
}
