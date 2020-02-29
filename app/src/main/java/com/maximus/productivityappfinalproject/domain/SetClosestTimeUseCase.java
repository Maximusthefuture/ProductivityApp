package com.maximus.productivityappfinalproject.domain;

import com.maximus.productivityappfinalproject.data.AppsRepository;

public class SetClosestTimeUseCase {
    private AppsRepository mAppsRepository;


    public SetClosestTimeUseCase(AppsRepository appsRepository) {
        mAppsRepository = appsRepository;
    }

    public void setClosestHour(long hour) {
        mAppsRepository.setClosestHour(hour);
    }

    public void setClosestDay(long day) {
        mAppsRepository.setClosestDay(day);
    }
}
