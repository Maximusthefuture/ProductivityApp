package com.maximus.productivityappfinalproject.framework.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;

import java.util.List;

@Dao
public interface AppLimitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAppLimit(AppUsageLimitModel limitModel);

    @Query("SELECT * FROM app_limit_table")
    List<AppUsageLimitModel> getUsageLimitList();

    @Query("DELETE FROM app_limit_table WHERE packageName =:packageName")
    void deleteAppLimit(String packageName);
}
