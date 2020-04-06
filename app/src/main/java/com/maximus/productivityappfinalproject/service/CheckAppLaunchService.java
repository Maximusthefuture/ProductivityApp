package com.maximus.productivityappfinalproject.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.data.AppLimitDataSource;
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.data.PhoneUsageDataSource;
import com.maximus.productivityappfinalproject.data.ScreenReceiver;
import com.maximus.productivityappfinalproject.data.prefs.SharedPrefManager;
import com.maximus.productivityappfinalproject.data.prefs.SharedPrefManagerImpl;
import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.device.PhoneUsageNotificationManager;
import com.maximus.productivityappfinalproject.domain.GetAppWithLimitUseCase;
import com.maximus.productivityappfinalproject.domain.GetClosestTimeUseCase;
import com.maximus.productivityappfinalproject.domain.ResetAppUseTimeDbUseCase;
import com.maximus.productivityappfinalproject.domain.SetClosestTimeUseCase;
import com.maximus.productivityappfinalproject.domain.UpdateClosestTimeUseCase;
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;
import com.maximus.productivityappfinalproject.framework.AppLimitDataSourceImpl;
import com.maximus.productivityappfinalproject.framework.PhoneUsageDataSourceImp;
import com.maximus.productivityappfinalproject.utils.MyPreferenceManager;
import com.maximus.productivityappfinalproject.utils.Utils;

import java.util.Calendar;

//TODO ForegroundService
public class CheckAppLaunchService extends Service {

    private static final String TAG = "CheckAppLaunchService";
    private HandlerThread appLaunchThread;
    private Handler mHandler;
    private Looper mLooper;
    private Runnable mRunnable;
    private MyUsageStatsManagerWrapper mMyUsageStatsManagerWrapper;
    private GetAppWithLimitUseCase mGetAppWithLimitUseCase;
    private AppsRepositoryImpl mAppsRepository;
    private AppLimitDataSource mAppLimitDataSource;
    private PhoneUsageDataSource mPhoneUsageDataSource;
    private UpdateClosestTimeUseCase mUpdateClosestTimeUseCase;
    private GetClosestTimeUseCase mGetClosestTimeUseCase;
    private SetClosestTimeUseCase mSetClosestTimeUseCase;
    private ResetAppUseTimeDbUseCase mResetAppUseTimeDbUseCase;
    private SharedPrefManager mSharedPrefManager;
    private PhoneUsageNotificationManager mNotificationManager;
    private long closestHour;
    private long closestDay;
    private long timeNow;
    private MyPreferenceManager mMyPreferenceManager;
    private ScreenReceiver mScreenReceiver;
    String currentForegroundApp;


    @Override
    public void onCreate() {
        super.onCreate();
        mMyUsageStatsManagerWrapper = new MyUsageStatsManagerWrapper(getApplicationContext());
        appLaunchThread = new HandlerThread("MyHandlerThread", Process.THREAD_PRIORITY_BACKGROUND);
        mSharedPrefManager = new SharedPrefManagerImpl(this, "TimePrefs");
        appLaunchThread.start();
        mAppLimitDataSource = new AppLimitDataSourceImpl(getApplicationContext());
        mPhoneUsageDataSource = new PhoneUsageDataSourceImp(getApplicationContext());
        mAppsRepository = new AppsRepositoryImpl(mPhoneUsageDataSource, mAppLimitDataSource, mSharedPrefManager);
        mGetAppWithLimitUseCase = new GetAppWithLimitUseCase(mAppsRepository);
        mSetClosestTimeUseCase = new SetClosestTimeUseCase(mAppsRepository);
        mUpdateClosestTimeUseCase = new UpdateClosestTimeUseCase(mSetClosestTimeUseCase);
        mGetClosestTimeUseCase = new GetClosestTimeUseCase(mAppsRepository);
        mResetAppUseTimeDbUseCase = new ResetAppUseTimeDbUseCase(mAppsRepository);
        mLooper = appLaunchThread.getLooper();
        mNotificationManager = new PhoneUsageNotificationManager(this);
        mNotificationManager.createNotificationChannel();
        mHandler = new Handler(mLooper);
        MyPreferenceManager.init(getApplicationContext());

        IntentFilter filter = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);
        mScreenReceiver = new ScreenReceiver();
        registerReceiver(mScreenReceiver, filter);



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


        boolean isBoot = intent.getBooleanExtra(ScreenReceiver.BOOT_COMPLETED_RECEIVER, true);

        if (isBoot) {
            startForeground(PhoneUsageNotificationManager.NOTIFICATION_ID, mNotificationManager.createNotification(this));
        }


        mHandler.post(mRunnable);

        boolean foregroundWork = MyPreferenceManager.getInstance().getBoolean(getString(R.string.show_notification_key));

        if (foregroundWork) {
            startForeground(PhoneUsageNotificationManager.NOTIFICATION_ID, mNotificationManager.createNotification(this));
        }else {
            stopForeground(true);
        }
        return START_STICKY;
    }

    private void checkTimeAndResetDb() {
        if (closestHour < timeNow) {
            Runnable hourly = () -> mResetAppUseTimeDbUseCase.resetHourAppUsage();
            mHandler.postDelayed(hourly, 2000);
            mUpdateClosestTimeUseCase.updateClosestHour();
        }

        if (closestDay < timeNow) {
            mUpdateClosestTimeUseCase.updateClosestHour();
            Runnable daily = () -> mResetAppUseTimeDbUseCase.resetDayAppUsage();
            mHandler.postDelayed(daily, 2000);
            mUpdateClosestTimeUseCase.updateClosestDay();
        }
    }

    private void checkAppLimitAndUpdateStats() {
        currentForegroundApp = mMyUsageStatsManagerWrapper.getForegroundApp();

        if (mGetAppWithLimitUseCase.isLimitSet(currentForegroundApp)) {
            Log.d(TAG, "currentForegroundApp: " + currentForegroundApp);
            AppUsageLimitModel appUsageLimitModel = mGetAppWithLimitUseCase.getAppUsageLimitFromDB(currentForegroundApp);
            //TODO баг который складывает время приложений в фоне?
            PhoneUsage phoneUsage = mGetAppWithLimitUseCase.getAppUsageData(currentForegroundApp);
            Log.d(TAG, "phoneUsage: " + phoneUsage.getPackageName() + "   " + Utils.formatMillisToSeconds(phoneUsage.getTimeCompletedInHour()) + "    " + appUsageLimitModel.getPackageName());
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
        return new Pair<>(true, msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mScreenReceiver);
    }
}
