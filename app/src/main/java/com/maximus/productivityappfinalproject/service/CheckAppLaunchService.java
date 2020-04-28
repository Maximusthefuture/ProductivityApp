package com.maximus.productivityappfinalproject.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.maximus.productivityappfinalproject.data.ScreenReceiver;
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
import com.maximus.productivityappfinalproject.presentation.ui.Reminder;
import com.maximus.productivityappfinalproject.utils.MyPreferenceManager;
import com.maximus.productivityappfinalproject.utils.Utils;

import java.util.Calendar;

import javax.inject.Inject;

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
    private SharedPrefManager mLimitedSharedPrefs;
    private long closestHour;
    private long closestDay;
    private long timeNow;
    private MyPreferenceManager mMyPreferenceManager;
    private ScreenReceiver mScreenReceiver;
    private boolean isLockBeforeBed;

    @Override
    public void onCreate() {
        ((MyApplication) getApplication()).getAppComponent().inject(this);
        super.onCreate();

        appLaunchThread = new HandlerThread("MyHandlerThread", Process.THREAD_PRIORITY_BACKGROUND);
        //todo prefs fileName???
//        mSharedPrefManager = new SharedPrefManagerImpl(this, "TimePrefs");
        mUpdateClosestTimeUseCase = new UpdateClosestTimeUseCase(mSetClosestTimeUseCase);

        appLaunchThread.start();
        mLooper = appLaunchThread.getLooper();
        mNotificationManager = new PhoneUsageNotificationManager(this);
        mNotificationManager.createNotificationChannel();
        mHandler = new Handler(mLooper);
        IntentFilter filter = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        mScreenReceiver = new ScreenReceiver();
        registerReceiver(mScreenReceiver, filter);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        notificationMinutes = 30000;

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

//        isLockBeforeBed = MyPreferenceManager.getInstance().getBoolean(getString(R.string.show_notification_key));

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
            }
        };
        mHandler.post(mRunnable);

        return START_STICKY;
    }


    //todo some refactor
    private void resetHour() {
       Disposable d =  mAppsRepository.getPhoneUsage()
                .filter(phoneUsage -> {
                    for (PhoneUsage usage : phoneUsage) {
                        if (usage.getTimeCompletedInHour() > 0) {
                            Log.d(TAG, "test() called with: phoneUsage = [" + phoneUsage + "true]");
                            return true;
                        }
                    }
                    Log.d(TAG, "test() called with: phoneUsage = [" + phoneUsage + "false]");
                    return false;
                })
                .subscribe(phoneUsage -> {
                    for (PhoneUsage usage : phoneUsage) {
                        Log.d(TAG, "accept: before update" + usage.getPackageName() + "   " + usage.getTimeCompletedInHour());
                        usage.setTimeCompletedInHour(0);
                        mAppsRepository.updatePhoneUsage(usage);
                        Log.d(TAG, "accept: " + usage.getPackageName() + "  " + usage.getTimeCompletedInHour());
                        Log.d(TAG, "accept() called with: phoneUsage = [" + phoneUsage + "]");
                    }
                });
    }

    private void checkTimeAndResetDb() {
        if (closestHour < timeNow) {
//            Runnable hourly = () -> mResetAppUseTimeDbUseCase.resetHourAppUsage();
//            mHandler.postDelayed(hourly, 5000);
            resetHour();
            mUpdateClosestTimeUseCase.updateClosestHour();
        }

        if (closestDay < timeNow) {
            mUpdateClosestTimeUseCase.updateClosestHour();
            Runnable daily = () -> {
                mResetAppUseTimeDbUseCase.resetDayAppUsage();
//                mLimitedSharedPrefs.setTimeLimitChanged(false);
            };
            mHandler.postDelayed(daily, 5000);
            mUpdateClosestTimeUseCase.updateClosestDay();
        }
    }
    //todo баг со временем использования приложения, считает больше на n минут после сбрасывания
    private void checkAppLimitAndUpdateStats() {
        currentForegroundApp = mMyUsageStatsManagerWrapper.getForegroundApp();
        Toast toast = Toast.makeText(this, "ОСТАЛОСЬ 5 МИНУТ", Toast.LENGTH_LONG);

        if (mGetAppWithLimitUseCase.isLimitSet(currentForegroundApp)) {
            Log.d(TAG, "currentForegroundApp: " + currentForegroundApp);

            AppUsageLimitModel appUsageLimitModel = mGetAppWithLimitUseCase.appUsageLimitObservable(currentForegroundApp);
            Log.d(TAG, "TimeLimitPerHour: " + appUsageLimitModel.getTimeLimitPerHour());
            PhoneUsage phoneUsage = mGetAppWithLimitUseCase.getAppUsageData(currentForegroundApp);
            Log.d(TAG, "PhoneUsage:  " + phoneUsage.getTimeCompletedInHour());
            long limitTime = appUsageLimitModel.getTimeLimitPerHour() - phoneUsage.getTimeCompletedInHour();
            Log.d(TAG, "notificationMinutes: " + notificationMinutes);
            Log.d(TAG, "limit time: " + limitTime);
            //SingleLiveData?
            Intent intent = new Intent(this, Reminder.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            if (limitTime <= notificationMinutes) {
                //toast?
            }

            if (mSharedPrefManager.getClosestHour() < Calendar.getInstance().getTimeInMillis()
                    && phoneUsage.getTimeCompletedInHour() > 0) {
                mResetAppUseTimeDbUseCase.resetHourAppUsage();
            }
            //todo checkAppLimitAndUpdateStats: 474144 after reset
            Log.d(TAG, "checkAppLimitAndUpdateStats: " + phoneUsage.getTimeCompletedInHour());
            Log.d(TAG, "mSharedPrefs " + mSharedPrefManager.getClosestHour());

            Pair<Boolean, String> appAccessAllowed = getStatusAccessAllowedNow(appUsageLimitModel, phoneUsage);
            boolean isAppAccessAllowedNow = appAccessAllowed.first;
            String d = appAccessAllowed.second;

            if (!isAppAccessAllowedNow) {
                showHomeScreen(getApplicationContext());
                Toast.makeText(getApplication().getApplicationContext(), getString(R.string.cannot_use_app, appUsageLimitModel.getAppName(), d), Toast.LENGTH_LONG).show();
            }

            if (currentForegroundApp != null) {
                mGetAppWithLimitUseCase.updateCurrentAppStats(currentForegroundApp);
                Log.d(TAG, "updateCurrentAppStats calls");
            }

            if (Utils.isTimeBeforeBad(appUsageLimitModel, 22,
                    30, 8, 40)) {
                showHomeScreen(this);
            }
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
        unregisterReceiver(mScreenReceiver);
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
