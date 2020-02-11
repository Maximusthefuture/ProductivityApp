package com.maximus.productivityappfinalproject.data.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.maximus.productivityappfinalproject.domain.model.HistoryItems;
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {HistoryItems.class, IgnoreItems.class}, version = 6, exportSchema = false)
public abstract class AppsDatabase extends RoomDatabase {
    private static AppsDatabase INSTANCE;

    public abstract HistoryDao appDao();
    public abstract IgnoreDao ignoreDao();
    public static final ExecutorService datatbaseWriterExecutor
            = Executors.newFixedThreadPool(4);

    private static final Object sLock = new Object();

    public static AppsDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppsDatabase.class, "Apps.db")
                        .addCallback(sCallback)
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return INSTANCE;
        }
    }


    private static RoomDatabase.Callback sCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            datatbaseWriterExecutor.execute(() -> {
                IgnoreItems ignoreItems = new IgnoreItems("com.android.settings","Settings");
                IgnoreDao dao = INSTANCE.ignoreDao();
                dao.insertAppItem(ignoreItems);
            });
        }
    };
}
