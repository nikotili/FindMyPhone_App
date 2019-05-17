package com.introtomobileappdev.introtomobileappdev.activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[] {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                },
                1);


//        new ConnectionTask().execute(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), Ac.class);
        startActivity(intent);

//        Intent intent = new Intent(getApplicationContext(), SendFileService.class);
//        startService(intent);

//        PackageManager p = getPackageManager();
//        ComponentName componentName = new ComponentName(this, MainActivity.class);
//        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        finish();
    }

}
