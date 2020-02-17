package com.maximus.productivityappfinalproject.domain;

import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;

public class AddIgnoreListUseCase {
    private AppsRepositoryImpl mAppsRepository;

    public AddIgnoreListUseCase(AppsRepositoryImpl appsRepository) {
        mAppsRepository = appsRepository;
    }

    public void addToIgnoreList(IgnoreItems item) {
        mAppsRepository.insertToIgnoreList(item);
    }
}
