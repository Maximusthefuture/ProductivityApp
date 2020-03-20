package com.maximus.productivityappfinalproject.domain.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

@Entity(tableName = "ignore_table")
public class IgnoreItems {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "package_name")
    private String mPackageName;
    @ColumnInfo(name = "created")
    private long mCreated;
    @ColumnInfo(name = "name")
    private String mName;
    @ColumnInfo(name = "last_time_used")
    private String mLastTimeUsed;
    @ColumnInfo(name = "time_used")
    private long mTimeUsed;


    public IgnoreItems(String packageName, long created, String name, String lastTimeUsed, long timeUsed) {
        mPackageName = packageName;
        mCreated = created;
        mName = name;
        mLastTimeUsed = lastTimeUsed;
        mTimeUsed = timeUsed;
    }

    @Ignore
    public IgnoreItems(@NonNull String packageName) {
        mPackageName = packageName;
    }

    @Ignore
    public IgnoreItems(@NonNull String packageName, String lastTimeUsed, long timeUsed) {
        mLastTimeUsed = lastTimeUsed;
        mPackageName = packageName;
        mTimeUsed = timeUsed;
    }
    @Ignore
    public IgnoreItems(@NonNull String packageName, String name) {
        mPackageName = packageName;
        mName = name;
    }

    @Ignore
    public IgnoreItems(@NonNull String packageName, String name, String lastTimeUsed, long timeUsed) {
        mPackageName = packageName;
        mName = name;
        mLastTimeUsed = lastTimeUsed;
        mTimeUsed = timeUsed;
    }

//    public void setTimeUsed(long timeUsed) {
//        mTimeUsed = timeUsed;
//    }

    public String getPackageName() {
        return mPackageName;
    }

    public long getCreated() {
        return mCreated;
    }

    public String getName() {
        return mName;
    }

    public String getLastTimeUsed() {
        return mLastTimeUsed;
    }

    public long getTimeUsed() {
        return mTimeUsed;
    }
}
