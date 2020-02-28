package com.maximus.productivityappfinalproject.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.maximus.productivityappfinalproject.data.AppLimitDataSource;
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.data.PhoneUsageDataSource;
import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.domain.GetAppWithLimitUseCase;
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;
import com.maximus.productivityappfinalproject.framework.AppLimitDataSourceImpl;
import com.maximus.productivityappfinalproject.framework.PhoneUsageDataSourceImp;

public class CheckAppLaunchService extends Service {

    private HandlerThread appLaunchThread;
    private Handler mHandler;
    private Looper mLooper;
    private Runnable mRunnable;
    private MyUsageStatsManagerWrapper mMyUsageStatsManagerWrapper;
    private GetAppWithLimitUseCase mGetAppWithLimitUseCase;
    private AppsRepositoryImpl mAppsRepository;
    private AppLimitDataSource mAppLimitDataSource;
    private PhoneUsageDataSource mPhoneUsageDataSource;
    long currentTime;
    private static final String TAG = "CheckAppLaunchService";


    @Override
    public void onCreate() {
        super.onCreate();
        mMyUsageStatsManagerWrapper = new MyUsageStatsManagerWrapper(getApplicationContext());
        appLaunchThread = new HandlerThread("MyHandlerThread", Process.THREAD_PRIORITY_BACKGROUND);
        appLaunchThread.start();
        mAppLimitDataSource = new AppLimitDataSourceImpl(getApplicationContext());
        mPhoneUsageDataSource = new PhoneUsageDataSourceImp(getApplicationContext());
        mAppsRepository = new AppsRepositoryImpl(mPhoneUsageDataSource ,mAppLimitDataSource);
        mGetAppWithLimitUseCase = new GetAppWithLimitUseCase(mAppsRepository);
        mLooper = appLaunchThread.getLooper();
        mHandler = new Handler(mLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, 2000);
                String currentForegroundApp = mMyUsageStatsManagerWrapper.getForegroundApp();
                if (currentForegroundApp != null) {
                    mGetAppWithLimitUseCase.updateCurrentAppStats(currentForegroundApp);
                }
                Log.d(TAG, "currentForegroundApp " + currentForegroundApp);
                if (mGetAppWithLimitUseCase.isLimitSet(currentForegroundApp)) {
                    AppUsageLimitModel appUsageLimitModel =  mGetAppWithLimitUseCase.getAppUsageLimitFromDB();
                    PhoneUsage phoneUsage = mGetAppWithLimitUseCase.getAppUsageData(currentForegroundApp);
                    Pair<Boolean, String> appAccessAllowed = getStatusAccessAllowedNow(appUsageLimitModel, phoneUsage); // модель с временем использования);
                    boolean isAppAccessAllowedNow = appAccessAllowed.first;
                    Log.d(TAG, "PHONE PACKAGE NAEM" + phoneUsage.getPackageName());
                    Log.d(TAG, "PHONECOMPLETED " + phoneUsage.getTimeCompletedInHour());
                    Log.d(TAG, "AppLIMITED " + appUsageLimitModel.getTimeLimitPerHour());
                    String d = appAccessAllowed.second;

                    if (!isAppAccessAllowedNow) {
                        showHomeScreen(getApplicationContext());
                        Toast.makeText(CheckAppLaunchService.this, "You cant use this app today" + d, Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
        mHandler.post(mRunnable);

        return START_STICKY;
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
        String msg;
        if (appUsageLimitModel.isAppLimited()) {
            if (appUsageLimitModel.getTimeLimitPerHour() <= phoneUsage.getTimeCompletedInHour()) {
                msg = "time limit";
                return new Pair<>(false, msg);
            }

            if (appUsageLimitModel.getTimeLimitPerDay() <= phoneUsage.getTimeCompletedInDay()) {
                msg = "time limit day";
                return new Pair<>(false, msg);
            }
        }
        return new Pair<>(true, "");
    }
}
