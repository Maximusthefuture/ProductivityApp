package com.maximus.productivityappfinalproject.framework;

import android.content.Context;

import com.maximus.productivityappfinalproject.data.IgnoreAppDataSource;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase;

import java.util.List;

public class IgnoreAppDataSourceImp implements IgnoreAppDataSource {

    private Context mContext;

    public IgnoreAppDataSourceImp(Context context) {
        mContext = context;
    }

    private AppsDatabase mAppsDatabase =  AppsDatabase.getInstance(mContext);

    @Override
    public void add(IgnoreItems ignoreItems) {
        mAppsDatabase.ignoreDao().insertIgnoreItem(ignoreItems);

    }

    @Override
    public List<IgnoreItems> getAll() {
        return mAppsDatabase.ignoreDao().getIgnoreItems();
    }

    @Override
    public void removeItem(IgnoreItems ignoreItems) {
        mAppsDatabase.ignoreDao().deleteByPackageName(ignoreItems.getPackageName());
    }

    @Override
    public void removeItem(String packageName) {
        mAppsDatabase.ignoreDao().deleteByPackageName(packageName);
    }

    @Override
    public void removeAll() {
        mAppsDatabase.ignoreDao().deleteAllItems();
    }
}
