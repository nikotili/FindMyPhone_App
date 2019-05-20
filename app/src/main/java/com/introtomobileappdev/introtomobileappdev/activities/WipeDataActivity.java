package com.introtomobileappdev.introtomobileappdev.activities;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.introtomobileappdev.introtomobileappdev.Admin;
import com.introtomobileappdev.introtomobileappdev.R;


public class WipeDataActivity extends AppCompatActivity {

    DevicePolicyManager devicePolicyManager;
    ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        finish();
    }
}
