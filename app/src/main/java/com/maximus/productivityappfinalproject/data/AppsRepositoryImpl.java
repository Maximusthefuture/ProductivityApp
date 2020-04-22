package com.maximus.productivityappfinalproject.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.maximus.productivityappfinalproject.data.prefs.SharedPrefManager;
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


//TODO:
// All todo's from presentation,
//todo viewModelFactory?
//todo dagger2?
// todo foreground service?
// todo look how much battery eat app
// TODO: customview переделать доделать, random colors
// TODO: rx java?
//todo is service recreated when app relaunch? check
//todo cache?
//todo tests? unit, ui
//todo add bottom sheets like in yandex map?
// settings? bottom navigation? like in yandex map?
// save statistic to server? and share?
// change UI
// onBoarding write about permission and why u need that
// reminder if no one app isLimited
//TODO Separate repository by logic, db, sharedpref, api
//todo pattern builder????
public class AppsRepositoryImpl implements AppsRepository, ApiRepository {

    private MutableLiveData<List<IgnoreItems>> mIgnoreItems = new MutableLiveData<>();
    private MutableLiveData<Integer> mPhoneUsage = new MutableLiveData<>();
    private IgnoreAppDataSource mIgnoreAppDataSource;
    private PhoneUsageDataSource mPhoneUsageDataSource;
    private AppLimitDataSource mAppLimitDataSource;
    private SharedPrefManager mSharedPrefManager;


    public AppsRepositoryImpl(IgnoreAppDataSource ignoreAppDataSource) {
        mIgnoreAppDataSource = ignoreAppDataSource;

    }

    public AppsRepositoryImpl(PhoneUsageDataSource phoneUsageDataSource) {
        mPhoneUsageDataSource = phoneUsageDataSource;
    }

//    @Inject
    public AppsRepositoryImpl(AppLimitDataSource appLimitDataSource) {
        mAppLimitDataSource = appLimitDataSource;
    }

    public AppsRepositoryImpl(IgnoreAppDataSource ignoreAppDataSource, PhoneUsageDataSource phoneUsageDataSource) {
        mIgnoreAppDataSource = ignoreAppDataSource;
        mPhoneUsageDataSource = phoneUsageDataSource;
    }

    public AppsRepositoryImpl(PhoneUsageDataSource phoneUsageDataSource, AppLimitDataSource appLimitDataSource, SharedPrefManager prefManager) {
        mPhoneUsageDataSource = phoneUsageDataSource;
        mAppLimitDataSource = appLimitDataSource;
        mSharedPrefManager = prefManager;
    }

    public AppsRepositoryImpl(IgnoreAppDataSource ignoreAppDataSource, PhoneUsageDataSource phoneUsageDataSource, AppLimitDataSource appLimitDataSource, SharedPrefManager sharedPrefManager) {
        mIgnoreAppDataSource = ignoreAppDataSource;
        mPhoneUsageDataSource = phoneUsageDataSource;
        mAppLimitDataSource = appLimitDataSource;
        mSharedPrefManager = sharedPrefManager;
    }

    public AppsRepositoryImpl(IgnoreAppDataSource ignoreAppDataSource, AppLimitDataSource appLimitDataSource) {
        mIgnoreAppDataSource = ignoreAppDataSource;
        mAppLimitDataSource = appLimitDataSource;
    }

    public AppsRepositoryImpl(PhoneUsageDataSource phoneUsageDataSource, AppLimitDataSource appLimitDataSource) {
        mPhoneUsageDataSource = phoneUsageDataSource;
        mAppLimitDataSource = appLimitDataSource;
    }

    @Override
    public LiveData<List<IgnoreItems>> getIgnoreItems() {
        AppsDatabase.databaseWriterExecutor.execute(() -> {
            mIgnoreItems.postValue(mIgnoreAppDataSource.getAll());
        });
        return mIgnoreItems;
    }



    public Observable<List<IgnoreItems>> getIgnoreList() {
        return Observable.create(emitter -> {
            emitter.onNext(mIgnoreAppDataSource.getAll());
            emitter.onComplete();
        });
    }

    public Observable<List<AppUsageLimitModel>> getLimited() {
        return Observable.create(emitter -> {
            emitter.onNext(mAppLimitDataSource.getLimitedApps());
            emitter.onComplete();
        });
    }

    @Override
    public void insertToIgnoreList(IgnoreItems item) {
        AppsDatabase.databaseWriterExecutor.execute(() ->
                mIgnoreAppDataSource.add(item));
    }

    @Override
    public void deleteFromIgnoreList(String packageName) {
        AppsDatabase.databaseWriterExecutor.execute(() -> {
            mIgnoreAppDataSource.removeItem(packageName);
        });
    }

    @Override
    public void deleteAllIgnoreList() {
        AppsDatabase.databaseWriterExecutor.execute(() -> {
            mIgnoreAppDataSource.removeAll();
        });
    }

    @Override
    public LiveData<Integer> getUsageCount(int usageCount) {
        AppsDatabase.databaseWriterExecutor.execute(() -> {
            mPhoneUsage.postValue(mPhoneUsageDataSource.getUsageCount(usageCount));
        });
        return mPhoneUsage;
    }

    @Override
    public void insertPhoneUsage(PhoneUsage phoneUsage) {
        AppsDatabase.databaseWriterExecutor.execute(() -> {
            mPhoneUsageDataSource.insertPhoneUsage(phoneUsage);
        });
    }

    @Override
    public List<PhoneUsage> getPhoneUsageData() {
        return mPhoneUsageDataSource.getPhoneUsageData();
    }

    public Observable<List<PhoneUsage>> getPhoneUsageAsync() {
        return Observable.create(emitter -> {
            emitter.onNext(mPhoneUsageDataSource.getPhoneUsageData());
            emitter.onComplete();
//            emitter.onError(new Throwable("Can't get PhoneUsageData"));//????
        });
    }


    @Override
    public void updatePhoneUsage(PhoneUsage phoneUsage) {
        AppsDatabase.databaseWriterExecutor.execute(() -> {
            mPhoneUsageDataSource.updatePhoneUsage(phoneUsage);
        });
    }

    @Override
    public LiveData<Integer> getUsageCount() {
        AppsDatabase.databaseWriterExecutor.execute(() -> {
            mPhoneUsage.postValue(mPhoneUsageDataSource.getUsageCount());

        });
        return mPhoneUsage;
    }

    @Override
    public void resetHourly() {
        mPhoneUsageDataSource.resetHourly();
    }

    @Override
    public void resetDaily() {
        mPhoneUsageDataSource.resetDaily();
    }

    @Override
    public void deletePhoneUsage() {
        mPhoneUsageDataSource.removePhoneUsage();
    }

    @Override
    public List<AppUsageLimitModel> getLimitedItems() {
        return mAppLimitDataSource.getLimitedApps();
    }

    public Observable<List<AppUsageLimitModel>> getLimitObservable() {
        return Observable.create(emmiter-> {
            emmiter.onNext(mAppLimitDataSource.getLimitedApps());
            emmiter.onComplete();
        });
    }

    @Override
    public void addToLimit(AppUsageLimitModel app) {
        AppsDatabase.databaseWriterExecutor.execute(() -> {
            mAppLimitDataSource.addToLimit(app);
        });
    }

    @Override
    public void removeLimitedApp(String packageName) {
        mAppLimitDataSource.removeLimitedApp(packageName);
    }

    @Override
    public Long getClosestHour() {
        return mSharedPrefManager.getClosestHour();
    }

    @Override
    public void setClosestHour(long hour) {
        mSharedPrefManager.setClosestHour(hour);
    }

    @Override
    public Long getClosestDay() {
        return mSharedPrefManager.getClosestDay();
    }

    @Override
    public void setClosestDay(long day) {
        mSharedPrefManager.setClosestDay(day);
    }

    //TODO RETROFIT
    @Override
    public void getPhraseFromApi(String phrase) {

    }
}

