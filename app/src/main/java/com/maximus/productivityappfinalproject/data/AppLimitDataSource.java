package com.maximus.productivityappfinalproject.data;

import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;

import java.util.List;

public interface AppLimitDataSource {

    void addToLimit(AppUsageLimitModel app);
    List<AppUsageLimitModel> getLimitedApps();
    void removeLimitedApp(String packageName);
}
