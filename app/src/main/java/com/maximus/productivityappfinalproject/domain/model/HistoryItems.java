package com.maximus.productivityappfinalproject.domain.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "History")
public class HistoryItems {

    @PrimaryKey
    @ColumnInfo(name = "app_package_name")
    @NonNull
    private String mPackageName;
    @ColumnInfo(name = "name")
    private String mName;
    @ColumnInfo(name = "date")
    private String mDate;
    @ColumnInfo(name = "isSystem")
    private int mIsSystem;
    @ColumnInfo(name = "duration")
    private long mDuration;
    @ColumnInfo(name = "time_stamp")
    private long mTimeStamp;




    public HistoryItems(String packageName, String name, String date, int isSystem, long duration, long timeStamp) {
        mPackageName = packageName;
        mName = name;
        mDate = date;
        mIsSystem = isSystem;
        mDuration = duration;
        mTimeStamp = timeStamp;
    }
    @Ignore
    public HistoryItems(long duration, long timeStamp) {
        mDuration = duration;
        mTimeStamp = timeStamp;
    }
    @Ignore
    public HistoryItems(String packageName, String name) {
        mPackageName = packageName;
        mName = name;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public String getName() {
        return mName;
    }

    public String getDate() {
        return mDate;
    }

    public int getIsSystem() {
        return mIsSystem;
    }

    public long getDuration() {
        return mDuration;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }
}
