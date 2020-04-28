package com.maximus.productivityappfinalproject.domain;


import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;

import java.util.List;

import javax.inject.Inject;

public class GetAppIntervalUseCase {

    private MyUsageStatsManagerWrapper mMyUsageStats;

    @Inject
    public GetAppIntervalUseCase(MyUsageStatsManagerWrapper myUsageStats) {
        mMyUsageStats = myUsageStats;

    }

    public List<AppsModel> getAppUsedInterval(String packageName, int sort) {
        return mMyUsageStats.getUsedInterval(packageName, sort);
    }
}
