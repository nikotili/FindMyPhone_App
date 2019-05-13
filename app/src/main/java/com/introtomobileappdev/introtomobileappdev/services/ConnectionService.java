package com.introtomobileappdev.introtomobileappdev.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.introtomobileappdev.introtomobileappdev.tasks.ConnectionTask;
import com.introtomobileappdev.introtomobileappdev.utils.Constants;
import com.introtomobileappdev.introtomobileappdev.R;
import com.introtomobileappdev.introtomobileappdev.activities.MainActivity;

public class ConnectionService extends Service {

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }



    @Override
    public void onCreate() {
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                0, notificationIntent, 0);
//
//        Notification notification = new NotificationCompat.Builder(this, Constants.CHANNEL_ID)
//                .setContentTitle("Example Service")
//                .setContentText("hello")
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentIntent(pendingIntent)
//                .build();
//
//        startForeground(1, notification);
        new ConnectionTask().execute(getApplicationContext());
    }
}
