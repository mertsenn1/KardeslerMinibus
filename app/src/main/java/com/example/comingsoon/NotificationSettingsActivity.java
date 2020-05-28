package com.example.comingsoon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * This class handles the notification settings.
 */
public class NotificationSettingsActivity extends AppCompatActivity implements TimePickerFragment.DialogListener {

    public static final String SHARED_PREFERENCES = "preferences";
    public static final String KEY_NOTIFICATION_TIME = "notificationTime";
    public static final String KEY_NOTIFICATION_TIME_STRING = "notificationString";

    private RelativeLayout timePicker;
    private Button backButton;
    private SharedPreferences sharedPreferences;
    private TextView currentTimeSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        currentTimeSettings = (TextView) findViewById( R.id.current_time_settings);

        loadData();
        configureBackButton();
        configureNotificationTimer();
    }

    /**
     * This method is to show time picker dialog
     */
    public void showTimePickerDialog(View v) {
        Log.d( "TIME", "showTimePickerDialog");
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    /**
     * This method links back button to Settings Activity
     */
    private void configureBackButton() {
        backButton = findViewById( R.id.notification_back_button);
        backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                startActivity( new Intent( NotificationSettingsActivity.this, SettingsActivity.class));
            }
        });
    }

    /**
     * This method configures notification timer
     */
    private void configureNotificationTimer() {
        timePicker = (RelativeLayout) findViewById( R.id.time_picker);
        timePicker.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                Log.d( "TIME", "onClick: clicked");
                showTimePickerDialog( view);
            }
        });
    }

    @Override
    /**
     * This method sets the notification time
     * @param time time to set
     */
    public void setTime( String time) {
        sharedPreferences = getSharedPreferences( SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString( KEY_NOTIFICATION_TIME_STRING, time);
        editor.putInt( KEY_NOTIFICATION_TIME, convertToSeconds( time));
        editor.apply();
        currentTimeSettings.setText( time);
    }

    /**
     * This method converts time ( minute ) to seconds.
     */
    public int convertToSeconds( String time) {
        int minutes = Integer.parseInt( time.substring( 0, 2));
        int seconds = Integer.parseInt( time.substring( 3));

        return ( minutes * 60) + seconds;
    }

    /**
     * This method loads data of notification time
     */
    public void loadData () {
        sharedPreferences = getSharedPreferences( SHARED_PREFERENCES, MODE_PRIVATE);
        String time = sharedPreferences.getString( KEY_NOTIFICATION_TIME_STRING, "05:00");
        currentTimeSettings.setText( time);
    }
}
