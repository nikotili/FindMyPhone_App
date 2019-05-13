package com.introtomobileappdev.introtomobileappdev;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.introtomobileappdev.introtomobileappdev.activities.Ac;
import com.introtomobileappdev.introtomobileappdev.activities.MainActivity;

public class StartOnBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

//        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
        {
            Intent intent1 = new Intent(context, Ac.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);

        }
    }
}
