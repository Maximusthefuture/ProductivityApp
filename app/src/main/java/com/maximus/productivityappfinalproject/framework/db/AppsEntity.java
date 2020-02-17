package com.maximus.productivityappfinalproject.framework.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "apps")
public class AppsEntity {

    @PrimaryKey
    @ColumnInfo(name = "package_name")
    private String mPackageName;
    @ColumnInfo(name = "app_name")
    private String mAppName;
    @ColumnInfo(name = "event_type")
    private long mEventType;
    @ColumnInfo(name = "app_usage_time")
    private long mAppUsageTime;


    public AppsEntity(String packageName, String appName, long eventType, long appUsageTime) {
        mPackageName = packageName;
        mAppName = appName;
        mEventType = eventType;
        mAppUsageTime = appUsageTime;
    }


    public String getPackageName() {
        return mPackageName;
    }

    public String getAppName() {
        return mAppName;
    }

    public long getEventType() {
        return mEventType;
    }

    public long getAppUsageTime() {
        return mAppUsageTime;
    }
}
