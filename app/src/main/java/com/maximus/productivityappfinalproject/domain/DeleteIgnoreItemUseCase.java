package com.maximus.productivityappfinalproject.domain;

import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;

public class DeleteIgnoreItemUseCase {
    private AppsRepositoryImpl mAppsRepository;

    public DeleteIgnoreItemUseCase(AppsRepositoryImpl appsRepository) {
        mAppsRepository = appsRepository;
    }

    public void deleteIgnoreItem(String packageName) {
        mAppsRepository.deleteFromIgnoreList(packageName);
    }
}
