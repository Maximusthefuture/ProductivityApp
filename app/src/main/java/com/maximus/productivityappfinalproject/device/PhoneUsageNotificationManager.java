package com.maximus.productivityappfinalproject.device;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.maximus.productivityappfinalproject.R;

public class PhoneUsageNotificationManager {

    public static final String CHANNEL_ID = "channel_id_1";
    public static final int NOTIFICATION_ID = 1;
    private Context mContext;

    public PhoneUsageNotificationManager(Context context) {
        mContext = context;
    }

    /**
     * @param phoneUsedTime Время использования телефоном
     * @param count         Количество разблокирований телефона
     * @return {@link Notification}
     */
    public Notification createNotification(String phoneUsedTime, int count) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID);

        builder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Time: " + phoneUsedTime)
                .setContentText("Usage count: " + count)
                .setVibrate(null)
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
