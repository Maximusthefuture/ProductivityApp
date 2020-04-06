package com.maximus.productivityappfinalproject.device;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.maximus.productivityappfinalproject.MainActivity;
import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.presentation.ui.TrackingListFragment;

public class PhoneUsageNotificationManager {

    public static final String CHANNEL_ID = "channel_id_1";
    public static final int NOTIFICATION_ID = 1;
    private Context mContext;

    public PhoneUsageNotificationManager(Context context) {
        mContext = context;
    }

    /**
     * @param
     * @return {@link Notification}
     */
    public Notification createNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


        builder
                .setSmallIcon(R.mipmap.ic_launcher_mine)
                .setContentText("Так держать!")
                .setVibrate(null)
                .setContentIntent(pendingIntent)
                .setChannelId(CHANNEL_ID);
        Notification notification = builder.build();

        return notification;

    }

    //TODO add String resource
    public void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Phone usage channel";
            String description = "Monitoring phone usage timer, and count";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager
                    = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
