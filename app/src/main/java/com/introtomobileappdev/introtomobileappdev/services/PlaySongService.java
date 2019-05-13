package com.introtomobileappdev.introtomobileappdev.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import com.introtomobileappdev.introtomobileappdev.R;

public class PlaySongService extends Service {
    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alert);
        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }
}
