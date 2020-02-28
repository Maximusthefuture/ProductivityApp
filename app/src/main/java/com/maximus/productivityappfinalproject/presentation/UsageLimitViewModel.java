package com.maximus.productivityappfinalproject.presentation;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.maximus.productivityappfinalproject.data.AppLimitDataSource;
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.data.PhoneUsageDataSource;
import com.maximus.productivityappfinalproject.domain.GetAppWithLimitUseCase;
import com.maximus.productivityappfinalproject.domain.SetAppWithLimitUseCase;
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;
import com.maximus.productivityappfinalproject.framework.AppLimitDataSourceImpl;
import com.maximus.productivityappfinalproject.framework.PhoneUsageDataSourceImp;
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase;

import java.util.List;

public class UsageLimitViewModel extends AndroidViewModel {
    private static final String TAG = "UsageLimitViewModel";
    private AppsRepositoryImpl mAppsRepository;
    private GetAppWithLimitUseCase mGetAppWithLimitUseCase;
    private AppLimitDataSource mAppLimitDataSource;
    private SetAppWithLimitUseCase mSetAppWithLimitUseCase;
    private Context mContext;
    private PhoneUsageDataSource mPhoneUsageDataSource;

    public UsageLimitViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mPhoneUsageDataSource = new PhoneUsageDataSourceImp(mContext);
        mAppLimitDataSource = new AppLimitDataSourceImpl(application.getApplicationContext());
        mAppsRepository = new AppsRepositoryImpl(mPhoneUsageDataSource, mAppLimitDataSource);
        mGetAppWithLimitUseCase = new GetAppWithLimitUseCase(mAppsRepository);
        mSetAppWithLimitUseCase = new SetAppWithLimitUseCase(mAppsRepository, application.getApplicationContext());
    }


    public void setLimit(String appPackageName, String appName,int perDayHours, int perDayMinutes, int perHourMinutes) {
        mSetAppWithLimitUseCase.setLimit(appPackageName, appName, perDayHours, perDayMinutes, perHourMinutes);
    }

    public void checkInput(int perDayHours, int perDayMinutes, int perHourMinutes) {
        mSetAppWithLimitUseCase.checkUserInput(perDayHours, perDayMinutes, perHourMinutes);
    }
}
