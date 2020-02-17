package com.maximus.productivityappfinalproject.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.maximus.productivityappfinalproject.service.NotificationService;

public class ScreenReceiver extends BroadcastReceiver {

    private boolean isScreenOn;
    public static final String SCREEN_STATE = "screen_state";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            isScreenOn = true;
        } else if ((intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) || (intent.getAction().equals(Intent.ACTION_SHUTDOWN))) {
            isScreenOn = false;
        } else if ((intent.getAction().equals(Intent.ACTION_SCREEN_ON))) {
            isScreenOn = true;
        }

        Intent screenIntent = new Intent(context, NotificationService.class);
        screenIntent.putExtra(SCREEN_STATE, isScreenOn);
        context.startService(screenIntent);
    }
}
