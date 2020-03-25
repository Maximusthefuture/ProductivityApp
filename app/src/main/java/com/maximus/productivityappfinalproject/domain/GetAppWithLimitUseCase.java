package com.maximus.productivityappfinalproject.domain;

import android.util.Log;

import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase;

import java.util.List;

public class GetAppWithLimitUseCase {
    private AppUsageLimitModel mAppUsageLimitModel;
    private AppsRepositoryImpl mAppsRepository;
    private String prevApp;
    private static final String noAPP = "NO_APP";
    private long currentTime;
    private static final String TAG = "GetAppWithLimitUseCase";


    public GetAppWithLimitUseCase(AppsRepositoryImpl appsRepository) {
        mAppsRepository = appsRepository;
        prevApp = noAPP;
    }


    public boolean isLimitSet(String packageName) {
        String currentPackageName;
        List<AppUsageLimitModel> list = mAppsRepository.getLimitedItems();
        for (AppUsageLimitModel usageLimitModel : list) {
            currentPackageName = usageLimitModel.getPackageName();
            if (currentPackageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public AppUsageLimitModel getAppUsageLimitFromDB(String packageName) {
        AppUsageLimitModel appUsageLimitModel = null;
        List<AppUsageLimitModel> list = mAppsRepository.getLimitedItems();
        for (AppUsageLimitModel usageLimitModel : list) {
                boolean setLimited = usageLimitModel.isAppLimited();
                if (usageLimitModel.isAppLimited() && usageLimitModel.getPackageName().equals(packageName)) {
                    appUsageLimitModel = new AppUsageLimitModel(usageLimitModel.getAppName(), usageLimitModel.getTimeLimitPerDay(), usageLimitModel.getTimeLimitPerHour(), setLimited);
                }
        }
        return appUsageLimitModel;
    }


    //TODO Переместить в другое место более подходящее по логике
    public PhoneUsage getAppUsageData(String appPackageName) {
        PhoneUsage phoneUsageModel = new PhoneUsage();
        String currentPackageName;
        boolean found = false;
        List<PhoneUsage> list = mAppsRepository.getPhoneUsageData();
        for (PhoneUsage phoneUsage : list) {
            currentPackageName = phoneUsage.getPackageName();
            if (currentPackageName.equals(appPackageName)) {
                found = true;
            }
            if (found) {
                long timeUsedThisDay = phoneUsage.getTimeCompletedInDay();
                long timeUsedThisHour = phoneUsage.getTimeCompletedInHour();
                phoneUsageModel = new PhoneUsage(appPackageName, timeUsedThisHour, timeUsedThisDay);

            } else {
                phoneUsageModel = new PhoneUsage(currentPackageName, 0, 0);
            }
        }
        return phoneUsageModel;
    }

    public void updateAppUsage(String currentAppForeground, long timeCompletedThisUse) {

        PhoneUsage phoneUsageModel;
        boolean found = false;
        String currentPackageName = "";
        long getTimeDay = 0;
        long getTimeHour = 0;
        //TODO ещё одна проверка? если isLimit установлен?

        List<PhoneUsage> list = mAppsRepository.getPhoneUsageData();
        for (PhoneUsage phoneUsage : list) {
            Log.d(TAG, "updateAppUsage: " + phoneUsage.getPackageName() + "  in hour:  " + phoneUsage.getTimeCompletedInHour() + " in day: " + phoneUsage.getTimeCompletedInDay());
            currentPackageName = phoneUsage.getPackageName();
            if (currentPackageName.equals(currentAppForeground)) {
                found = true;
                getTimeDay = phoneUsage.getTimeCompletedInDay();
                getTimeHour = phoneUsage.getTimeCompletedInHour();
            }
            if (!found) {
                phoneUsageModel = new PhoneUsage(currentAppForeground, getTimeHour, getTimeDay);
                mAppsRepository.insertPhoneUsage(phoneUsageModel);
            } else {
                long timeCompletedThisHour = getTimeHour;
                timeCompletedThisHour += timeCompletedThisUse;
                long timeCompletedThisDay = getTimeDay;
                timeCompletedThisDay += timeCompletedThisUse;
                phoneUsageModel = new PhoneUsage(currentAppForeground, timeCompletedThisHour, timeCompletedThisDay);
                mAppsRepository.insertPhoneUsage(phoneUsageModel);
            }
        }
    }

    public void updateCurrentAppStats(String currentAppForeground) {
        long currentTimeForeground = System.currentTimeMillis() - currentTime;
        if (!prevApp.equals(currentAppForeground)) {
            if (!prevApp.equals(noAPP)) {
                updateAppUsage(prevApp, currentTimeForeground);
            }
            prevApp = currentAppForeground;
            currentTime = System.currentTimeMillis();
        } else if(currentTimeForeground >= 60000) {
            updateAppUsage(prevApp, currentTimeForeground);
            currentTime = System.currentTimeMillis();
        }
    }
}
