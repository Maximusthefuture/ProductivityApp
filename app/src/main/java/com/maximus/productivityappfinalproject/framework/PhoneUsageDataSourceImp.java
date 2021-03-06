package com.maximus.productivityappfinalproject.framework;

import com.maximus.productivityappfinalproject.data.PhoneUsageDataSource;
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase;

import java.util.List;

import javax.inject.Inject;

public class PhoneUsageDataSourceImp implements PhoneUsageDataSource {

    private AppsDatabase mAppsDatabase;

    @Inject
    public PhoneUsageDataSourceImp(AppsDatabase appsDatabase) {
        this.mAppsDatabase = appsDatabase;
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

    @Override
    public void resetHourly() {
        mAppsDatabase.phoneUsageDao().resetHourly();
    }

    @Override
    public void resetDaily() {
        mAppsDatabase.phoneUsageDao().resetDaily();
    }

    @Override
    public void updateUsageTime(long perDay, long perHour) {
        mAppsDatabase.phoneUsageDao().updateUsageTime(perDay, perHour);
    }

}
