package com.maximus.productivityappfinalproject.data;

import androidx.lifecycle.LiveData;

import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;


import java.util.List;

public interface AppsRepository {


    LiveData<List<IgnoreItems>> getIgnoreItems();
    void insertToIgnoreList(IgnoreItems item);
    void deleteFromIgnoreList(String item);
    void deleteAllIgnoreList();


    LiveData<Integer> getUsageCount(int usageCount);
    void insertPhoneUsage(PhoneUsage phoneUsage);
    LiveData<Integer> getUsageCount();
    void deletePhoneUsage();
    void updatePhoneUsage(PhoneUsage phoneUsage);
    List<PhoneUsage> getPhoneUsageData();

    List<AppUsageLimitModel> getLimitedItems();
    void addToLimit(AppUsageLimitModel app);
    void removeLimitedApp(String packageName);

}
