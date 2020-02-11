package com.maximus.productivityappfinalproject.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.maximus.productivityappfinalproject.data.AppsRepository;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;

import java.util.List;

public class IgnoreListViewModel extends AndroidViewModel {

    private AppsRepository mAppsRepository;
    private LiveData<List<IgnoreItems>> mAllIgnoreItems;

    public IgnoreListViewModel(@NonNull Application application) {
        super(application);
        mAppsRepository = new AppsRepository(application);
        mAllIgnoreItems = mAppsRepository.getAllIgnoreItems();
    }

    public LiveData<List<IgnoreItems>> getAllIgnoreItems() {
        return mAllIgnoreItems;
    }

    public void deleteAll() {
        mAppsRepository.deleteAllIgnoreItems();
    }
 }
