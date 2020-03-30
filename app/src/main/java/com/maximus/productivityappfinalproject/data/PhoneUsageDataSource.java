package com.maximus.productivityappfinalproject.data;

import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;

import java.util.List;

public interface PhoneUsageDataSource {

    List<PhoneUsage> getPhoneUsageData();

    void insertPhoneUsage(PhoneUsage phoneUsage);

    void updatePhoneUsage(PhoneUsage phoneUsage);

    int getUsageCount(int usageCount);

    int getUsageCount();

    void removePhoneUsage();

    void resetHourly();

    void resetDaily();

    void updateUsageTime(long perDay, long perHour);
}
