package com.maximus.productivityappfinalproject.domain;

import androidx.lifecycle.LiveData;

import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;

public class GetPhoneUsageCountUseCase {
    private AppsRepositoryImpl mAppsRepository;

    public GetPhoneUsageCountUseCase(AppsRepositoryImpl appsRepository) {
        mAppsRepository = appsRepository;
    }

    public LiveData<Integer> getPhoneUsageCount(int count) {
       return mAppsRepository.getUsageCount(count);
    }

    public LiveData<Integer> getPhoneUsageCount() {
        return mAppsRepository.getUsageCount();
    }
}
