package com.maximus.productivityappfinalproject.presentation;

import android.app.Application;
import android.content.Context;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.data.AppLimitDataSource;
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.data.PhoneUsageDataSource;
import com.maximus.productivityappfinalproject.domain.SetAppWithLimitUseCase;
import com.maximus.productivityappfinalproject.framework.AppLimitDataSourceImpl;
import com.maximus.productivityappfinalproject.framework.PhoneUsageDataSourceImp;

public class UsageLimitViewModel extends AndroidViewModel {
    private static final String TAG = "UsageLimitViewModel";
    private AppsRepositoryImpl mAppsRepository;

    private AppLimitDataSource mAppLimitDataSource;
    private SetAppWithLimitUseCase mSetAppWithLimitUseCase;
    private Context mContext;
    private PhoneUsageDataSource mPhoneUsageDataSource;
    private MutableLiveData<Integer> mSnackbarText = new MutableLiveData<>();

    public UsageLimitViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mPhoneUsageDataSource = new PhoneUsageDataSourceImp(mContext);
        mAppLimitDataSource = new AppLimitDataSourceImpl(application.getApplicationContext());
        mAppsRepository = new AppsRepositoryImpl(mPhoneUsageDataSource, mAppLimitDataSource);
        mSetAppWithLimitUseCase = new SetAppWithLimitUseCase(mAppsRepository, application.getApplicationContext());
    }

    public void setLimit(String appPackageName, String appName, int perDayHours, int perDayMinutes, int perHourMinutes) {
        mSetAppWithLimitUseCase.setLimit(appPackageName, appName, perDayHours, perDayMinutes, perHourMinutes);
        mSnackbarText.setValue(R.string.limit_set);

    }

    public TextWatcher checkInput(EditText inHour, EditText inDayHour, EditText inDayMinutes, Button materialButton) {
        return mSetAppWithLimitUseCase.checkInput(inHour, inDayHour, inDayMinutes, materialButton);
//        mSetAppWithLimitUseCase.checkUserInput(perDayHours, perDayMinutes, perHourMinutes);
    }

    public LiveData<Integer> getSnackBarMessage() {
        return mSnackbarText;
    }

}
