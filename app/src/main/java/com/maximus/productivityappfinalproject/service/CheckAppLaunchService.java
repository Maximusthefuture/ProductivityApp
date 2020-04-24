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

import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.data.AppLimitDataSource;
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.data.PhoneUsageDataSource;
import com.maximus.productivityappfinalproject.data.ScreenReceiver;
import com.maximus.productivityappfinalproject.data.prefs.SharedPrefManager;
import com.maximus.productivityappfinalproject.data.prefs.SharedPrefManagerImpl;
import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.device.PhoneUsageNotificationManager;
import com.maximus.productivityappfinalproject.di.AppComponent;
import com.maximus.productivityappfinalproject.di.AppModule;
import com.maximus.productivityappfinalproject.di.ContextModule;
import com.maximus.productivityappfinalproject.di.DaggerAppComponent;
import com.maximus.productivityappfinalproject.domain.GetAppWithLimitUseCase;
import com.maximus.productivityappfinalproject.domain.GetClosestTimeUseCase;
import com.maximus.productivityappfinalproject.domain.ResetAppUseTimeDbUseCase;
import com.maximus.productivityappfinalproject.domain.SetClosestTimeUseCase;
import com.maximus.productivityappfinalproject.domain.UpdateClosestTimeUseCase;
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;
import com.maximus.productivityappfinalproject.framework.AppLimitDataSourceImpl;
import com.maximus.productivityappfinalproject.framework.PhoneUsageDataSourceImp;
import com.maximus.productivityappfinalproject.presentation.ui.Reminder;
import com.maximus.productivityappfinalproject.utils.MyPreferenceManager;
import com.maximus.productivityappfinalproject.utils.Utils;

import java.util.Calendar;

import javax.inject.Inject;

//TODO ForegroundService
public class CheckAppLaunchService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "CheckAppLaunchService";
    String currentForegroundApp;
    long notificationMinutes;
//    @Inject
    GetAppWithLimitUseCase mGetAppWithLimitUseCase;
    //    @Inject
    AppLimitDataSource mAppLimitDataSource;
    boolean isScreenOn;
    private HandlerThread appLaunchThread;
    private Handler mHandler;
    private Looper mLooper;
    private Runnable mRunnable;
    private MyUsageStatsManagerWrapper mMyUsageStatsManagerWrapper;
    private AppsRepositoryImpl mAppsRepository;
    private PhoneUsageDataSource mPhoneUsageDataSource;
    private UpdateClosestTimeUseCase mUpdateClosestTimeUseCase;
    private GetClosestTimeUseCase mGetClosestTimeUseCase;
    private SetClosestTimeUseCase mSetClosestTimeUseCase;
    private ResetAppUseTimeDbUseCase mResetAppUseTimeDbUseCase;
    private SharedPrefManager mSharedPrefManager;
    private PhoneUsageNotificationManager mNotificationManager;
    private SharedPrefManagerImpl mLimitedSharedPrefs;
    private long closestHour;
    private long closestDay;
    private long timeNow;
    private MyPreferenceManager mMyPreferenceManager;
    private ScreenReceiver mScreenReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        AppComponent f = DaggerAppComponent.builder().appModule(new AppModule()).contextModule(new ContextModule(this)).build();
        f.inject(this);
        mMyUsageStatsManagerWrapper = new MyUsageStatsManagerWrapper(getApplicationContext());
        appLaunchThread = new HandlerThread("MyHandlerThread", Process.THREAD_PRIORITY_BACKGROUND);
        mSharedPrefManager = new SharedPrefManagerImpl(this, "TimePrefs");
        mLimitedSharedPrefs = new SharedPrefManagerImpl(this, "limited_prefs");
        mAppLimitDataSource = new AppLimitDataSourceImpl(getApplicationContext());
        mPhoneUsageDataSource = new PhoneUsageDataSourceImp(getApplicationContext());
        mAppsRepository = new AppsRepositoryImpl(mPhoneUsageDataSource, mAppLimitDataSource, mSharedPrefManager);
        mGetAppWithLimitUseCase = new GetAppWithLimitUseCase(mAppsRepository);
        mSetClosestTimeUseCase = new SetClosestTimeUseCase(mAppsRepository);
        mUpdateClosestTimeUseCase = new UpdateClosestTimeUseCase(mSetClosestTimeUseCase);
        mGetClosestTimeUseCase = new GetClosestTimeUseCase(mAppsRepository);
        mResetAppUseTimeDbUseCase = new ResetAppUseTimeDbUseCase(mAppsRepository);
        appLaunchThread.start();
        mLooper = appLaunchThread.getLooper();
        mNotificationManager = new PhoneUsageNotificationManager(this);
        mNotificationManager.createNotificationChannel();
        mHandler = new Handler(mLooper);
        MyPreferenceManager.init(getApplicationContext());

        IntentFilter filter = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        mScreenReceiver = new ScreenReceiver();
        registerReceiver(mScreenReceiver, filter);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        notificationMinutes = 300000;

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
            }
        };

        mHandler.post(mRunnable);

        return START_STICKY;
    }

    private void checkTimeAndResetDb() {
        if (closestHour < timeNow) {
            Runnable hourly = () -> mResetAppUseTimeDbUseCase.resetHourAppUsage();
            mHandler.postDelayed(hourly, 5000);
            mUpdateClosestTimeUseCase.updateClosestHour();
        }

        if (closestDay < timeNow) {
            mUpdateClosestTimeUseCase.updateClosestHour();
            Runnable daily = () -> {
                mResetAppUseTimeDbUseCase.resetDayAppUsage();
                mLimitedSharedPrefs.setTimeLimitChanged(false);
            };
            mHandler.postDelayed(daily, 5000);
            mUpdateClosestTimeUseCase.updateClosestDay();
        }
    }

    private void checkAppLimitAndUpdateStats() {
        currentForegroundApp = mMyUsageStatsManagerWrapper.getForegroundApp();
        Toast toast = Toast.makeText(this, "ОСТАЛОСЬ 5 МИНУТ", Toast.LENGTH_LONG);

        if (mGetAppWithLimitUseCase.isLimitSet(currentForegroundApp)) {
            Log.d(TAG, "currentForegroundApp: " + currentForegroundApp);

            AppUsageLimitModel appUsageLimitModel = mGetAppWithLimitUseCase.getAppUsageLimitFromDB(currentForegroundApp);
            Log.d(TAG, "TimeLimitPerHour: " + appUsageLimitModel.getTimeLimitPerHour());

            PhoneUsage phoneUsage = mGetAppWithLimitUseCase.getAppUsageData(currentForegroundApp);
            long limitTime = appUsageLimitModel.getTimeLimitPerHour() - phoneUsage.getTimeCompletedInHour();
            Log.d(TAG, "notificationMinutes: " + notificationMinutes);
            Log.d(TAG, "limit time: " + limitTime);
            //SingleLiveData?
            Intent intent = new Intent(this, Reminder.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            if (limitTime <= notificationMinutes) {
//                toast.show();
//                toast.cancel();

//              this.getApplicationContext().startActivity(intent);
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
                Toast.makeText(CheckAppLaunchService.this, getString(R.string.cannot_use_app, appUsageLimitModel.getAppName(), d), Toast.LENGTH_LONG).show();
            }

            if (currentForegroundApp != null) {
                mGetAppWithLimitUseCase.updateCurrentAppStats(currentForegroundApp);
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
