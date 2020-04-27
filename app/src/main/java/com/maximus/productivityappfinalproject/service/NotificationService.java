package com.maximus.productivityappfinalproject.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.data.PhoneUsageDataSource;
import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.device.PhoneUsageNotificationManager;
import com.maximus.productivityappfinalproject.domain.AddPhoneUsageUseCase;
import com.maximus.productivityappfinalproject.domain.GetAppWithLimitUseCase;
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;

import javax.inject.Inject;


public class NotificationService extends Service {

    private static final String TAG = "NotificationService";
    @Inject
    PhoneUsageDataSource mDataSource;
    String time = null;
    private int count = 0;
    private boolean isScreenOn;
    private PhoneUsageNotificationManager mNotificationManager;
    private Handler mHandler;
    private Runnable mRunnable;
    private int seconds = 0;
    private BroadcastReceiver receiver;
    private AddPhoneUsageUseCase mPhoneUsageUseCase;
    private AppsRepositoryImpl mAppsRepository;
    private PhoneUsage mPhoneUsage;
    private MyUsageStatsManagerWrapper mMyUsageStatsManagerWrapper;
    private GetAppWithLimitUseCase mGetAppWithLimitUseCase;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppsRepository = new AppsRepositoryImpl(mDataSource);
        mPhoneUsageUseCase = new AddPhoneUsageUseCase(mAppsRepository);

        mMyUsageStatsManagerWrapper = new MyUsageStatsManagerWrapper(getApplicationContext(), null);


//        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
//        filter.addAction(Intent.ACTION_SCREEN_OFF);
//        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
//        receiver = new ScreenReceiver();
//        registerReceiver(receiver, filter);

        mNotificationManager = new PhoneUsageNotificationManager(this);
        mNotificationManager.createNotificationChannel();
//        mPhoneUsageDataSource = new PhoneUsageDataSourceImp(getApplicationContext());
        mAppsRepository = new AppsRepositoryImpl(mDataSource);
        mGetAppWithLimitUseCase = new GetAppWithLimitUseCase(mAppsRepository);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void updateNotification(Notification notification) {
        NotificationManagerCompat notificationManagerCompat
                = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(PhoneUsageNotificationManager.NOTIFICATION_ID, notification);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        unregisterReceiver(receiver);

    }
}
