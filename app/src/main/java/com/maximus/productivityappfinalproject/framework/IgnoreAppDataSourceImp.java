package com.maximus.productivityappfinalproject.framework;

import android.content.Context;

import com.maximus.productivityappfinalproject.data.IgnoreAppDataSource;
import com.maximus.productivityappfinalproject.domain.model.LimitedApps;
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

public class IgnoreAppDataSourceImp implements IgnoreAppDataSource {

    private Context mContext;
    private AppsDatabase mAppsDatabase;

    @Inject
    public IgnoreAppDataSourceImp(Context context) {
        mContext = context;
        mAppsDatabase = AppsDatabase.getInstance(mContext);
    }

    @Override
    public void add(LimitedApps item) {
        mAppsDatabase.ignoreDao().insertIgnoreItem(item);

    }

    @Override
    public Flowable<List<LimitedApps>> getAll() {
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
