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
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;

import java.util.List;

public class AppsViewModel extends AndroidViewModel {
    private AppsRepository mRepository;
    private LiveData<List<AppsModel>> mAllApps;
    private Context mContext;
    private MutableLiveData<Integer> mDayInterval = new MutableLiveData<>();
    private IntervalEnum mIntervalEnum = IntervalEnum.TODAY;

    public AppsViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mRepository = new AppsRepository(application);
        mAllApps = mRepository.getAllApps(true, mDayInterval.getValue());
        setFiltering(mIntervalEnum.TODAY);
        mDayInterval.setValue(0);
    }

    public LiveData<List<AppsModel>> getAllApps() {
        return mAllApps;
    }

    public void insert(IgnoreItems appsModel) {
        mRepository.insert(appsModel);
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
