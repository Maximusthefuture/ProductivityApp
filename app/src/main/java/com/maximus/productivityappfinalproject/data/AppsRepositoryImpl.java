package com.maximus.productivityappfinalproject.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase;

import java.util.Calendar;
import java.util.List;


public class AppsRepositoryImpl implements AppsRepository{

    private static final String TAG = "AppsRepositoryImpl";
    Calendar mCalendar;

    private MutableLiveData<List<IgnoreItems>> mIgnoreItems = new MutableLiveData<>();
    private IgnoreAppDataSource mIgnoreAppDataSource;
    private AppsDatabase mAppsDatabase;


    public AppsRepositoryImpl(Application application, IgnoreAppDataSource ignoreAppDataSource) {

        mCalendar = Calendar.getInstance();
        mIgnoreAppDataSource = ignoreAppDataSource;
        mAppsDatabase = AppsDatabase.getInstance(application);
    }

    @Override
    public LiveData<List<IgnoreItems>> getIgnoreItems() {
//        List<IgnoreEntity> ignoreA = new ArrayList<>();
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            List<IgnoreItems> ignoreB = ignoreA.stream().map(objA -> {
//                IgnoreItems ignoreItems = new IgnoreItems();
//                ignoreItems.getPackageName();
//                ignoreItems.getName();
//                return ignoreItems;
//            }).collect(Collectors.toList());
//            Log.d(TAG, "getIgnoreItems: " + ignoreB);
//        }
//        return AppsDatabase.getInstance(mContext).ignoreDao().getIgnoreItems();
        AppsDatabase.datatbaseWriterExecutor.execute(() -> {
        mIgnoreItems.postValue(mIgnoreAppDataSource.getAll());
        });
        return mIgnoreItems;
    }

    @Override
    public void insertToIgnoreList(IgnoreItems item) {
        AppsDatabase.datatbaseWriterExecutor.execute(() -> {
            mIgnoreAppDataSource.add(item);
        });
    }

    @Override
    public void deleteFromIgnoreList(IgnoreItems item) {
        mIgnoreAppDataSource.removeItem(item);
    }

    @Override
    public void deleteAllIgnoreList() {
        AppsDatabase.datatbaseWriterExecutor.execute(() -> {
            mIgnoreAppDataSource.removeAll();
        });
    }


    public void insert(IgnoreItems appsModel) {
        AppsDatabase.datatbaseWriterExecutor.execute(() -> {
            mAppsDatabase.ignoreDao().insertIgnoreItem(appsModel);
        });
    }


//    private long fetchAppStatsInfo(long startMillis, long endMillis, String appPkg) {
//        Map<String, UsageStats> usageStatsMap = mUsageStatsManager
//                .queryAndAggregateUsageStats(startMillis, endMillis);
//        long total = 0L;
//        if (usageStatsMap.containsKey(appPkg)) {
//            total = usageStatsMap.get(appPkg).getTotalTimeInForeground();
//
//        }
//        return total;
//    }
//






//    public void sortEventByRange(int range) {
//        long appDuration = 0;
//        List<AppsModel> modelList = new ArrayList<>();
//        UsageEvents events = mUsageStatsManager.queryEvents(Utils.getYesterday()[0], Utils.getYesterday()[1]); // range[0], range[1]
//        UsageEvents.Event usageEvents = new UsageEvents.Event();
//        String lastPackage = "";
//        Map<String, Long> startEventPoint = new HashMap<>();
//        Map<String, MyEvent> endEventPoint = new HashMap<>();
//        while (events.hasNextEvent()) {
//            events.getNextEvent(usageEvents);
//            int eventType = usageEvents.getEventType();
//            long eventTime = usageEvents.getTimeStamp();
//            String eventPackage = usageEvents.getPackageName();
//            if (eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
////               getAppName()
//                AppsModel appsModel = equalsPackages(modelList, eventPackage);
//                if (appsModel == null) {
//                    appsModel = new AppsModel(eventPackage);
//                    modelList.add(appsModel);
//                }
//                if (!startEventPoint.containsKey(eventPackage)) {
//                    startEventPoint.put(eventPackage, eventTime);
//                    for (Long value : startEventPoint.values()) {
//                        Log.d(TAG, "start point: " + Utils.formatMillisToSeconds(value));
//                    }
//                }
//            }
//            if (eventType == UsageEvents.Event.ACTIVITY_PAUSED) {
//                if (startEventPoint.size() > 0 && startEventPoint.containsKey(eventPackage)) {
//                    endEventPoint.put(eventPackage, new MyEvent(usageEvents));
//                    for (MyEvent value : endEventPoint.values()) {
//                        Log.d(TAG, "end point: " + Utils.formatMillisToSeconds(value.mTimeStamp));
//                    }
//                }
//            }
//            if (!lastPackage.equals(eventPackage)) {
//                if (startEventPoint.containsKey(lastPackage) && endEventPoint.containsKey(lastPackage)) {
//                    MyEvent lastEvent = endEventPoint.get(lastPackage);
//                    AppsModel appsModel = equalsPackages(modelList, lastPackage);
//                    if (modelList != null) {
//                        long duration = lastEvent.mTimeStamp - startEventPoint.get(lastPackage);
//                        if (duration <= 0) duration = 0;
//                        appDuration += duration;
//                        appsModel = new AppsModel(lastEvent.mTimeStamp, appDuration);
//                        if (duration > 5000) {
////                            mCount++;
//                        }
//                    }
//                    startEventPoint.remove(lastPackage);
//                    endEventPoint.remove(lastPackage);
//                }
//                lastPackage = eventPackage;
//
//            }
//        }
////        modelList.add(appsModel);
//    }


    //    public void deleteAllIgnoreItems() {
//        AppsDatabase.datatbaseWriterExecutor.execute(() ->
//                mAppsDatabase.ignoreDao().deleteAllItems());
//    }

//    public void deleteAllIgnoreList() {
//        mIgnoreAppDataSource.removeAll();
//    }

//    public void deleteIgnoreItem(IgnoreItems item) {
//        mIgnoreAppDataSource.removeItem(item);
//    }


//TODO DELETE WHEN USELESS

//    public LiveData<List<AppsModel>> getAllApps(boolean isSystem, int sort) {
//         List<AppsModel> mAppsModelList = new ArrayList<>();
////        mCalendar.set(Calendar.WEEK_OF_MONTH, 1);
//
//        //ВСЯ СОРТИРОВКА В ИНТЕРАКТОРЕ
//        long[] range = Utils.getInterval(IntervalEnum.getInterval(sort));
////        long startMills = mCalendar.getTimeInMillis();
//        AppsDatabase.datatbaseWriterExecutor.execute(() -> {
//            for (String packageName : getInstalledPackages(isSystem)) {
//                //TODO when appUsageTime!!! NOT ALL TIME
//                AppsModel appsModel =
//                        new AppsModel(packageName ,getAppName(packageName), getAppIcon(packageName),
//                                getLastTimeUsed(range[0], System.currentTimeMillis(), packageName),
//                                fetchAppStatsInfo(range[0], range[1], packageName));
//                       /*
//                     Если время использования приложения 6с
//                  то оно не будет отображаться в списке
//                      */
//                if (appsModel.getAppUsageTime() <= 6000) {
//                    continue;
//                }
//
//                if (isIgnoredList(getIgnoreItems(), appsModel.getPackageName())) {
//                    continue;
//                }
////                Log.d(TAG, "getAllApps: " + getLastTimeUsed(startMills, System.currentTimeMillis(), packageName));
//                mAppsModelList.add(appsModel);
//                mAllApps.postValue(mAppsModelList);
//
//
//                Collections.sort(mAppsModelList, (o1, o2) ->
//                        (int) (o2.getAppUsageTime() - o1.getAppUsageTime()));
//            }
//
//        });
//
//        return mAllApps;
//    }


    //    private List<String> getInstalledPackages(boolean isSystem) {
//        List<String> packageName = new ArrayList<>();
//        Intent intent = new Intent(Intent.ACTION_MAIN, null);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        List<ResolveInfo> resolveInfoList = mPackageManager.queryIntentActivities(intent, 0);
//        for (ResolveInfo resolveInfo : resolveInfoList) {
//            isSystem = isSystemPackage(resolveInfo);
////            if (isSystem){
////                continue;
////            }
//            ActivityInfo activityInfo = resolveInfo.activityInfo;
//            packageName.add(activityInfo.applicationInfo.packageName);
//
//        }
//        return packageName;
//    }
//
//
//    private String getAppName(String packageName) {
//        ApplicationInfo applicationInfo;
//
//        try {
//            applicationInfo = mPackageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//            applicationInfo = null;
//        }
//        return (String) (applicationInfo != null ? mPackageManager.getApplicationLabel(applicationInfo) : packageName);
//    }

//    private Drawable getAppIcon(String packageName) {
//        Drawable drawable;
//        try {
//            drawable = mPackageManager.getApplicationIcon(packageName);
//
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//            drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_launcher_round);
//        }
//        return drawable;
//    }

//    private boolean isSystemPackage(ResolveInfo resolveInfo) {
//        return ((resolveInfo.activityInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
//    }


    //    private String getLastTimeUsed(long startMillis, long endMillis, String appPkg) {
//        Map<String, UsageStats> usageStatsMap = mUsageStatsManager
//                .queryAndAggregateUsageStats(startMillis, endMillis);
//        long total = 0L;
//        Date date = null;
//        String result = null;
//        SimpleDateFormat dateFormat = new SimpleDateFormat("EE MMMM dd, HH:mm", Locale.getDefault());
//        if (usageStatsMap.containsKey(appPkg)) {
//            total = usageStatsMap.get(appPkg).getLastTimeUsed();
//            date = new Date(total);
//            result = dateFormat.format(date);
//        }
//        return result;
//    }

}

