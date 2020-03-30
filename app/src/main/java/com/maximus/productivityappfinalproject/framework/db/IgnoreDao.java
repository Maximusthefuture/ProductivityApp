package com.maximus.productivityappfinalproject.framework.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.maximus.productivityappfinalproject.domain.model.IgnoreItems;

import java.util.List;

@Dao
public interface IgnoreDao {

    @Query("SELECT * FROM ignore_table")
    List<IgnoreItems> getIgnoreItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIgnoreItem(IgnoreItems item);

    @Update
    void updateIgnoreItem(IgnoreItems ignoreItems);

    @Query("DELETE FROM ignore_table")
    void deleteAllItems();

    @Query("DELETE FROM ignore_table WHERE package_name =:packageName")
    int deleteByPackageName(String packageName);

    //TODO
    @Query("UPDATE ignore_table SET name=:usageTime")
    void updateUsageTimeAndLastUsed(long usageTime);


}
