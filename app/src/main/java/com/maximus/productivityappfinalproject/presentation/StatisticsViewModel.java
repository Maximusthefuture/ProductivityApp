package com.maximus.productivityappfinalproject.presentation;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.data.IgnoreAppDataSource;
import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.domain.GetAppsUseCase;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.framework.IgnoreAppDataSourceImp;

import java.util.List;

public class StatisticsViewModel extends AndroidViewModel {


    private AppsRepositoryImpl mAppsRepositoryImpl;
    private LiveData<List<AppsModel>> mAllApps;
    private Context mContext;
    private GetAppsUseCase mGetAppsUseCase;
    private MyUsageStatsManagerWrapper mMyUsageStatsManagerWrapper;

    private IgnoreAppDataSource mIgnoreAppDataSourceImp;
    public StatisticsViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mIgnoreAppDataSourceImp = new IgnoreAppDataSourceImp(mContext);
        mAppsRepositoryImpl = new AppsRepositoryImpl(mIgnoreAppDataSourceImp);
        mMyUsageStatsManagerWrapper = new MyUsageStatsManagerWrapper(mContext);
        mGetAppsUseCase = new GetAppsUseCase(mMyUsageStatsManagerWrapper);
        mAllApps = mGetAppsUseCase.getAllApps(true, 0);
    }

    public LiveData<List<AppsModel>> getList() {
        return mAllApps;
    }
}
