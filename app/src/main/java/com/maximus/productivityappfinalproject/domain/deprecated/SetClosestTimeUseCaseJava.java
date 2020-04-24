package com.maximus.productivityappfinalproject.domain.deprecated;

import com.maximus.productivityappfinalproject.data.AppsRepository;

public class SetClosestTimeUseCaseJava {

    private AppsRepository mAppsRepository;

//    @Inject
    public SetClosestTimeUseCaseJava(AppsRepository appsRepository) {
        mAppsRepository = appsRepository;
    }

    public void setClosestHour(long hour) {
        mAppsRepository.setClosestHour(hour);
    }

    public void setClosestDay(long day) {
        mAppsRepository.setClosestDay(day);
    }
}
