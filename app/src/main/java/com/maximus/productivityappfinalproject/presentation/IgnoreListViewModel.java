package com.maximus.productivityappfinalproject.presentation;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.data.IgnoreAppDataSource;
import com.maximus.productivityappfinalproject.domain.DeleteIgnoreItemUseCase;
import com.maximus.productivityappfinalproject.domain.GetIgnoreListUseCase;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.framework.IgnoreAppDataSourceImp;

import java.util.List;

public class IgnoreListViewModel extends AndroidViewModel {

    private AppsRepositoryImpl mAppsRepository;
    private LiveData<List<IgnoreItems>> mAllIgnoreItems;
    private IgnoreAppDataSource mIgnoreAppDataSourceImp;
    private Context mContext;
    private GetIgnoreListUseCase mIgnoreListUseCase;
    private DeleteIgnoreItemUseCase mDeleteIgnoreItemUseCase;

    public IgnoreListViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
        mIgnoreAppDataSourceImp = new IgnoreAppDataSourceImp(mContext);
        mAppsRepository = new AppsRepositoryImpl(mIgnoreAppDataSourceImp);
        mDeleteIgnoreItemUseCase = new DeleteIgnoreItemUseCase(mAppsRepository);
        mIgnoreListUseCase = new GetIgnoreListUseCase(mAppsRepository);
        mAllIgnoreItems = mIgnoreListUseCase.getIgnoreList();
    }

    public LiveData<List<IgnoreItems>> getAllIgnoreItems() {
        return mAllIgnoreItems;
    }

    public void deleteAll() {
        mAppsRepository.deleteAllIgnoreList();
    }

    public void deleteIgnoreItem(String packageName) {
        mDeleteIgnoreItemUseCase.deleteIgnoreItem(packageName);
    }
 }
