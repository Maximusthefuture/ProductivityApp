package com.maximus.productivityappfinalproject.data;

import androidx.lifecycle.MutableLiveData;

import com.maximus.productivityappfinalproject.data.prefs.SharedPrefManager;
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;
import com.maximus.productivityappfinalproject.domain.model.LimitedApps;
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;


//TODO:
// All todo's from presentation,
//todo viewModelFactory?
//todo dagger2?
// todo foreground service? DONE!
// todo look how much battery eat app
// TODO: customview переделать доделать, random colors
// TODO: rx java?
//todo is service recreated when app relaunch? check
//todo cache?
//todo tests? unit, ui
//todo add bottom sheets like in yandex map?
// settings? bottom navigation? like in yandex map?
// save statistic to server? and share? firebase?
// change UI
// onBoarding write about permission and why u need that
// reminder if no one app isLimited
//TODO Separate repository by logic, db, sharedpref, api
//todo pattern builder????
@Singleton
public class AppsRepositoryImpl implements AppsRepository, ApiRepository {

    private MutableLiveData<List<LimitedApps>> mIgnoreItems = new MutableLiveData<>();
    private MutableLiveData<Integer> mPhoneUsage = new MutableLiveData<>();
    private LimitedAppsDataSource mLimitedAppsDataSource;
    private PhoneUsageDataSource mPhoneUsageDataSource;
    private AppLimitDataSource mAppLimitDataSource;
    private SharedPrefManager mSharedPrefManager;


    public AppsRepositoryImpl(LimitedAppsDataSource limitedAppsDataSource) {
        mLimitedAppsDataSource = limitedAppsDataSource;

    }

    public AppsRepositoryImpl(PhoneUsageDataSource phoneUsageDataSource) {
        mPhoneUsageDataSource = phoneUsageDataSource;
    }


    public AppsRepositoryImpl(AppLimitDataSource appLimitDataSource) {
        mAppLimitDataSource = appLimitDataSource;
    }

    public AppsRepositoryImpl(LimitedAppsDataSource limitedAppsDataSource, PhoneUsageDataSource phoneUsageDataSource) {
        mLimitedAppsDataSource = limitedAppsDataSource;
        mPhoneUsageDataSource = phoneUsageDataSource;
    }


    public AppsRepositoryImpl(PhoneUsageDataSource phoneUsageDataSource, AppLimitDataSource appLimitDataSource, SharedPrefManager prefManager) {
        mPhoneUsageDataSource = phoneUsageDataSource;
        mAppLimitDataSource = appLimitDataSource;
        mSharedPrefManager = prefManager;
    }

    @Inject
    public AppsRepositoryImpl(LimitedAppsDataSource limitedAppsDataSource, PhoneUsageDataSource phoneUsageDataSource, AppLimitDataSource appLimitDataSource, SharedPrefManager sharedPrefManager) {
        mLimitedAppsDataSource = limitedAppsDataSource;
        mPhoneUsageDataSource = phoneUsageDataSource;
        mAppLimitDataSource = appLimitDataSource;
        mSharedPrefManager = sharedPrefManager;
    }

    public AppsRepositoryImpl(LimitedAppsDataSource limitedAppsDataSource, AppLimitDataSource appLimitDataSource) {
        mLimitedAppsDataSource = limitedAppsDataSource;
        mAppLimitDataSource = appLimitDataSource;
    }

    public AppsRepositoryImpl(PhoneUsageDataSource phoneUsageDataSource, AppLimitDataSource appLimitDataSource) {
        mPhoneUsageDataSource = phoneUsageDataSource;
        mAppLimitDataSource = appLimitDataSource;
    }

    @Override
    public Observable<List<LimitedApps>> getIgnoreItems() {
        return Observable.create(emitter -> {
            emitter.onNext(mLimitedAppsDataSource.getAll());
            emitter.onComplete();
        });
    }

    public Observable<List<AppUsageLimitModel>> getLimited() {
        return Observable.create(emitter -> {
            emitter.onNext(mAppLimitDataSource.getLimitedApps());
            emitter.onComplete();
        });
    }

    public Observable<List<PhoneUsage>> getPhoneUsage() {
        return Observable.create(emitter -> {
            emitter.onNext(mPhoneUsageDataSource.getPhoneUsageData());
            emitter.onComplete();
        });
    }

    @Override
    public void insertToIgnoreList(LimitedApps item) {
        AppsDatabase.databaseWriterExecutor.execute(() ->
                mLimitedAppsDataSource.add(item));
//        mLimitedAppsDataSource.add(item);
    }

    @Override
    public void deleteFromIgnoreList(String packageName) {
        AppsDatabase.databaseWriterExecutor.execute(() -> {
            mLimitedAppsDataSource.removeItem(packageName);
        });
    }

    @Override
    public void deleteAllIgnoreList() {
        AppsDatabase.databaseWriterExecutor.execute(() -> {
            mLimitedAppsDataSource.removeAll();
        });
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

    public Flowable<List<PhoneUsage>> getPhoneUsageFlowable() {
        return Flowable.create(emitter -> {
            emitter.onNext((mPhoneUsageDataSource.getPhoneUsageData()));
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public void updatePhoneUsage(PhoneUsage phoneUsage) {
        AppsDatabase.databaseWriterExecutor.execute(() -> {
            mPhoneUsageDataSource.updatePhoneUsage(phoneUsage);
        });
//        Observable.create(emitter -> {
//                emitter.onNext(mPhoneUsageDataSource.updatePhoneUsage(phoneUsage));
//            emitter.onComplete();
//        })
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
        return Observable.create(emmiter -> {
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

