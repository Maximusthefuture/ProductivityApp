package com.maximus.productivityappfinalproject.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase;

import java.util.List;


public class AppsRepositoryImpl implements AppsRepository, ApiRepository {

    private static final String TAG = "AppsRepositoryImpl";


    private MutableLiveData<List<IgnoreItems>> mIgnoreItems = new MutableLiveData<>();
    private MutableLiveData<Integer> mPhoneUsage = new MutableLiveData<>();
    private IgnoreAppDataSource mIgnoreAppDataSource;
    private PhoneUsageDataSource mPhoneUsageDataSource;
    private AppLimitDataSource mAppLimitDataSource;


    public AppsRepositoryImpl(IgnoreAppDataSource ignoreAppDataSource) {
        mIgnoreAppDataSource = ignoreAppDataSource;

    }

    public AppsRepositoryImpl(PhoneUsageDataSource phoneUsageDataSource) {
        mPhoneUsageDataSource = phoneUsageDataSource;
    }

    public AppsRepositoryImpl(AppLimitDataSource appLimitDataSource) {
        mAppLimitDataSource = appLimitDataSource;
    }

    public AppsRepositoryImpl() {
    }

    public AppsRepositoryImpl(IgnoreAppDataSource ignoreAppDataSource, PhoneUsageDataSource phoneUsageDataSource) {
        mIgnoreAppDataSource = ignoreAppDataSource;
        mPhoneUsageDataSource = phoneUsageDataSource;
    }


    public AppsRepositoryImpl(PhoneUsageDataSource phoneUsageDataSource, AppLimitDataSource appLimitDataSource) {
        mPhoneUsageDataSource = phoneUsageDataSource;
        mAppLimitDataSource = appLimitDataSource;
    }

    @Override
    public LiveData<List<IgnoreItems>> getIgnoreItems() {
        AppsDatabase.datatbaseWriterExecutor.execute(() -> {
            mIgnoreItems.postValue(mIgnoreAppDataSource.getAll());
        });
        return mIgnoreItems;

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
    }

    @Override
    public void insertToIgnoreList(IgnoreItems item) {
        AppsDatabase.datatbaseWriterExecutor.execute(() ->
                mIgnoreAppDataSource.add(item));
    }

    public void getMyLimitedItems() {
        AppsDatabase.datatbaseWriterExecutor.execute(() -> {
            List<AppUsageLimitModel> imy = mAppLimitDataSource.getLimitedApps();
            for (AppUsageLimitModel appUsageLimitModel : imy) {
                Log.d(TAG, "getMyLimitedItems: " +
                        appUsageLimitModel.getPackageName() + " perHourLimit " +
                        appUsageLimitModel.getTimeLimitPerHour() + " perDayLimit " +
                        appUsageLimitModel.getTimeLimitPerDay() + "isLimited " + appUsageLimitModel.isAppLimited());
            }
        });
    }

    @Override
    public void deleteFromIgnoreList(String packageName) {
        AppsDatabase.datatbaseWriterExecutor.execute(() -> {
            mIgnoreAppDataSource.removeItem(packageName);
        });
    }

    @Override
    public void deleteAllIgnoreList() {
        AppsDatabase.datatbaseWriterExecutor.execute(() -> {
            mIgnoreAppDataSource.removeAll();
        });
    }

    @Override
    public LiveData<Integer> getUsageCount(int usageCount) {
        AppsDatabase.datatbaseWriterExecutor.execute(() -> {
            mPhoneUsage.postValue(mPhoneUsageDataSource.getUsageCount(usageCount));
        });
        return mPhoneUsage;
    }

    @Override
    public void insertPhoneUsage(PhoneUsage phoneUsage) {
        AppsDatabase.datatbaseWriterExecutor.execute(() -> {
            mPhoneUsageDataSource.insertPhoneUsage(phoneUsage);
        });
    }

    @Override
    public List<PhoneUsage> getPhoneUsageData() {
       return mPhoneUsageDataSource.getPhoneUsageData();
    }


    @Override
    public void updatePhoneUsage(PhoneUsage phoneUsage) {
        AppsDatabase.datatbaseWriterExecutor.execute(() -> {
            mPhoneUsageDataSource.updatePhoneUsage(phoneUsage);
        });
    }

    @Override
    public LiveData<Integer> getUsageCount() {
        AppsDatabase.datatbaseWriterExecutor.execute(() -> {
            mPhoneUsage.postValue(mPhoneUsageDataSource.getUsageCount());

        });
        return mPhoneUsage;
    }

    @Override
    public void deletePhoneUsage() {
        mPhoneUsageDataSource.removePhoneUsage();
    }

    @Override
    public List<AppUsageLimitModel> getLimitedItems() {
        return mAppLimitDataSource.getLimitedApps();
    }

    @Override
    public void addToLimit(AppUsageLimitModel app) {
        AppsDatabase.datatbaseWriterExecutor.execute(() -> {
            mAppLimitDataSource.addToLimit(app);
        });
    }

    @Override
    public void removeLimitedApp(String packageName) {
        mAppLimitDataSource.removeLimitedApp(packageName);
    }


    //TODO RETROFIT
    @Override
    public void getPhraseFromApi(String phrase) {

    }
}

