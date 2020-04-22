package com.maximus.productivityappfinalproject.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.maximus.productivityappfinalproject.service.CheckAppLaunchService;
import com.maximus.productivityappfinalproject.service.NotificationService;

public class ScreenReceiver extends BroadcastReceiver {

    private boolean isScreenOn;
    private boolean bootCompleted;
    public static final String SCREEN_STATE = "screen_state";
    public static final String BOOT_COMPLETED_RECEIVER = "boot_completed";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            bootCompleted = true;
            isScreenOn = true;
            Log.d("SCREENRECEIVER", "BOOT_COMPLETED");
        } else if ((intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) || (intent.getAction().equals(Intent.ACTION_SHUTDOWN))) {
            isScreenOn = false;
        } else if ((intent.getAction().equals(Intent.ACTION_SCREEN_ON))) {
            isScreenOn = true;
        } else if (intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON")) {
            bootCompleted = true;
        }

        Intent screenIntent = new Intent(context, CheckAppLaunchService.class);
        screenIntent.putExtra(SCREEN_STATE, isScreenOn);
        screenIntent.putExtra(BOOT_COMPLETED_RECEIVER, bootCompleted);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(screenIntent);
        } else {
            context.startService(screenIntent);
        }

    }
}
