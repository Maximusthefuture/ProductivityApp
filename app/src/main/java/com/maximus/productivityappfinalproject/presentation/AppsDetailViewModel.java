package com.maximus.productivityappfinalproject.presentation;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.maximus.productivityappfinalproject.data.IgnoreAppDataSource;
import com.maximus.productivityappfinalproject.utils.IntervalEnum;
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.domain.GetAppIntervalUseCase;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.framework.IgnoreAppDataSourceImp;
import com.maximus.productivityappfinalproject.service.NotificationService;

import java.util.List;

public class AppsDetailViewModel extends AndroidViewModel {

    private final MutableLiveData<AppsModel> mApp = new MutableLiveData<>();
    private final AppsRepositoryImpl mAppsRepositoryImpl;
    private MutableLiveData<String> mLiveDataPackageName = new MutableLiveData<>();
    private MutableLiveData<Integer> mDayInterval = new MutableLiveData<>();
    private IntervalEnum mIntervalEnum = IntervalEnum.TODAY;
    private IgnoreAppDataSource mIgnoreAppDataSourceImp;
    private MyUsageStatsManagerWrapper mMyUsageStatsManagerWrapper;
    private Context mContext;
    private GetAppIntervalUseCase mAppIntervalUseCase;


    public AppsDetailViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mIgnoreAppDataSourceImp = new IgnoreAppDataSourceImp(mContext);
        mAppsRepositoryImpl = new AppsRepositoryImpl(mIgnoreAppDataSourceImp);
        mMyUsageStatsManagerWrapper = new MyUsageStatsManagerWrapper(mContext);
        mAppIntervalUseCase = new GetAppIntervalUseCase(mMyUsageStatsManagerWrapper);
        setFiltering(mIntervalEnum);
        mDayInterval.setValue(0);
    }


    public LiveData<List<AppsModel>> intervalList(String packageName) {
        mLiveDataPackageName.setValue(packageName);
        return mAppIntervalUseCase.getAppUsedInterval(mLiveDataPackageName.getValue(), mDayInterval.getValue());
    }

    public void startService() {
        Intent intent = new Intent(getApplication().getApplicationContext(), NotificationService.class);
        getApplication().getApplicationContext().startService(intent);
    }

    public void setFiltering(IntervalEnum intervalEnum) {
        mIntervalEnum = intervalEnum;
        switch (intervalEnum) {
            case TODAY:
                mDayInterval.setValue(0);
                break;
            case YESTERDAY:
                mDayInterval.setValue(1);
                break;
            case THIS_WEEK:
                mDayInterval.setValue(2);


        }
    }
}
