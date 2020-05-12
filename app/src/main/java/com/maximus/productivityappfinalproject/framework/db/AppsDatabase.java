package com.maximus.productivityappfinalproject.framework.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;
import com.maximus.productivityappfinalproject.domain.model.LimitedApps;
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {LimitedApps.class, PhoneUsage.class, AppUsageLimitModel.class}, version = 14, exportSchema = false)
public abstract class AppsDatabase extends RoomDatabase {
    private static AppsDatabase INSTANCE;

    public abstract LimitedDao ignoreDao();
    public abstract PhoneUsageDao phoneUsageDao();
    public abstract AppLimitDao appLimitDao();
    public static final ExecutorService databaseWriterExecutor
            = Executors.newFixedThreadPool(5);

}
