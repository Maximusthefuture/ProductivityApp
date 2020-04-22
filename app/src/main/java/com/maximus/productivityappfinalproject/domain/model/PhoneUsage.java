package com.maximus.productivityappfinalproject.domain.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "phone_usage_table")
public class PhoneUsage {


    @ColumnInfo(name = "package_name")
    @NonNull
    @PrimaryKey()
    private String packageName;
    @ColumnInfo(name = "usage_count")
    private int usageCount;
    @ColumnInfo(name = "usage_time")
    private String usageTime;
    @ColumnInfo(name = "time_completed_in_hour")
    private long timeCompletedInHour;
    @ColumnInfo(name = "time_completed_in_day")
    private long timeCompletedInDay;

    public PhoneUsage(String packageName, int usageCount, String usageTime, long timeCompletedInHour, long timeCompletedInDay) {
        this.packageName = packageName;
        this.usageCount = usageCount;
        this.usageTime = usageTime;
        this.timeCompletedInHour = timeCompletedInHour;
        this.timeCompletedInDay = timeCompletedInDay;
    }

    @Ignore
    public PhoneUsage(String packageName, long timeCompletedInHour, long timeCompletedInDay) {
        this.packageName = packageName;
        this.timeCompletedInHour = timeCompletedInHour;
        this.timeCompletedInDay = timeCompletedInDay;
    }

    @Ignore
    public PhoneUsage(long timeCompletedInHour, long timeCompletedInDay) {
        this.timeCompletedInHour = timeCompletedInHour;
        this.timeCompletedInDay = timeCompletedInDay;
    }

    @Ignore
    public PhoneUsage(int usageCount) {
        this.usageCount = usageCount;
    }

    @Ignore
    public PhoneUsage() {
    }

    public String getPackageName() {
        return packageName;
    }

    public long getTimeCompletedInHour() {
        return timeCompletedInHour;
    }

    public long getTimeCompletedInDay() {
        return timeCompletedInDay;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public String getUsageTime() {
        return usageTime;
    }


}
