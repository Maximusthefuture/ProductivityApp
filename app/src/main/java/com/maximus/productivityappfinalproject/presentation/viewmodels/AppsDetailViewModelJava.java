package com.maximus.productivityappfinalproject.presentation.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.maximus.productivityappfinalproject.data.IgnoreAppDataSource;
import com.maximus.productivityappfinalproject.presentation.ui.AppsFragment;
import com.maximus.productivityappfinalproject.utils.IntervalEnum;
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.domain.GetAppIntervalUseCase;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.framework.IgnoreAppDataSourceImp;
import com.maximus.productivityappfinalproject.service.NotificationService;

import java.util.List;

public class AppsDetailViewModelJava extends AndroidViewModel {

    private final MutableLiveData<AppsModel> mApp = new MutableLiveData<>();
//    private final AppsRepositoryImpl mAppsRepositoryImpl;
    private MutableLiveData<Integer> mDayInterval = new MutableLiveData<>();
    private IntervalEnum mIntervalEnum = IntervalEnum.TODAY;
//    private IgnoreAppDataSource mIgnoreAppDataSourceImp;
//    private MyUsageStatsManagerWrapper mMyUsageStatsManagerWrapper;
    private Context mContext;
    private GetAppIntervalUseCase mAppIntervalUseCase;


    public AppsDetailViewModelJava(@NonNull Application application) {
        super(application);
        Bundle bundle = new Bundle();
        mApp.setValue(bundle.getParcelable(AppsFragment.APP_DETAILS));

//        mContext = application.getApplicationContext();
//        mIgnoreAppDataSourceImp = new IgnoreAppDataSourceImp(mContext);
//        mAppsRepositoryImpl = new AppsRepositoryImpl(mIgnoreAppDataSourceImp);
//        mMyUsageStatsManagerWrapper = new MyUsageStatsManagerWrapper(mContext, mIgnoreAppDataSourceImp);
//        mAppIntervalUseCase = new GetAppIntervalUseCase(mMyUsageStatsManagerWrapper);
        setFiltering(mIntervalEnum);
        mDayInterval.setValue(0);
    }

    public LiveData<Integer> getInterval(){
        return mDayInterval;
    }

//    public LiveData<List<AppsModel>> intervalList(String packageName) {
//        return mAppIntervalUseCase.getAppUsedInterval(packageName, mDayInterval.getValue());
//    }

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

    public LiveData<AppsModel> getAppsModel() {
        return mApp;
    }
}
