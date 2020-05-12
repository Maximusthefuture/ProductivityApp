package com.maximus.productivityappfinalproject.framework;

import com.maximus.productivityappfinalproject.data.LimitedAppsDataSource;
import com.maximus.productivityappfinalproject.domain.model.LimitedApps;
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase;
import java.util.List;
import javax.inject.Inject;

public class LimitedAppsDataSourceImp implements LimitedAppsDataSource {

    private AppsDatabase mAppsDatabase;

    @Inject
    public LimitedAppsDataSourceImp(AppsDatabase appsDatabase) {
        this.mAppsDatabase = appsDatabase;
    }

    @Override
    public void add(LimitedApps item) {
        mAppsDatabase.ignoreDao().insertIgnoreItem(item);

    }

    @Override
    public List<LimitedApps> getAll() {
        return mAppsDatabase.ignoreDao().getIgnoreItems();
    }

    @Override
    public void removeItem(String packageName) {
        mAppsDatabase.ignoreDao().deleteByPackageName(packageName);
    }

    @Override
    public void removeAll() {
        mAppsDatabase.ignoreDao().deleteAllItems();
    }

    @Override
    public void update(LimitedApps items) {
        mAppsDatabase.ignoreDao().updateIgnoreItem(items);
    }
}
