package com.maximus.productivityappfinalproject.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.maximus.productivityappfinalproject.data.AppsRepository;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;

import java.util.List;

public class StatisticsViewModel extends AndroidViewModel {


    private AppsRepository mAppsRepository;
    private LiveData<List<AppsModel>> mAllApps;
    public StatisticsViewModel(@NonNull Application application) {
        super(application);
        mAppsRepository = new AppsRepository(application);
        mAllApps = mAppsRepository.getAllApps(true, 0);
    }

    public LiveData<List<AppsModel>> getList() {
        return mAllApps;
    }
}
