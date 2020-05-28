package com.example.comingsoon;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_ID = "bus_in_range_notification";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "bus_in_range_notification",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription( "Notifying user before bus arrives");

            NotificationManager manager = getSystemService( NotificationManager.class);
            manager.createNotificationChannel( channel);
        }
    }
}
