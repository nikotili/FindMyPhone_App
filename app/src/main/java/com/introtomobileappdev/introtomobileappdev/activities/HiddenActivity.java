package com.introtomobileappdev.introtomobileappdev.activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.introtomobileappdev.introtomobileappdev.services.ConnectionService;
import com.introtomobileappdev.introtomobileappdev.tasks.ConnectionTask;

public class HiddenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(getApplicationContext(), ConnectionService.class);
//        startService(intent);
        new ConnectionTask().execute(getApplicationContext());
        finish();
    }
}
