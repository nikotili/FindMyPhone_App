package com.introtomobileappdev.introtomobileappdev.utils;

import android.content.Context;
import android.content.Intent;

import com.introtomobileappdev.introtomobileappdev.activities.Ac;
import com.introtomobileappdev.introtomobileappdev.activities.MainActivity;
import com.introtomobileappdev.introtomobileappdev.services.ConnectionService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Utils {

    public static byte[] getBytesArrayFrom(final FileInputStream fileInputStream) throws IOException
    {
        int size = fileInputStream.available();
        byte[] data = new byte[size];

        for (int i = 0; i < data.length; i++)
        {
            data[i] = (byte) fileInputStream.read();
        }

        return data;
    }

    public static String getFileAbsolutePath(Context context, String fileName, String fileFormat)
    {
        return context.getFilesDir() + "/" + fileName + "." + fileFormat;
    }

    public static void restartApp(Context context)
    {
        Intent intent = new Intent(context, Ac.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void restartService(Context context)
    {
        Intent intent = new Intent(context, ConnectionService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(intent);
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
