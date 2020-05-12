package com.maximus.productivityappfinalproject.presentation.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper;
import com.maximus.productivityappfinalproject.domain.GetAppsUseCase;
import com.maximus.productivityappfinalproject.domain.LimitedListUseCase;
import com.maximus.productivityappfinalproject.domain.SearchAppUseCase;
import com.maximus.productivityappfinalproject.domain.model.AppsModel;
import com.maximus.productivityappfinalproject.domain.model.LimitedApps;
import com.maximus.productivityappfinalproject.presentation.AppRecyclerViewAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class AppsViewModel extends ViewModel {
    private static final String TAG = "AppsViewModel";
    private LiveData<List<AppsModel>> mAllApps;
    private MutableLiveData<Integer> mDayInterval = new MutableLiveData<>();
    private GetAppsUseCase mAppsUseCase;
    private LimitedListUseCase mIgnoreListUseCase;
    private MyUsageStatsManagerWrapper mMyUsageStatsManagerWrapper;
    private SearchAppUseCase mSearchAppUseCase;

    //TODO
    private boolean isSystemAppMenuChecked;

    @Inject
    public AppsViewModel(LimitedListUseCase limitedListUseCase, MyUsageStatsManagerWrapper myUsageStatsManagerWrapper, GetAppsUseCase useCase, SearchAppUseCase searchAppUseCase) {
        mIgnoreListUseCase = limitedListUseCase;
        mMyUsageStatsManagerWrapper = myUsageStatsManagerWrapper;
        mAppsUseCase = useCase;
        mSearchAppUseCase = searchAppUseCase;
        mAllApps = mAppsUseCase.getAllApps(false, 0);
        mDayInterval.setValue(0);
    }

    public LiveData<List<AppsModel>> getAllApps() {
        return mAllApps;
    }

    @NotNull
    public Disposable searchWithSearchView(Flowable<String> search, AppRecyclerViewAdapter mAdapter) {
        return mSearchAppUseCase.searchWithSearchView(search, mAdapter);
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
