package com.maximus.productivityappfinalproject.presentation;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.data.IgnoreAppDataSource;
import com.maximus.productivityappfinalproject.data.PhoneUsageDataSource;
import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.device.PhoneUsageNotificationManager;
import com.maximus.productivityappfinalproject.domain.GetAppsUseCase;
import com.maximus.productivityappfinalproject.domain.LimitedListUseCase;
import com.maximus.productivityappfinalproject.domain.ShowAppUsageUseCase;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.domain.model.LimitedApps;
import com.maximus.productivityappfinalproject.framework.IgnoreAppDataSourceImp;
import com.maximus.productivityappfinalproject.service.NotificationService;
import com.maximus.productivityappfinalproject.utils.MyPreferenceManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AppsViewModel extends ViewModel {
    private static final String TAG = "AppsViewModel";
    private AppsRepositoryImpl mRepository;
    private LiveData<List<AppsModel>> mAllApps;
    private MutableLiveData<Integer> mDayInterval = new MutableLiveData<>();
    private GetAppsUseCase mAppsUseCase;
    private LimitedListUseCase mIgnoreListUseCase;
    private MyUsageStatsManagerWrapper mMyUsageStatsManagerWrapper;
    private ShowAppUsageUseCase mShowAppUsageUseCase;
    private PhoneUsageNotificationManager mManager;
    private IgnoreAppDataSource mIgnoreAppDataSource;
//    @Inject
//     PhoneUsageDataSource mDataSource;
    //TODO
    private boolean isSystemAppMenuChecked;

    @Inject
    public AppsViewModel(LimitedListUseCase limitedListUseCase, MyUsageStatsManagerWrapper myUsageStatsManagerWrapper, GetAppsUseCase useCase) {
//        mDataSource = new PhoneUsageDataSourceImp(mContext);
//        mIgnoreAppDataSource = new IgnoreAppDataSourceImp(mContext);
//        mMyUsageStatsManagerWrapper = new MyUsageStatsManagerWrapper(mContext, mIgnoreAppDataSource);
//        mRepository = new AppsRepositoryImpl(mIgnoreAppDataSource, mDataSource);
//        mIgnoreListUseCase = new LimitedListUseCase(mRepository);
//        mAppsUseCase = new GetAppsUseCase(mMyUsageStatsManagerWrapper);
        mIgnoreListUseCase = limitedListUseCase;
        mMyUsageStatsManagerWrapper = myUsageStatsManagerWrapper;
        mAppsUseCase = useCase;

        mAllApps = mAppsUseCase.getAllApps(false, 0);
//        mShowAppUsageUseCase = new ShowAppUsageUseCase(mManager);
        mDayInterval.setValue(0);
    }

    public LiveData<List<AppsModel>> getAllApps() {
        return mAllApps;
    }





    //TODO MOVE TO USECASE
    public List<AppsModel> searchLogic(String query) {
        List<AppsModel> list = mMyUsageStatsManagerWrapper.getAllAppsObservable(false, 0);
        List<AppsModel> list2 = new ArrayList<>();
        for (AppsModel appsModel : list) {
            if (appsModel.getAppName().toLowerCase().contains(query)) {
                list2.add(new AppsModel(appsModel.getPackageName(), appsModel.getAppName(), appsModel.getAppIcon(), appsModel.getLastTimeUsed(), appsModel.getAppUsageTime()));
                return list2;
            }
        }
        return Collections.emptyList();
    }

    @NotNull
    public Disposable searchWithSearchView(Flowable<String> search, AppRecyclerViewAdapter mAdapter) {
        return search
                .observeOn(Schedulers.computation())
                .filter(s -> s.length() >= 3)
                .map(new Function<String, List<AppsModel>>() {
                    @Override
                    public List<AppsModel> apply(String s) throws Exception {
                        return searchLogic(s.toLowerCase());
                    }
                })
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mAdapter::setList, e -> {
                });
    }

    public void insertToIgnoreList(AppsModel info) {
        String packageName = info.getPackageName();
        String appName = info.getAppName();
        info.setSelected(true);
        LimitedApps limitedApps = new LimitedApps(packageName, appName);
        List<LimitedApps> list = new ArrayList<>();
        list.add(limitedApps);
        mIgnoreListUseCase.addToIgnoreList(limitedApps);

    }

    public Observable<List<AppsModel>> getAllAppsObservable() {
        return mAppsUseCase.getAllAppsObservable(false, 0);
    }

}
