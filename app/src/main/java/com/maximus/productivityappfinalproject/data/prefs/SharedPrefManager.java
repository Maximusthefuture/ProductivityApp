package com.maximus.productivityappfinalproject.data.prefs;

public interface SharedPrefManager {

    void setClosestHour(long hour);
    Long getClosestHour();
    void setClosestDay(long day);
    Long getClosetDay();
}
