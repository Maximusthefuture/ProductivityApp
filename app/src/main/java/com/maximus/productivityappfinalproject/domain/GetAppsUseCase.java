package com.maximus.productivityappfinalproject.domain;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;

import java.util.List;

public class GetAppsUseCase {

    private MyUsageStatsManagerWrapper mUsageStats;

    public GetAppsUseCase(Context context) {
        mUsageStats = new MyUsageStatsManagerWrapper(context);
    }

    public LiveData<List<AppsModel>> getAllApps(boolean isSystem, int sort) {
        return mUsageStats.getAllApps(isSystem, sort);
    }

}