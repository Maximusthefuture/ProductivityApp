package com.maximus.productivityappfinalproject.domain;

import androidx.lifecycle.LiveData;

import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;

import java.util.List;

public class GetIgnoreListUseCase {
    private AppsRepositoryImpl mAppsRepository;

    public GetIgnoreListUseCase(AppsRepositoryImpl appsRepository) {
        mAppsRepository = appsRepository;
    }

    public LiveData<List<IgnoreItems>> getIgnoreList() {
       return  mAppsRepository.getIgnoreItems();
    }
}
