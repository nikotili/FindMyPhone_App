package com.introtomobileappdev.introtomobileappdev.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.introtomobileappdev.introtomobileappdev.R;
import com.introtomobileappdev.introtomobileappdev.activities.WipeDataActivity;
import com.introtomobileappdev.introtomobileappdev.tasks.SendFileTask;
import com.introtomobileappdev.introtomobileappdev.utils.Constants;

public class SendFileService extends Service {

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }



    @Override
    public void onCreate() {
        Intent notificationIntent = new Intent(this, WipeDataActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, Constants.CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText("hello")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new SendFileTask().execute(getApplicationContext());
        return START_STICKY;
    }
}
