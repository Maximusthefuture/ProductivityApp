package com.maximus.productivityappfinalproject.domain;

import androidx.lifecycle.LiveData;

import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetAppsUseCase {

    private MyUsageStatsManagerWrapper mUsageStats;

    @Inject
    public GetAppsUseCase(MyUsageStatsManagerWrapper mMyUsageStats) {
        mUsageStats = mMyUsageStats;
    }

    public LiveData<List<AppsModel>> getAllApps(boolean isSystem, int sort) {
        return mUsageStats.getAllApps(isSystem, sort);
    }

    public Observable<List<AppsModel>> getAllAppsObservable(boolean isSystem, int sort) {
        return Observable.create(emitter -> {
            emitter.onNext(mUsageStats.getAllAppsObservable(isSystem, sort));
            emitter.onComplete();
        });
    }

}
