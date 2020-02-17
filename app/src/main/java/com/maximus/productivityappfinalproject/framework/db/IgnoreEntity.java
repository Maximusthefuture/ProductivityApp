package com.maximus.productivityappfinalproject.framework.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "ignore_table")
public class IgnoreEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "package_name")
    private String mPackageName;
    @ColumnInfo(name = "created")
    private long mCreated;
    @ColumnInfo(name = "name")
    private String mName;


    public IgnoreEntity(String packageName, long created, String name) {
        mPackageName = packageName;
        mCreated = created;
        mName = name;
    }

    @Ignore
    public IgnoreEntity(@NonNull String packageName) {
        mPackageName = packageName;
    }
    @Ignore
    public IgnoreEntity(@NonNull String packageName, String name) {
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

