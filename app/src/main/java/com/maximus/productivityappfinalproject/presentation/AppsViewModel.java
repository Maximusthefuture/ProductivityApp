package com.maximus.productivityappfinalproject.presentation;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;

import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.data.PhoneUsageDataSource;
import com.maximus.productivityappfinalproject.domain.GetPhoneUsageCountUseCase;
import com.maximus.productivityappfinalproject.framework.PhoneUsageDataSourceImp;
import com.maximus.productivityappfinalproject.service.NotificationService;
import com.maximus.productivityappfinalproject.utils.IntervalEnum;
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.data.IgnoreAppDataSource;
import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.device.PhoneUsageNotificationManager;
import com.maximus.productivityappfinalproject.domain.AddIgnoreListUseCase;
import com.maximus.productivityappfinalproject.domain.GetAppsUseCase;
import com.maximus.productivityappfinalproject.domain.ShowAppUsageUseCase;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.framework.IgnoreAppDataSourceImp;
import com.maximus.productivityappfinalproject.utils.MyPreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class AppsViewModel extends AndroidViewModel {
    private static final String TAG = "AppsViewModel";
    private AppsRepositoryImpl mRepository;
    private LiveData<List<AppsModel>> mAllApps;
    private Context mContext;
    private MutableLiveData<Integer> mDayInterval = new MutableLiveData<>();
    private IntervalEnum mIntervalEnum = IntervalEnum.TODAY;
    private GetAppsUseCase mAppsUseCase;
    private AddIgnoreListUseCase mIgnoreListUseCase;
    private MyUsageStatsManagerWrapper mMyUsageStatsManagerWrapper;
    private ShowAppUsageUseCase mShowAppUsageUseCase;
    private PhoneUsageNotificationManager mManager;
    private IgnoreAppDataSource mIgnoreAppDataSource;
    private GetPhoneUsageCountUseCase mUsageCountUseCase;
    private PhoneUsageDataSource mDataSource;

    public AppsViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mDataSource = new PhoneUsageDataSourceImp(mContext);
        mMyUsageStatsManagerWrapper = new MyUsageStatsManagerWrapper(mContext);
        mIgnoreAppDataSource = new IgnoreAppDataSourceImp(mContext);
        mRepository = new AppsRepositoryImpl(mIgnoreAppDataSource, mDataSource);
        mIgnoreListUseCase = new AddIgnoreListUseCase(mRepository);
        mAppsUseCase = new GetAppsUseCase(mMyUsageStatsManagerWrapper);
        mAllApps = mAppsUseCase.getAllApps(false, 1);
        mManager = new PhoneUsageNotificationManager(mContext);
        mShowAppUsageUseCase = new ShowAppUsageUseCase(mManager);
        mUsageCountUseCase = new GetPhoneUsageCountUseCase(mRepository);
        mDayInterval.setValue(0);
    }

    public LiveData<List<AppsModel>> getAllApps() {
        return mAllApps;
    }

    public void insertToIgnoreList(AppsModel info) {
        String packageName = info.getPackageName();
        String appName = info.getAppName();
        IgnoreItems ignoreItems = new IgnoreItems(packageName, appName);
        List<IgnoreItems> list = new ArrayList<>();
        list.add(ignoreItems);
        mIgnoreListUseCase.addToIgnoreList(ignoreItems);
    }

    public void startService() {
        MyPreferenceManager.init(mContext);
        boolean isNotificationsSwitchOn =  MyPreferenceManager.getInstance().getBoolean(mContext.getString(R.string.show_notification_key));
        if (isNotificationsSwitchOn) {
            Intent intent = new Intent(mContext, NotificationService.class);
//            mUsageCountUseCase.getPhoneUsageCount(0);
            mContext.startService(intent);
        }
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
