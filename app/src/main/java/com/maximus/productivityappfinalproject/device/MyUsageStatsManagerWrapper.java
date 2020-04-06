package com.maximus.productivityappfinalproject.device;

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

import com.maximus.productivityappfinalproject.data.IgnoreAppDataSource;
import com.maximus.productivityappfinalproject.framework.IgnoreAppDataSourceImp;
import com.maximus.productivityappfinalproject.utils.IntervalEnum;
import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase;
import com.maximus.productivityappfinalproject.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;

public class MyUsageStatsManagerWrapper {
    private static final String TAG = "MyUsageStatsManagerWrap";
    private MutableLiveData<List<AppsModel>> mAllApps = new MutableLiveData<>();
    private UsageStatsManager mUsageStatsManager;
    private final PackageManager mPackageManager;
    private Context mContext;
    private MutableLiveData<List<AppsModel>> mAppsInterval = new MutableLiveData<>();
    private IgnoreAppDataSource mIgnoreAppDataSource;


    public MyUsageStatsManagerWrapper(Context context) {
        mContext = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            mUsageStatsManager = (UsageStatsManager) mContext.getSystemService(Context.USAGE_STATS_SERVICE);
        }
        mPackageManager = mContext.getPackageManager();
        mIgnoreAppDataSource = new IgnoreAppDataSourceImp(mContext);
    }

    /**
     * Проверяем какое сейчас приложение в фоне,
     * c помощью @UsageEvents.Event.ACTIVITY_RESUMED
     */
    public String getForegroundApp() {

        String foregroundApp = null;

        long time = System.currentTimeMillis();
        long hour = 3600 * 1000;

        UsageEvents usageEvents = mUsageStatsManager.queryEvents(time - hour, time);
        UsageEvents.Event event = new UsageEvents.Event();


        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);
            if (event.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED) {
                foregroundApp = event.getPackageName();
            }
        }
        return foregroundApp;
    }


    /**
     * Получаем все приложения, и сортируем их
     *
     * @param isSystem Проверяет системное ли приложение
     * @param sort Сортируем по {@link IntervalEnum}
     * @return {@link AppsModel}
     */
    public LiveData<List<AppsModel>> getAllApps(boolean isSystem, int sort) {
        List<AppsModel> mAppsModelList = new ArrayList<>();

        long[] range = Utils.getInterval(IntervalEnum.getInterval(sort));
        AppsDatabase.datatbaseWriterExecutor.execute(() -> {
            for (String packageName : getInstalledPackages(isSystem)) {
                AppsModel appsModel =
                        new AppsModel(packageName ,getAppName(packageName), getAppIcon(packageName),
                                getLastTimeUsed(range[0], System.currentTimeMillis(), packageName),
                                fetchAppStatsInfo(range[0], range[1], packageName));
//                if (appsModel.getAppUsageTime() <= 0) {
//                    continue;
//                }
                if (isAppSelected(mAppsModelList)) {
                    continue;
                }
                //TODO check is the iteractor? usecase?
                if (isIgnoredList(mIgnoreAppDataSource.getAll(), appsModel.getPackageName())) {
                    continue;
                }
//                Log.d(TAG, "getAllApps: " + getLastTimeUsed(startMills, System.currentTimeMillis(), packageName));
                mAppsModelList.add(appsModel);
                mAllApps.postValue(mAppsModelList);

                Collections.sort(mAppsModelList, (o1, o2) ->
                        (int) (o2.getAppUsageTime() - o1.getAppUsageTime()));
            }
        });

        return mAllApps;
    }

    public List<AppsModel> getAllAppsObservable(boolean isSystem, int sort) {
        List<AppsModel> mAppsModelList = new ArrayList<>();

        long[] range = Utils.getInterval(IntervalEnum.getInterval(sort));
            for (String packageName : getInstalledPackages(isSystem)) {
                AppsModel appsModel =
                        new AppsModel(packageName ,getAppName(packageName), getAppIcon(packageName),
                                getLastTimeUsed(range[0], System.currentTimeMillis(), packageName),
                                fetchAppStatsInfo(range[0], range[1], packageName));

                if (isAppSelected(mAppsModelList)) {
                    continue;
                }
                //TODO check is the iteractor? usecase?
                if (isIgnoredList(mIgnoreAppDataSource.getAll(), appsModel.getPackageName())) {
                    continue;
                }
//                Log.d(TAG, "getAllApps: " + getLastTimeUsed(startMills, System.currentTimeMillis(), packageName));
                mAppsModelList.add(appsModel);
                Collections.sort(mAppsModelList, (o1, o2) ->
                        (int) (o2.getAppUsageTime() - o1.getAppUsageTime()));
            }

        return mAppsModelList;
    }

    public boolean isAppSelected(List<AppsModel> appsModels) {
        for (AppsModel appsModel : appsModels) {
            if (appsModel.isSelected()) {
                return true;
            }
        }
        return false;
    }

//    //UPDATE INTO DB
//    public IgnoreItems addIgnore(AppsModel info) {
//        String packageName = info.getPackageName();
//        String appName = info.getAppName();
//        long[] range = Utils.getInterval(IntervalEnum.getInterval(0));
//        String lastTimeUsed = getLastTimeUsed(range[0], System.currentTimeMillis(), packageName);
//        long usedTime = fetchAppStatsInfo(range[0], 0, packageName);
//
//        IgnoreItems ignoreItems = new IgnoreItems(packageName, appName, lastTimeUsed);
//        List<IgnoreItems> list = new ArrayList<>();
//        list.add(ignoreItems);
//        mIgnoreAppDataSource.update(ignoreItems);
//        return ignoreItems;
//    }

    //TODO UPDATE ONLY USEDTIME AND LASTTIMEUSED!!!
    public void refreshIgnoreList(IgnoreItems ignoreItems) {
        AppsDatabase.datatbaseWriterExecutor.execute(new Runnable() {
            @Override
            public void run() {
                long[] range = Utils.getInterval(IntervalEnum.getInterval(0));
                String lastTimeUsed = getLastTimeUsed(range[0], System.currentTimeMillis(), ignoreItems.getPackageName());
                long usedTime = fetchAppStatsInfo(range[0], range[1], ignoreItems.getPackageName());
                IgnoreItems items = new IgnoreItems(ignoreItems.getPackageName(),ignoreItems.getName(), lastTimeUsed, usedTime);
                List<IgnoreItems> list = new ArrayList<>();
                list.add(items);
                mIgnoreAppDataSource.update(items);
            }
        });
    }

    private boolean isIgnoredList(List<IgnoreItems> itemsList, String packageName) {
        for (IgnoreItems ignoreItems : itemsList) {
            if (ignoreItems.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    private AppsModel equalsPackages(List<AppsModel> list, String packageName) {
        for (AppsModel appsModel : list) {
            if (appsModel.getPackageName().equals(packageName)) {
                return appsModel;
            }
        }
        return null;
    }

    /**
     * Получаем интервал использования приложений
     *
     * @param packageName
     * @param sort
     * @return List of {@link AppsModel}
     */
    public LiveData<List<AppsModel>> getUsedInterval(String packageName, int sort) {
        List<AppsModel> appsModelList = new ArrayList<>();
        int mCount = 0;
        long[] range = Utils.getInterval(IntervalEnum.getInterval(sort));
        UsageEvents events = mUsageStatsManager.queryEvents(range[0], range[1]);
        UsageEvents.Event event = new UsageEvents.Event();
        AppsModel appsModel;
        MyEvent myEvent = null;
        long start = 0;
        long usageTime = 0;

        while (events.hasNextEvent()) {
            events.getNextEvent(event);
            String currectPackageName = event.getPackageName();
            int eventType = event.getEventType();
            long eventTime = event.getTimeStamp();
            if (currectPackageName.equals(packageName)) {
                if (eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                    if (start == 0) {
                        start = eventTime;
                        appsModel = new AppsModel(0, eventTime, eventType);
                        appsModelList.add(appsModel);
                    }
                } else if (eventType == UsageEvents.Event.ACTIVITY_PAUSED) {
                    if (start > 0) {
                        myEvent = new MyEvent(event);
                        myEvent.mEventType = -1;

                    }
                }
            } else {
                if (myEvent != null && start > 0) {
                    usageTime = myEvent.mTimeStamp - start;
                    if (usageTime <= 0) {
                        usageTime = 0;
                    }
                    appsModel = new AppsModel(usageTime, myEvent.mTimeStamp, myEvent.mEventType);
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


    /**
     * Получаем данные о приложении за указанный период времени
     *
     * @param startMillis время начала отсчета
     * @param endMillis время конца
     * @param appPkg Имя пакета
     * @return время использования в foreground
     */
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

    public Drawable getAppIcon(String packageName) {
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

    private List<String> getInstalledPackages(boolean isSystem) {
        List<String> packageName = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = mPackageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            packageName.add(activityInfo.applicationInfo.packageName);

        }
        return packageName;
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
