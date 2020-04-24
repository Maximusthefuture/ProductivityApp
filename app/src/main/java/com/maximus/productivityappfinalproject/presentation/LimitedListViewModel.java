package com.maximus.productivityappfinalproject.presentation;

import android.app.Application;
import android.content.Context;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.google.android.material.chip.Chip;
import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.data.AppLimitDataSource;
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.data.IgnoreAppDataSource;
import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.domain.GetAppWithLimitUseCase;
import com.maximus.productivityappfinalproject.domain.LimitedListUseCase;
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.framework.AppLimitDataSourceImpl;
import com.maximus.productivityappfinalproject.framework.IgnoreAppDataSourceImp;
import com.maximus.productivityappfinalproject.utils.Utils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class LimitedListViewModel extends AndroidViewModel {

    private static final String TAG = "IgnoreListViewModel";
    private AppsRepositoryImpl mAppsRepository;
    private LiveData<List<IgnoreItems>> mAllIgnoreItems;

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

    private IgnoreAppDataSource mIgnoreAppDataSourceImp;
    private Context mContext;
    private LimitedListUseCase mLimitedListUseCase;
    private MyUsageStatsManagerWrapper mMyUsageStatsManagerWrapper;
    private AppLimitDataSource mAppLimitDataSource;


    public LimitedListViewModel(@NonNull Application application) {
        super(application);


        mContext = application.getApplicationContext();
        mMyUsageStatsManagerWrapper = new MyUsageStatsManagerWrapper(mContext);
        mIgnoreAppDataSourceImp = new IgnoreAppDataSourceImp(mContext);
        mAppLimitDataSource = new AppLimitDataSourceImpl(mContext);
        mAppsRepository = new AppsRepositoryImpl(mIgnoreAppDataSourceImp, mAppLimitDataSource);
        mLimitedListUseCase = new LimitedListUseCase(mAppsRepository);
        mAllIgnoreItems = mLimitedListUseCase.getLimitedList();
//        mAppWithLimitUseCase = new GetAppWithLimitUseCase(mAppsRepository);
    }

    public LiveData<List<IgnoreItems>> getAllIgnoreItems() {
        return mAllIgnoreItems;
    }

    public void deleteAll() {
        mLimitedListUseCase.deleteAllLimitedItems();

    }

    public void deleteItem(String packageName) {
        mLimitedListUseCase.deleteLimitedItem(packageName);
    }

    public Observable<List<AppUsageLimitModel>> getLimited() {
        return mAppsRepository.getLimited();
    }


    public String getRemainingTime(String packageName) {
        getLimited()
                .flatMap((list) ->  Observable.fromIterable(list))
                .subscribe((app) -> {
                    if (app.getPackageName().equals(packageName)) {
                         app.getTimeLimitPerHour();
                    }
                }, e-> {
                    Log.e(TAG, "getRemainingTime: ",  e);
                });
        return null;
    }

    public Observable<List<IgnoreItems>> getIgnoreItems() {
        return mAppsRepository.getIgnoreList();
    }

    public void refresh(IgnoreItems ignoreItems) {
        mMyUsageStatsManagerWrapper.refreshIgnoreList(ignoreItems);
    }

    //TODO
    public void showLimitedTime(List<AppUsageLimitModel> list, String mPackageName, Chip mLimitHourlyChip, Chip mLimitDailyChip) {
        for (AppUsageLimitModel appUsageLimitModel : list) {
            if (appUsageLimitModel.getPackageName().equals(mPackageName)) {
                int timeLimitPerDay = appUsageLimitModel.getTimeLimitPerDay();
                int timeLimitPerHour = appUsageLimitModel.getTimeLimitPerHour();
                int timeCompleted = appUsageLimitModel.getTimeCompleted();
                Log.d(TAG, "showLimitedTime: " + appUsageLimitModel.getPackageName() + "   " + appUsageLimitModel.getTimeLimitPerHour());
                if (timeLimitPerDay >= 90000000) {
                    //TODO livedata?
                    mLimitHourlyChip.setText("Лимит не установлен");
                } else {
                    mLimitDailyChip.setText(mContext.getString(R.string.limit_in_day_chip_with_args, Utils.formatMillisToSeconds(timeLimitPerDay)));
                }
                if (timeLimitPerHour >= 90000000) {
                    mLimitHourlyChip.setText("Лимит не установлен");
                } else {
                    mLimitHourlyChip.setText(mContext.getString(R.string.hourly_limit_set_to, Utils.formatMillisToSeconds(timeLimitPerHour)));
                }

                Toast.makeText(mContext, Utils.formatMillisToSeconds(timeCompleted), Toast.LENGTH_SHORT).show();
            }
//            else {
//                mLimitDailyChip.setText("Лимит не установлен");
//                mLimitHourlyChip.setText("Лимит не установлен");
//            }
        }
    }

    public Disposable showLimitedTimeOnChips(String packageName, Chip mLimitHourlyChip, Chip mLimitDailyChip) {
        return getLimited()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    showLimitedTime(list, packageName, mLimitHourlyChip, mLimitDailyChip);
                }, e -> {
                    Log.d(TAG, "rxjavasomething: " + e);
                });
    }
}
