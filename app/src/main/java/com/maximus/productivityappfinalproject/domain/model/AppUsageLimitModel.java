package com.maximus.productivityappfinalproject.domain.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "app_limit_table")
public class AppUsageLimitModel {


    @ColumnInfo
    @NonNull
    @PrimaryKey
    private String packageName;
    @ColumnInfo
    private String appName;
    @ColumnInfo
    private int timeLimitPerDay;
    @ColumnInfo
    private int timeLimitPerHour;
    @ColumnInfo
    private int timeCompleted;
    @ColumnInfo
    private boolean isAppLimited;

    public AppUsageLimitModel(String appName, String packageName, int timeLimitPerDay, int timeLimitPerHour, int timeCompleted, boolean isAppLimited) {
        this.appName = appName;
        this.packageName = packageName;
        this.timeLimitPerDay = timeLimitPerDay;
        this.timeLimitPerHour = timeLimitPerHour;
        this.timeCompleted = timeCompleted;
        this.isAppLimited = isAppLimited;
    }

    @Ignore
    public AppUsageLimitModel(String appName, int timeLimitPerDay, int timeLimitPerHour, boolean isAppLimited) {
        this.appName = appName;
        this.timeLimitPerDay = timeLimitPerDay;
        this.timeLimitPerHour = timeLimitPerHour;
        this.isAppLimited = isAppLimited;
    }



    @Ignore
    public AppUsageLimitModel() {
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getTimeLimitPerDay() {
        return timeLimitPerDay;
    }

    public int getTimeLimitPerHour() {
        return timeLimitPerHour;
    }

    public int getTimeCompleted() {
        return timeCompleted;
    }

    public boolean isAppLimited() {
        return isAppLimited;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUsageLimitModel that = (AppUsageLimitModel) o;
        return timeLimitPerDay == that.timeLimitPerDay &&
                timeLimitPerHour == that.timeLimitPerHour &&
                timeCompleted == that.timeCompleted &&
                isAppLimited == that.isAppLimited &&
                Objects.equals(appName, that.appName) &&
                Objects.equals(packageName, that.packageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appName, packageName, timeLimitPerDay, timeLimitPerHour, timeCompleted, isAppLimited);
    }
}
