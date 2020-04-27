package com.maximus.productivityappfinalproject.presentation;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.material.chip.Chip;
import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.data.AppLimitDataSource;
import com.maximus.productivityappfinalproject.data.AppsRepository;
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.domain.LimitedListUseCase;
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;
import com.maximus.productivityappfinalproject.domain.model.LimitedApps;
import com.maximus.productivityappfinalproject.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LimitedListViewModel extends ViewModel {

    private static final String TAG = "IgnoreListViewModel";

    AppsRepository mAppsRepository;

    LimitedListUseCase mLimitedListUseCase;
    private MutableLiveData<List<LimitedApps>> mAllIgnoreItems;
    /**
     * Используем в data binding
     */

    private List<LimitedApps> mLimitedApps;
    //    private IgnoreAppDataSource mIgnoreAppDataSourceImp;
    private Context mContext;
    private MyUsageStatsManagerWrapper mMyUsageStatsManagerWrapper;
    private AppLimitDataSource mAppLimitDataSource;


    @Inject
    public LimitedListViewModel(AppsRepository appsRepository, LimitedListUseCase useCase, MyUsageStatsManagerWrapper myUsageStatsManagerWrapper) {
        this.mAppsRepository = appsRepository;
        this.mLimitedListUseCase = useCase;
        this.mMyUsageStatsManagerWrapper = myUsageStatsManagerWrapper;
        mAllIgnoreItems = new MutableLiveData<>();

    }

    public final LiveData<Boolean> empty = Transformations.map(mAllIgnoreItems,
            new Function<List<LimitedApps>, Boolean>() {
                @Override
                public Boolean apply(List<LimitedApps> input) {
                    return input.isEmpty();
                }
            });

    public LiveData<List<LimitedApps>> getAllIgnoreItems() {
        return mAllIgnoreItems;
    }

    public void deleteAll() {
        mLimitedListUseCase.deleteAllLimitedItems();
    }

    public void getLimitedItems() {
        Disposable d =  mLimitedListUseCase.getLimitedList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(limitedApps -> onLimitedItemsFetched(limitedApps));
    }

    private void onLimitedItemsFetched(List<LimitedApps> limitedApps) {
        mLimitedApps = new ArrayList<>(limitedApps.size());
        mAllIgnoreItems.setValue(limitedApps);
    }

    public void deleteItem(String packageName) {
        mLimitedListUseCase.deleteLimitedItem(packageName);
    }

//    public Observable<List<AppUsageLimitModel>> getLimited() {
//        return mAppsRepository.getLimitedItems();
//    }


//    public String getRemainingTime(String packageName) {
//        getLimited()
//                .flatMap((list) -> Observable.fromIterable(list))
//                .subscribe((app) -> {
//                    if (app.getPackageName().equals(packageName)) {
//                        app.getTimeLimitPerHour();
//                    }
//                }, e -> {
//                    Log.e(TAG, "getRemainingTime: ", e);
//                });
//        return null;
//    }


    public void refresh(LimitedApps limitedApps) {
        mMyUsageStatsManagerWrapper.refreshIgnoreList(limitedApps);
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

//    public Disposable showLimitedTimeOnChips(String packageName, Chip mLimitHourlyChip, Chip mLimitDailyChip) {
//        return getLimited()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(list -> {
//                    showLimitedTime(list, packageName, mLimitHourlyChip, mLimitDailyChip);
//                }, e -> {
//                    Log.d(TAG, "rxjavasomething: " + e);
//                });
//    }
}
