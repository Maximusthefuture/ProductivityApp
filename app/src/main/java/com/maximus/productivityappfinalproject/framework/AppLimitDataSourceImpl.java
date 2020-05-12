package com.maximus.productivityappfinalproject.framework;

import com.maximus.productivityappfinalproject.data.AppLimitDataSource;
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase;

import java.util.List;

import javax.inject.Inject;

public class AppLimitDataSourceImpl implements AppLimitDataSource {


    private AppsDatabase mAppsDatabase;

    @Inject
    public AppLimitDataSourceImpl(AppsDatabase appsDatabase) {
        this.mAppsDatabase = appsDatabase;
    }

    @Override
    public void addToLimit(AppUsageLimitModel app) {
        mAppsDatabase.appLimitDao().insertAppLimit(app);
    }

    @Override
    public List<AppUsageLimitModel> getLimitedApps() {
        return mAppsDatabase.appLimitDao().getUsageLimitList();
    }

    @Override
    public void removeLimitedApp(String packageName) {
        mAppsDatabase.appLimitDao().deleteAppLimit(packageName);
    }
}
