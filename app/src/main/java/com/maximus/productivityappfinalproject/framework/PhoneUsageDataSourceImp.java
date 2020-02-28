package com.maximus.productivityappfinalproject.framework;

import android.content.Context;

import com.maximus.productivityappfinalproject.data.PhoneUsageDataSource;
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase;

import java.util.List;

public class PhoneUsageDataSourceImp implements PhoneUsageDataSource {
    private Context mContext;
    private AppsDatabase mAppsDatabase;

    public PhoneUsageDataSourceImp(Context context) {
        mContext = context;
        mAppsDatabase = AppsDatabase.getInstance(mContext);
    }

    @Override
    public List<PhoneUsage> getPhoneUsageData() {
        return mAppsDatabase.phoneUsageDao().getPhoneUsageData();
    }

    @Override
    public void insertPhoneUsage(PhoneUsage phoneUsage) {
        mAppsDatabase.phoneUsageDao().insertPhoneUsage(phoneUsage);
    }

    @Override
    public void updatePhoneUsage(PhoneUsage phoneUsage) {
        mAppsDatabase.phoneUsageDao().updatePhoneUsage(phoneUsage);
    }

    @Override
    public int getUsageCount(int usageCount) {
        return mAppsDatabase.phoneUsageDao().getUsageCount(usageCount);
    }

    @Override
    public int getUsageCount() {
        return mAppsDatabase.phoneUsageDao().getUsageCount();
    }

    @Override
    public void removePhoneUsage() {
        mAppsDatabase.phoneUsageDao().deletePhoneUsage();
    }
}
