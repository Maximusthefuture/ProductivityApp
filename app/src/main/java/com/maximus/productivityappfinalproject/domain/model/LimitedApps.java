package com.maximus.productivityappfinalproject.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "ignore_table")
public class LimitedApps implements Parcelable {

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


    public LimitedApps(String packageName, long created, String name, String lastTimeUsed, long timeUsed) {
        mPackageName = packageName;
        mCreated = created;
        mName = name;
        mLastTimeUsed = lastTimeUsed;
        mTimeUsed = timeUsed;
    }

    @Ignore
    public LimitedApps(@NonNull String packageName) {
        mPackageName = packageName;
    }

    @Ignore
    public LimitedApps(@NonNull String packageName, String lastTimeUsed, long timeUsed) {
        mLastTimeUsed = lastTimeUsed;
        mPackageName = packageName;
        mTimeUsed = timeUsed;
    }
    @Ignore
    public LimitedApps(@NonNull String packageName, String name) {
        mPackageName = packageName;
        mName = name;
    }

    @Ignore
    public LimitedApps(@NonNull String packageName, String name, String lastTimeUsed, long timeUsed) {
        mPackageName = packageName;
        mName = name;
        mLastTimeUsed = lastTimeUsed;
        mTimeUsed = timeUsed;
    }

//    public void setTimeUsed(long timeUsed) {
//        mTimeUsed = timeUsed;
//    }

    protected LimitedApps(Parcel in) {
        mPackageName = in.readString();
        mCreated = in.readLong();
        mName = in.readString();
        mLastTimeUsed = in.readString();
        mTimeUsed = in.readLong();
    }

    public static final Creator<LimitedApps> CREATOR = new Creator<LimitedApps>() {
        @Override
        public LimitedApps createFromParcel(Parcel in) {
            return new LimitedApps(in);
        }

        @Override
        public LimitedApps[] newArray(int size) {
            return new LimitedApps[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPackageName);
        dest.writeLong(mCreated);
        dest.writeString(mName);
        dest.writeString(mLastTimeUsed);
        dest.writeLong(mTimeUsed);
    }
}
