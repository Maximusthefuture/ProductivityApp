package com.maximus.productivityappfinalproject.framework.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.maximus.productivityappfinalproject.domain.model.PhoneUsage;

import java.util.List;

@Dao
public interface PhoneUsageDao {

    @Query("SELECT * FROM phone_usage_table WHERE usage_count =:count")
    int getUsageCount(int count);


    @Query("SELECT usage_count FROM phone_usage_table")
    int getUsageCount();

    @Query("SELECT * FROM phone_usage_table")
    List<PhoneUsage> getPhoneUsageData();

//    @Query("SELECT * FROM phone_usage WHERE usage_time =:usageTime")
//    String getUsageTime(String usageTime);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPhoneUsage(PhoneUsage phoneUsage);

    @Update
    void updatePhoneUsage(PhoneUsage phoneUsage);

    @Query("DELETE  FROM phone_usage_table ")
    void deletePhoneUsage();
}