package com.maximus.productivityappfinalproject.presentation;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.maximus.productivityappfinalproject.IntervalEnum;
import com.maximus.productivityappfinalproject.data.AppsRepository;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;

import java.util.List;

public class AppsDetailViewModel extends AndroidViewModel {

    private final MutableLiveData<AppsModel> mApp = new MutableLiveData<>();
    private final AppsRepository mAppsRepository;
    private MutableLiveData<String> mLiveDataPackageName = new MutableLiveData<>();
    private MutableLiveData<Integer> mDayInterval = new MutableLiveData<>();
    private IntervalEnum mIntervalEnum = IntervalEnum.TODAY;


    public AppsDetailViewModel(@NonNull Application application) {
        super(application);
//        mContext = application.getApplicationContext();
        mAppsRepository = new AppsRepository(application);
        setFiltering(mIntervalEnum);
        mDayInterval.setValue(0);
    }


    public LiveData<List<AppsModel>> intervalList(String packageName) {
        mLiveDataPackageName.setValue(packageName);
        return mAppsRepository.getUsedInterval(mLiveDataPackageName.getValue(), mDayInterval.getValue());
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
