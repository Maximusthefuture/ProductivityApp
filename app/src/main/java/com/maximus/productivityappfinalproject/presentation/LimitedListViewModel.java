package com.maximus.productivityappfinalproject.presentation;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.maximus.productivityappfinalproject.data.AppLimitDataSource;
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.data.IgnoreAppDataSource;
import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.domain.DeleteIgnoreItemUseCase;
import com.maximus.productivityappfinalproject.domain.GetAppWithLimitUseCase;
import com.maximus.productivityappfinalproject.domain.GetIgnoreListUseCase;
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.framework.AppLimitDataSourceImpl;
import com.maximus.productivityappfinalproject.framework.IgnoreAppDataSourceImp;
import com.maximus.productivityappfinalproject.utils.Utils;

import java.util.List;

public class LimitedListViewModel extends AndroidViewModel {

    private static final String TAG = "IgnoreListViewModel";
    private AppsRepositoryImpl mAppsRepository;
    private LiveData<List<IgnoreItems>> mAllIgnoreItems;
    private IgnoreAppDataSource mIgnoreAppDataSourceImp;
    private Context mContext;
    private GetIgnoreListUseCase mIgnoreListUseCase;
    private DeleteIgnoreItemUseCase mDeleteIgnoreItemUseCase;
    private MyUsageStatsManagerWrapper mMyUsageStatsManagerWrapper;
    private GetAppWithLimitUseCase mAppWithLimitUseCase;
    private AppLimitDataSource mAppLimitDataSource;


    /**
     * Используем в data binding
     */
    public final LiveData<Boolean> empty = Transformations.map(mAllIgnoreItems,
            new Function<List<IgnoreItems>, Boolean>() {
                @Override
                public Boolean apply(List<IgnoreItems> input) {
                    return input.isEmpty();
                }
            });



    public LimitedListViewModel(@NonNull Application application) {
        super(application);

        mContext = application.getApplicationContext();
        mMyUsageStatsManagerWrapper = new MyUsageStatsManagerWrapper(mContext);
        mIgnoreAppDataSourceImp = new IgnoreAppDataSourceImp(mContext);
        mAppLimitDataSource = new AppLimitDataSourceImpl(mContext);
        mAppsRepository = new AppsRepositoryImpl(mIgnoreAppDataSourceImp, mAppLimitDataSource);
        mDeleteIgnoreItemUseCase = new DeleteIgnoreItemUseCase(mAppsRepository);
        mIgnoreListUseCase = new GetIgnoreListUseCase(mAppsRepository);
        mAllIgnoreItems = mIgnoreListUseCase.getIgnoreList();
        mAppWithLimitUseCase = new GetAppWithLimitUseCase(mAppsRepository);
    }

    public LiveData<List<IgnoreItems>> getAllIgnoreItems() {
        return mAllIgnoreItems;
    }

    public void deleteAll() {
        mAppsRepository.deleteAllIgnoreList();
        getAllIgnoreItems();
    }

    public void refresh(IgnoreItems ignoreItems) {
        mMyUsageStatsManagerWrapper.refreshIgnoreList(ignoreItems);
    }

    public void deleteIgnoreItem(String packageName) {
        mDeleteIgnoreItemUseCase.deleteIgnoreItem(packageName);
    }

    public boolean isLimit(String packageName) {
        return mAppWithLimitUseCase.isLimitSet(packageName);
    }

    //TODO move to usecase?
    public String getLimitTimePerDay(String packageName) {
        int time = 0;
        List<AppUsageLimitModel> models = mAppsRepository.getLimitedItems();
        for (AppUsageLimitModel model : models) {
            if (model.getPackageName().equals(packageName)) {
                time = model.getTimeLimitPerDay();
                return Utils.formatMillisToSeconds(time);
            }
        }
        return Utils.formatMillisToSeconds(time);
    }

 }
