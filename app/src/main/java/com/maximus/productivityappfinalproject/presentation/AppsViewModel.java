package com.maximus.productivityappfinalproject.presentation;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.maximus.productivityappfinalproject.IntervalEnum;
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.data.IgnoreAppDataSource;
import com.maximus.productivityappfinalproject.domain.AddIgnoreListUseCase;
import com.maximus.productivityappfinalproject.domain.GetAppsUseCase;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.framework.IgnoreAppDataSourceImp;

import java.util.List;

public class AppsViewModel extends AndroidViewModel {
    private AppsRepositoryImpl mRepository;
    private LiveData<List<AppsModel>> mAllApps;
    private Context mContext;
    private MutableLiveData<Integer> mDayInterval = new MutableLiveData<>();
    private IntervalEnum mIntervalEnum = IntervalEnum.TODAY;
    private GetAppsUseCase mAppsUseCase;
    private AddIgnoreListUseCase mIgnoreListUseCase;

    private IgnoreAppDataSource mIgnoreAppDataSourceImp;

    public AppsViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mAppsUseCase = new GetAppsUseCase(mContext);
        mRepository = new AppsRepositoryImpl(application, mIgnoreAppDataSourceImp);
        mAllApps = mAppsUseCase.getAllApps(false, 1);
        mIgnoreAppDataSourceImp = new IgnoreAppDataSourceImp(mContext);
        setFiltering(mIntervalEnum.TODAY);
        mIgnoreListUseCase = new AddIgnoreListUseCase(mRepository);
        mDayInterval.setValue(0);
    }

    public LiveData<List<AppsModel>> getAllApps() {
        return mAllApps;
    }

    public void insert(IgnoreItems appsModel) {
        mIgnoreListUseCase.addToIgnoreList(appsModel);
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

//    public void getApps(int id) {
//        mGetAppsUseCase.execute(id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::onSuccess, this::OnError);
//
//    }


}
