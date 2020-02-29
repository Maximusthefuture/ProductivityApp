package com.maximus.productivityappfinalproject.domain;

import com.maximus.productivityappfinalproject.data.AppsRepository;

public class ResetAppUseTimeDbUseCase {

    private AppsRepository mAppsRepository;

    public ResetAppUseTimeDbUseCase(AppsRepository appsRepository) {
        mAppsRepository = appsRepository;
    }

    public void resetHourAppUsage() {
        mAppsRepository.resetHourly();
    }

    public void resetDayAppUsage() {
        mAppsRepository.resetDaily();
    }
}
