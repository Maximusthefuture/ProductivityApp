package com.maximus.productivityappfinalproject.framework;

import android.content.Context;

import com.maximus.productivityappfinalproject.data.AppLimitDataSource;
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase;

import java.util.List;

import javax.inject.Inject;

public class AppLimitDataSourceImpl implements AppLimitDataSource {

    private Context mContext;
    private AppsDatabase mAppsDatabase;

    @Inject
    public AppLimitDataSourceImpl(Context context) {
        mContext = context;
        mAppsDatabase = AppsDatabase.getInstance(mContext);
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
