package com.example.comingsoon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class TimePickerFragment extends AppCompatDialogFragment {

    // Properties
    private EditText editTextTime;
    private DialogListener listener;
    // Methods
    @Override
    public Dialog onCreateDialog( @Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder;
        LayoutInflater inflater;
        View view;


        builder = new AlertDialog.Builder( getActivity());
        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate( R.layout.layout_timepicker, null);
        builder.setView( view);

        editTextTime = view.findViewById( R.id.notification_time);
        configureBackButton( view);
        configureContinueButton( view);

        return builder.create();
    }

    /**
     * This method sets the notification time
     */
    private void configureContinueButton( View view) {
        ImageView continueButton;

        continueButton = view.findViewById( R.id.driver_code_continue_button);
        continueButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                String time = editTextTime.getText().toString();
                listener.setTime( time);
                dismiss();
            }
        });
    }

    /**
     * This method configures back button
     */
    private void configureBackButton( View view){
        ImageView backButton;

        backButton = view.findViewById( R.id.driver_code_back_button);
        backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException( context.toString() + "must implement ExampleDialogListener");
        }
    }

    public interface DialogListener {
        void setTime( String time);
    }

}
