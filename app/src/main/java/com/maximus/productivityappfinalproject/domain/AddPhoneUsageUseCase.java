package com.maximus.productivityappfinalproject.domain;


import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;

public class AddPhoneUsageUseCase {
    private AppsRepositoryImpl mAppsRepository;

    public AddPhoneUsageUseCase(AppsRepositoryImpl appsRepository) {
        mAppsRepository = appsRepository;
    }

    public void insertPhoneUsage(PhoneUsage phoneUsage) {
        mAppsRepository.insertPhoneUsage(phoneUsage);
    }

}
