package com.maximus.productivityappfinalproject.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import com.maximus.productivityappfinalproject.data.AppsRepository;
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.data.PhoneUsageDataSource;
import com.maximus.productivityappfinalproject.data.ScreenReceiver;
import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.device.PhoneUsageNotificationManager;
import com.maximus.productivityappfinalproject.domain.AddPhoneUsageUseCase;
import com.maximus.productivityappfinalproject.domain.GetAppWithLimitUseCase;
import com.maximus.productivityappfinalproject.domain.GetPhoneUsageCountUseCase;
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;
import com.maximus.productivityappfinalproject.framework.PhoneUsageDataSourceImp;
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase;

import java.util.concurrent.BlockingDeque;


public class NotificationService extends Service {

    private static final String TAG = "NotificationService";
    private int count = 0;
    private boolean isScreenOn;
    private PhoneUsageNotificationManager mNotificationManager;
    private Handler mHandler;
    private Runnable mRunnable;
    private int seconds = 0;
    private BroadcastReceiver receiver;
    private AddPhoneUsageUseCase mPhoneUsageUseCase;
    private GetPhoneUsageCountUseCase mUsageCountUseCase;
    private AppsRepositoryImpl mAppsRepository;
    private PhoneUsageDataSource mDataSource;
    private PhoneUsage mPhoneUsage;
    private MyUsageStatsManagerWrapper mMyUsageStatsManagerWrapper;

    String time = null;
    private PhoneUsageDataSourceImp mPhoneUsageDataSource;
    private GetAppWithLimitUseCase mGetAppWithLimitUseCase;

    @Override
    public void onCreate() {
        super.onCreate();
        mDataSource = new PhoneUsageDataSourceImp(this);
        mAppsRepository = new AppsRepositoryImpl(mDataSource);
        mPhoneUsageUseCase = new AddPhoneUsageUseCase(mAppsRepository);
        mUsageCountUseCase = new GetPhoneUsageCountUseCase(mAppsRepository);
        mMyUsageStatsManagerWrapper = new MyUsageStatsManagerWrapper(getApplicationContext());


        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        receiver = new ScreenReceiver();
        registerReceiver(receiver, filter);
        mNotificationManager = new PhoneUsageNotificationManager(this);
        mNotificationManager.createNotificationChannel();
        mPhoneUsageDataSource = new PhoneUsageDataSourceImp(getApplicationContext());
        mAppsRepository = new AppsRepositoryImpl(mPhoneUsageDataSource);
        mGetAppWithLimitUseCase = new GetAppWithLimitUseCase(mAppsRepository);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    //TODO insert to database!!
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isScreenOn = intent.getBooleanExtra(ScreenReceiver.SCREEN_STATE, true);
        if (isScreenOn) {
            count++;
//           String app =  mMyUsageStatsManagerWrapper.getForegroundApp();
//            Log.d(TAG, "Foreground app" + app);
            String currentForegroundApp = mMyUsageStatsManagerWrapper.getForegroundApp();
            if (currentForegroundApp != null) {
//                mGetAppWithLimitUseCase.updateCurrentAppStats(currentForegroundApp);
            }
            mPhoneUsageUseCase.insertPhoneUsage(mPhoneUsage = new PhoneUsage(count));
//            getCountFrom Db or sharedPref?
//            insertCount to Db or sharedPref
            Log.d(TAG, "onStartCommand: USAGE COUNT FROM DB " + mUsageCountUseCase.getPhoneUsageCount().getValue());
            Log.d(TAG, "onStartCommand:  PHONEUSAGE MODEL USAGE COUNT " + mPhoneUsage.getUsageCount());

            mHandler = new Handler();
        }
        mHandler = null;
        Log.d(TAG, "onStartCommand: " + isScreenOn + " usage count:  " + count);
        updateNotification(mNotificationManager.createNotification(time, count));
        startForeground(PhoneUsageNotificationManager.NOTIFICATION_ID, mNotificationManager.createNotification(time, count));
        return START_STICKY;
    }

    public void updateNotification(Notification notification) {
        NotificationManagerCompat notificationManagerCompat
                = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(PhoneUsageNotificationManager.NOTIFICATION_ID, notification);
    }

    //TODO, or start timer with remote views
    private void startPhoneUsageTimer() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int sec = seconds % 60;
                time = String.format("%d:%02d:%02d", hours, minutes, sec);
                Log.d(TAG, "run: " + time);
                seconds++;
                updateNotification(mNotificationManager.createNotification(time, count));
                mHandler.postDelayed(this, 1000);
            }
        });

    }

    private void pauseTimer(Runnable runnable) {
        mHandler.removeCallbacks(runnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        unregisterReceiver(receiver);

    }
}
