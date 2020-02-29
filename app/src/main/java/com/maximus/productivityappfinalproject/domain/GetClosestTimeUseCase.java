package com.maximus.productivityappfinalproject.domain;

import com.maximus.productivityappfinalproject.data.AppsRepository;

public class GetClosestTimeUseCase {
    private AppsRepository mAppsRepository;


    public GetClosestTimeUseCase(AppsRepository appsRepository) {
        mAppsRepository = appsRepository;
    }

    public Long getClosestHour() {
        return mAppsRepository.getClosestHour();
    }

    public Long getClosestDay() {
        return mAppsRepository.getClosestDay();
    }

}
