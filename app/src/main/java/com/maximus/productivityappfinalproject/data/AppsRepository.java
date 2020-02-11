package com.maximus.productivityappfinalproject.data;

import android.app.Application;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.maximus.productivityappfinalproject.IntervalEnum;
import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.Utils;
import com.maximus.productivityappfinalproject.data.db.AppsDatabase;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppsRepository {

    private final Context mContext;
    private MutableLiveData<List<AppsModel>> mAllApps = new MutableLiveData<>();
    private MutableLiveData<List<AppsModel>> mAppsInterval = new MutableLiveData<>();
    private MutableLiveData<List<IgnoreItems>> mIgnoreItems = new MutableLiveData<>();
    private final PackageManager mPackageManager;
    private UsageStatsManager mUsageStatsManager;
    private ExecutorService mExecutorService;
    Calendar mCalendar;
    private AppsDatabase mAppsDatabase;
    private static final String TAG = "AppsRepository";


    public AppsRepository(Application application) {
        mContext = application.getApplicationContext();
        mPackageManager = mContext.getPackageManager();
        mCalendar = Calendar.getInstance();
        mAppsDatabase = AppsDatabase.getInstance(application);
        mExecutorService = Executors.newFixedThreadPool(3);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            mUsageStatsManager = (UsageStatsManager) mContext.getSystemService(Context.USAGE_STATS_SERVICE);
        }
    }


    public LiveData<List<AppsModel>> getUsedInterval(String packageName, int sort) {
        List<AppsModel> appsModelList = new ArrayList<>();
        int mCount = 0;
        //TODO
        long[] range = Utils.getInterval(IntervalEnum.getInterval(sort));
        UsageEvents events = mUsageStatsManager.queryEvents(range[0], range[1]);
        UsageEvents.Event event = new UsageEvents.Event();
        AppsModel appsModel;
        MyEvent myEvent = null;
        long start = 0;
        long usageTime;

        while (events.hasNextEvent()) {
            events.getNextEvent(event);
            String currectPackageName = event.getPackageName();
            int eventType = event.getEventType();
            long eventTime = event.getTimeStamp();
            Log.d("||||------>", currectPackageName + " " + packageName + " " + new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date(eventTime)) + " " + eventType);

            if (currectPackageName.equals(packageName)) {
                Log.d("||||||||||>", currectPackageName + " " + packageName + " " + new SimpleDateFormat(
                        "yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(new Date(eventTime)) + " " + eventType);
                if (eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                    Log.d("********", "start " + start);
                    if (start == 0) {
                        start = eventTime;
                        appsModel = new AppsModel (0,eventTime, eventType);
                        appsModelList.add(appsModel);
                    }
                } else if (eventType == UsageEvents.Event.ACTIVITY_PAUSED) {
                    if (start > 0) {
                        myEvent = new MyEvent(event);
                    }
                    Log.d("********", "add end " + start);
                }
            } else {
                if (myEvent != null && start > 0) {
                    usageTime = myEvent.mTimeStamp - start;
                    appsModel = new AppsModel(usageTime,myEvent.mTimeStamp, myEvent.mEventType);
//                    appsModel.setAppUsageTime(usageTime);
                    if (appsModel.getAppUsageTime() <= 0) {
                        appsModel.setAppUsageTime(usageTime);
                    }
                    Log.d(TAG, "getUsedInterval: " + appsModel.getAppUsageTime());
                    if (appsModel.getAppUsageTime() >= 4000) {
                       appsModel.setCount(mCount++);
                    }
                    appsModelList.add(appsModel);
                    start = 0;
                    myEvent = null;
                }
            }
        }
        mAppsInterval.setValue(appsModelList);
        return mAppsInterval;
    }



    public LiveData<List<AppsModel>> getAllApps(boolean isSystem, int sort) {
         List<AppsModel> mAppsModelList = new ArrayList<>();
        mCalendar.set(Calendar.WEEK_OF_MONTH, 1);
        long[] range = Utils.getInterval(IntervalEnum.getInterval(sort));
        long startMills = mCalendar.getTimeInMillis();
        mExecutorService.execute(() -> {
            for (String packageName : getInstalledPackages(isSystem)) {
                //TODO when appUsageTime!!! NOT ALL TIME
                AppsModel appsModel =
                        new AppsModel(packageName ,getAppName(packageName), getAppIcon(packageName),
                                getLastTimeUsed(startMills, System.currentTimeMillis(), packageName),

                                fetchAppStatsInfo(range[0], range[1], packageName));
                       /*
                     Если время использования приложения 0с
                  то оно не будет отображаться в списке
                      */
                if (appsModel.getAppUsageTime() <= 0) {
                    continue;
                }
                if (isIgnoredList(getIgnoreItems(), appsModel.getPackageName())) {
                    continue;
                }
                Log.d(TAG, "getAllApps: " + getLastTimeUsed(startMills, System.currentTimeMillis(), packageName));
                mAppsModelList.add(appsModel);
                mAllApps.postValue(mAppsModelList);


                Collections.sort(mAppsModelList, (o1, o2) ->
                        (int) (o2.getAppUsageTime() - o1.getAppUsageTime()));
            }

        });

        return mAllApps;
    }

    public List<IgnoreItems> getIgnoreItems() {
        return mAppsDatabase.ignoreDao().getIgnoreItems();
    }

    public LiveData<List<IgnoreItems>> getAllIgnoreItems() {
        AppsDatabase.datatbaseWriterExecutor.execute(() -> {
            mIgnoreItems.postValue(mAppsDatabase.ignoreDao().getIgnoreItems());
        });

        return mIgnoreItems;
    }

    public void insert(IgnoreItems appsModel) {
        AppsDatabase.datatbaseWriterExecutor.execute(() -> {
            mAppsDatabase.ignoreDao().insertAppItem(appsModel);
        });

    }


    private List<String> getInstalledPackages(boolean isSystem) {
        List<String> packageName = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = mPackageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            if (isSystem == isSystemPackage(resolveInfo)) {
            }
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            packageName.add(activityInfo.applicationInfo.packageName);

        }
        return packageName;
    }


    private String getAppName(String packageName) {
        ApplicationInfo applicationInfo;

        try {
            applicationInfo = mPackageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            applicationInfo = null;
        }
        return (String) (applicationInfo != null ? mPackageManager.getApplicationLabel(applicationInfo) : packageName);
    }

    private Drawable getAppIcon(String packageName) {
        Drawable drawable;
        try {
            drawable = mPackageManager.getApplicationIcon(packageName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_launcher_round);
        }
        return drawable;
    }

    private boolean isSystemPackage(ResolveInfo resolveInfo) {
        return ((resolveInfo.activityInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }


    private long fetchAppStatsInfo(long startMillis, long endMillis, String appPkg) {
        Map<String, UsageStats> usageStatsMap = mUsageStatsManager
                .queryAndAggregateUsageStats(startMillis, endMillis);
        long total = 0L;
        if (usageStatsMap.containsKey(appPkg)) {
            total = usageStatsMap.get(appPkg).getTotalTimeInForeground();

        }
        return total;
    }

    private String getLastTimeUsed(long startMillis, long endMillis, String appPkg) {
        Map<String, UsageStats> usageStatsMap = mUsageStatsManager
                .queryAndAggregateUsageStats(startMillis, endMillis);
        long total = 0L;
        Date date = null;
        String result = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMMM dd, HH:mm", Locale.getDefault());
        if (usageStatsMap.containsKey(appPkg)) {
            total = usageStatsMap.get(appPkg).getLastTimeUsed();
            date = new Date(total);
            result = dateFormat.format(date);
        }
        return result;
    }

    public void sortEventByRange(int range) {
        long appDuration = 0;
        List<AppsModel> modelList = new ArrayList<>();
        UsageEvents events = mUsageStatsManager.queryEvents(Utils.getYesterday()[0], Utils.getYesterday()[1]); // range[0], range[1]
        UsageEvents.Event usageEvents = new UsageEvents.Event();
        String lastPackage ="";
        Map<String, Long> startEventPoint = new HashMap<>();
        Map<String, MyEvent> endEventPoint = new HashMap<>();
        while (events.hasNextEvent()) {
            events.getNextEvent(usageEvents);
            int eventType = usageEvents.getEventType();
            long eventTime = usageEvents.getTimeStamp();
            String eventPackage = usageEvents.getPackageName();
            if (eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
//               getAppName()
                AppsModel appsModel = equalsPackages(modelList, eventPackage);
                if (appsModel == null) {
                    appsModel = new AppsModel(eventPackage);
                    modelList.add(appsModel);
                }
                if (!startEventPoint.containsKey(eventPackage)) {
                    startEventPoint.put(eventPackage, eventTime);
                    for (Long value : startEventPoint.values()) {
                        Log.d(TAG, "start point: " + Utils.formatMillisToSeconds(value));
                    }
                }
            }
            if (eventType == UsageEvents.Event.ACTIVITY_PAUSED) {
                if (startEventPoint.size() > 0 && startEventPoint.containsKey(eventPackage)) {
                    endEventPoint.put(eventPackage, new MyEvent(usageEvents));
                    for (MyEvent value : endEventPoint.values()) {
                        Log.d(TAG, "end point: " + Utils.formatMillisToSeconds(value.mTimeStamp));
                    }
                }
            }

            if (!lastPackage.equals(eventPackage)) {
                if (startEventPoint.containsKey(lastPackage) && endEventPoint.containsKey(lastPackage)) {
                    MyEvent lastEvent = endEventPoint.get(lastPackage);
                    AppsModel appsModel = equalsPackages(modelList, lastPackage);
                    if (modelList != null) {
                        long duration = lastEvent.mTimeStamp - startEventPoint.get(lastPackage);
                        if (duration <= 0) duration = 0;
                        appDuration += duration;
                        appsModel = new AppsModel(lastEvent.mTimeStamp, appDuration);
                        if (duration > 5000) {
//                            mCount++;
                        }
                    }
                    startEventPoint.remove(lastPackage);
                    endEventPoint.remove(lastPackage);
                }
                lastPackage = eventPackage;

            }
        }
//        modelList.add(appsModel);
    }

    private boolean isIgnoredList(List<IgnoreItems> itemsList, String packageName) {
        for (IgnoreItems ignoreItems : itemsList) {
            if (ignoreItems.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public void deleteAllIgnoreItems() {
        AppsDatabase.datatbaseWriterExecutor.execute(() ->
                mAppsDatabase.ignoreDao().deleteAllItems());
    }

    private AppsModel equalsPackages(List<AppsModel> list, String packageName) {
        for (AppsModel appsModel : list) {
            if (appsModel.getPackageName().equals(packageName)) {
                return appsModel;
            }

        }
        return null;
    }

    class MyEvent {
         String mPackageName;
         String mEventClassName;
         long mTimeStamp;
         int mEventType;

        public MyEvent(UsageEvents.Event events) {
            mPackageName = events.getPackageName();
            mEventClassName = events.getClassName();
            mTimeStamp = events.getTimeStamp();
            mEventType = events.getEventType();
        }
    }



}

