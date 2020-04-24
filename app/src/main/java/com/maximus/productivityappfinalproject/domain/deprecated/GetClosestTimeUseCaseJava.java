package com.maximus.productivityappfinalproject.domain.deprecated;

import com.maximus.productivityappfinalproject.data.AppsRepository;

public class GetClosestTimeUseCaseJava {
    private AppsRepository mAppsRepository;


    public GetClosestTimeUseCaseJava(AppsRepository appsRepository) {
        mAppsRepository = appsRepository;
    }

    public Long getClosestHour() {
        return mAppsRepository.getClosestHour();
    }

    public Long getClosestDay() {
        return mAppsRepository.getClosestDay();
    }

}
