package com.maximus.productivityappfinalproject.domain.model;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//TODO builder pattern?
public class AppsModel implements Parcelable {

    private String mPackageName;
    private String mAppName;
    private Drawable mAppIcon;
    private long mAppUsageTime;
    private long mEventTime;
    private int mEventType;
    private String mLastTimeUsed;
    private int mCount;
    private boolean mSelected;

    public AppsModel(int count) {
        mCount = count;
    }

    public AppsModel(long appUsageTime) {
        mAppUsageTime = appUsageTime;
    }

    public AppsModel() {
    }

    public boolean isSelected() {
        return mSelected;
    }

    public AppsModel(long eventTime, long usageTime) {
        mEventTime = eventTime;
        mAppUsageTime = usageTime;
    }

    public AppsModel(long appUsageTime, long eventTime, int eventType) {
        mAppUsageTime = appUsageTime;
        mEventTime = eventTime;
        mEventType = eventType;
    }

    public void setEventType(int eventType) {
        mEventType = eventType;

    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }

    public long getEventTime() {
        return mEventTime;
    }

    public int getEventType() {
        return mEventType;
    }


    public AppsModel(String packageName) {
        mPackageName = packageName;
    }


    public AppsModel(String packageName, String appName) {
        mPackageName = packageName;
        mAppName = appName;
    }


    public AppsModel(String packageName, String appName, Drawable appIcon, String lastTimeUsed, long appUsageTime) {
        mAppName = appName;
        mAppIcon = appIcon;
        mLastTimeUsed = lastTimeUsed;
        mAppUsageTime = appUsageTime;
        mPackageName = packageName;
    }

    public AppsModel(String packageName, String appName, Drawable appIcon, long appUsageTime, String lastTimeUsed, boolean selected) {
        mPackageName = packageName;
        mAppName = appName;
        mAppIcon = appIcon;
        mAppUsageTime = appUsageTime;
        mLastTimeUsed = lastTimeUsed;
        mSelected = selected;
    }

    protected AppsModel(Parcel in) {
        mAppName = in.readString();
        mAppUsageTime = in.readLong();
        mLastTimeUsed = in.readString();
    }

    public void setCount(int count) {
        mCount = count;
    }

    public String getAppName() {
        return mAppName;
    }

    public Drawable getAppIcon() {
        return mAppIcon;
    }

    public long getAppUsageTime() {
        return mAppUsageTime;
    }


    public String getLastTimeUsed() {
        return mLastTimeUsed;
    }

    @NonNull
    public String getPackageName() {
        return mPackageName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAppName);
        dest.writeLong(mAppUsageTime);

        dest.writeString(mLastTimeUsed);
        dest.writeString(mPackageName);
    }

    public static final Creator<AppsModel> CREATOR = new Creator<AppsModel>() {
        @Override
        public AppsModel createFromParcel(Parcel in) {
            return new AppsModel(in);
        }

        @Override
        public AppsModel[] newArray(int size) {
            return new AppsModel[size];
        }
    };

    @Override
    public String toString() {
        return "AppsModel{" +
                "mAppName='" + mAppName + '\'' +
                ", mAppIcon=" + mAppIcon +
                ", mAppUsageTime=" + mAppUsageTime +
                ", mLastTimeUsed='" + mLastTimeUsed + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
