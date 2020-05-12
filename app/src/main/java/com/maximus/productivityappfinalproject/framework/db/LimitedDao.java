package com.maximus.productivityappfinalproject.framework.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.maximus.productivityappfinalproject.domain.model.LimitedApps;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface IgnoreDao {

    @Query("SELECT * FROM ignore_table")
    Flowable<List<LimitedApps>> getIgnoreItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIgnoreItem(LimitedApps item);

    @Update
    void updateIgnoreItem(LimitedApps limitedApps);

    @Query("DELETE FROM ignore_table")
    void deleteAllItems();

    @Query("DELETE FROM ignore_table WHERE package_name =:packageName")
    int deleteByPackageName(String packageName);

    //TODO
    @Query("UPDATE ignore_table SET name=:usageTime")
    void updateUsageTimeAndLastUsed(long usageTime);


}
