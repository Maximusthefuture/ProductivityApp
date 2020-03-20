package com.maximus.productivityappfinalproject.framework;

import android.content.Context;

import com.maximus.productivityappfinalproject.data.IgnoreAppDataSource;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase;

import java.util.List;

public class IgnoreAppDataSourceImp implements IgnoreAppDataSource {

    private Context mContext;
    private AppsDatabase mAppsDatabase;

    public IgnoreAppDataSourceImp(Context context) {
        mContext = context;
        mAppsDatabase = AppsDatabase.getInstance(mContext);
    }

    @Override
    public void add(IgnoreItems item) {
        mAppsDatabase.ignoreDao().insertIgnoreItem(item);

    }

    @Override
    public List<IgnoreItems> getAll() {
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
    public void update(IgnoreItems items) {
        mAppsDatabase.ignoreDao().updateIgnoreItem(items);
    }
}
