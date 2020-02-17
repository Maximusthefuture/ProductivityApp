package com.maximus.productivityappfinalproject.domain.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
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


    public IgnoreItems(String packageName, long created, String name) {
        mPackageName = packageName;
        mCreated = created;
        mName = name;
    }
    @Ignore
    public IgnoreItems() {
    }

    @Ignore
    public IgnoreItems(@NonNull String packageName) {
        mPackageName = packageName;
    }
    @Ignore
    public IgnoreItems(@NonNull String packageName, String name) {
        mPackageName = packageName;
        mName = name;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public long getCreated() {
        return mCreated;
    }

    public String getName() {
        return mName;
    }
}
