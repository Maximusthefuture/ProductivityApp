package com.maximus.productivityappfinalproject.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.preference.PreferenceManager;

import com.maximus.productivityappfinalproject.MyApplication;
import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.data.PhoneUsageDataSource;
import com.maximus.productivityappfinalproject.data.prefs.SharedPrefManager;
import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.device.PhoneUsageNotificationManager;
import com.maximus.productivityappfinalproject.domain.GetAppWithLimitUseCase;
import com.maximus.productivityappfinalproject.domain.GetClosestTimeUseCase;
import com.maximus.productivityappfinalproject.domain.ResetAppUseTimeDbUseCase;
import com.maximus.productivityappfinalproject.domain.SetClosestTimeUseCase;
import com.maximus.productivityappfinalproject.domain.UpdateClosestTimeUseCase;
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;
import com.maximus.productivityappfinalproject.utils.Utils;

import java.util.Calendar;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//TODO ForegroundService
public class CheckAppLaunchService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "CheckAppLaunchService";
    @Inject
    AppsRepositoryImpl mAppsRepository;
    @Inject
    GetAppWithLimitUseCase mGetAppWithLimitUseCase;
    @Inject
    PhoneUsageDataSource mPhoneUsageDataSource;
    @Inject
    GetClosestTimeUseCase mGetClosestTimeUseCase;
    @Inject
    SetClosestTimeUseCase mSetClosestTimeUseCase;
    @Inject
    ResetAppUseTimeDbUseCase mResetAppUseTimeDbUseCase;
    @Inject
    SharedPrefManager mSharedPrefManager;
    @Inject
    MyUsageStatsManagerWrapper mMyUsageStatsManagerWrapper;

    private String currentForegroundApp;
    private long notificationMinutes;
    private boolean isScreenOn;
    private HandlerThread appLaunchThread;
    private Handler mHandler;
    private Looper mLooper;
    private Runnable mRunnable;
    private UpdateClosestTimeUseCase mUpdateClosestTimeUseCase;
    private PhoneUsageNotificationManager mNotificationManager;
    private long closestHour;
    private long closestDay;
    private long timeNow;


    @Override
    public void onCreate() {
        ((MyApplication) getApplication()).getAppComponent().inject(this);
        super.onCreate();

        appLaunchThread = new HandlerThread("MyHandlerThread", Process.THREAD_PRIORITY_BACKGROUND);
        //todo prefs fileName???

        mUpdateClosestTimeUseCase = new UpdateClosestTimeUseCase(mSetClosestTimeUseCase);

        appLaunchThread.start();
        mLooper = appLaunchThread.getLooper();
        mNotificationManager = new PhoneUsageNotificationManager(this);
        mNotificationManager.createNotificationChannel();
        mHandler = new Handler(mLooper);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        notificationMinutes = 30000;

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        startForeground(PhoneUsageNotificationManager.NOTIFICATION_ID, mNotificationManager.createNotification(this));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, 2000);
                closestHour = mGetClosestTimeUseCase.getClosestHour();
                closestDay = mGetClosestTimeUseCase.getClosestDay();
                timeNow = Calendar.getInstance().getTimeInMillis();
                checkTimeAndResetDb();
                checkAppLimitAndUpdateStats();
                resetHourDbWithRx();
            }
        };

        mHandler.post(mRunnable);
//        Observable.interval(3, TimeUnit.SECONDS)
//                .observeOn(Schedulers.single())
//                .subscribe(s -> {
//                    checkAppLimitAndUpdateStats();
//                    Log.d(TAG, "onStartCommand: " + s + "rx");
//                    Log.d(TAG, "Thread " + Thread.currentThread());
//                });

        return START_STICKY;
    }


    private void resetHour() {
        Disposable d = mAppsRepository.getPhoneUsage()
                .subscribeOn(Schedulers.single())
                .flatMap((list) -> Observable.fromIterable(list))
                .filter(phoneUsage -> phoneUsage.getTimeCompletedInHour() > 0)
                .observeOn(Schedulers.single())
                .subscribe(phoneUsage -> {
                    mResetAppUseTimeDbUseCase.resetHourAppUsage();
                    phoneUsage.setTimeCompletedInHour(0);
                    mAppsRepository.updatePhoneUsage(phoneUsage);
                });
    }

    //TODO test
    private void resetHourDbWithRx() {
        if (closestHour < timeNow) {
            resetHour();
            Log.d(TAG, "resetHourDbWithRx: ");
            mUpdateClosestTimeUseCase.updateClosestHour();
        }
    }

    private void checkTimeAndResetDb() {
        if (closestHour < timeNow) {
            mUpdateClosestTimeUseCase.updateClosestHour();
            Runnable hourly = () -> {
                mResetAppUseTimeDbUseCase.resetHourAppUsage();
            };
            mHandler.postDelayed(hourly, 5000);
            mUpdateClosestTimeUseCase.updateClosestHour();
        }

        if (closestDay < timeNow) {
            mUpdateClosestTimeUseCase.updateClosestDay();
            Runnable daily = () -> {
                mResetAppUseTimeDbUseCase.resetDayAppUsage();
            };
            mHandler.postDelayed(daily, 5000);
            mUpdateClosestTimeUseCase.updateClosestDay();
        }
    }

    //todo баг со временем использования приложения, считает больше на n минут после сбрасывания
    private void checkAppLimitAndUpdateStats() {
        currentForegroundApp = mMyUsageStatsManagerWrapper.getForegroundApp();

        AppUsageLimitModel appUsageLimitModel = mGetAppWithLimitUseCase.getAppUsageLimit(currentForegroundApp);
        PhoneUsage phoneUsage = mGetAppWithLimitUseCase.getAppUsageData(currentForegroundApp);
        //Нотификейшн за это время до окончания лимита
        long limitTime = appUsageLimitModel.getTimeLimitPerHour() - phoneUsage.getTimeCompletedInHour();

        if (mGetAppWithLimitUseCase.isLimitSet(currentForegroundApp)) {
            Pair<Boolean, String> appAccessAllowed = getStatusAccessAllowedNow(appUsageLimitModel, phoneUsage);
            boolean isAppAccessAllowedNow = appAccessAllowed.first;
            String d = appAccessAllowed.second;

            if (!isAppAccessAllowedNow) {
                showHomeScreen(getApplicationContext());
                Toast.makeText(getApplication().getApplicationContext(), getString(R.string.cannot_use_app, appUsageLimitModel.getAppName(), d), Toast.LENGTH_LONG).show();
            }
        }

        if (currentForegroundApp != null) {
            mGetAppWithLimitUseCase.updateCurrentAppStats(currentForegroundApp);
            Log.d(TAG, "updateCurrentAppStats calls");
        }

//        Intent intent = new Intent(this, Reminder.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Utils.isTimeBeforeBad(appUsageLimitModel, 22,
                30, 8, 40)) {
//            startActivity(intent);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void showHomeScreen(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public Pair<Boolean, String> getStatusAccessAllowedNow(AppUsageLimitModel appUsageLimitModel, PhoneUsage phoneUsage) {
        String msg = null;

        if (appUsageLimitModel.isAppLimited()) {
            if (phoneUsage.getTimeCompletedInHour() >= appUsageLimitModel.getTimeLimitPerHour()) {
                msg = getString(R.string.closest_hour);
                return new Pair<>(false, msg);
            }

            if (phoneUsage.getTimeCompletedInDay() >= appUsageLimitModel.getTimeLimitPerDay()) {
                msg = getString(R.string.in_this_day);
                return new Pair<>(false, msg);
            }
        }
        msg = "No limited";
        return new Pair<>(true, msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy:  SERVICE");

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, CheckAppLaunchService.class));
        } else {
            startService(new Intent(this, CheckAppLaunchService.class));
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.notification_before_lock_key))) {
//            notificationMinutes = Long.parseLong((sharedPreferences.getString(key, "")));
        }
    }
}
