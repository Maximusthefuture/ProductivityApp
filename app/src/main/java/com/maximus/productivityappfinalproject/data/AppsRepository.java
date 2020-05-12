package com.maximus.productivityappfinalproject.data;



import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;
import com.maximus.productivityappfinalproject.domain.model.LimitedApps;
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;


import java.util.List;


import io.reactivex.Observable;

public interface AppsRepository {


    Observable<List<LimitedApps>> getIgnoreItems();
    void insertToIgnoreList(LimitedApps item);
    void deleteFromIgnoreList(String item);
    void deleteAllIgnoreList();


    void insertPhoneUsage(PhoneUsage phoneUsage);
    void resetHourly();
    void resetDaily();
    void deletePhoneUsage();
    void updatePhoneUsage(PhoneUsage phoneUsage);
    List<PhoneUsage> getPhoneUsageData();

    List<AppUsageLimitModel> getLimitedItems();
    void addToLimit(AppUsageLimitModel app);
    void removeLimitedApp(String packageName);

    void setClosestHour(long hour);
    Long getClosestHour();
    void setClosestDay(long day);
    Long getClosestDay();


}
