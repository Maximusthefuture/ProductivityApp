package com.maximus.productivityappfinalproject.domain.deprecated;

import com.maximus.productivityappfinalproject.data.AppsRepository;

public class ResetAppUseTimeDbUseCaseJava {

    private AppsRepository mAppsRepository;

    public ResetAppUseTimeDbUseCaseJava(AppsRepository appsRepository) {
        mAppsRepository = appsRepository;
    }

    public void resetHourAppUsage() {
        mAppsRepository.resetHourly();
    }

    public void resetDayAppUsage() {
        mAppsRepository.resetDaily();
    }
}
