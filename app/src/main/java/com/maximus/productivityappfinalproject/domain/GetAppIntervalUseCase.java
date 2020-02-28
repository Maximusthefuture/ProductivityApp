package com.maximus.productivityappfinalproject.domain;


import androidx.lifecycle.LiveData;

import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;

import java.util.List;

public class GetAppIntervalUseCase {

    private MyUsageStatsManagerWrapper mMyUsageStats;

    public GetAppIntervalUseCase(MyUsageStatsManagerWrapper myUsageStats) {
        mMyUsageStats = myUsageStats;

    }

    public LiveData<List<AppsModel>> getAppUsedInterval(String packageName, int sort) {
        return mMyUsageStats.getUsedInterval(packageName, sort);
    }
}
