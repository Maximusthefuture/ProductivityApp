package com.maximus.productivityappfinalproject.framework.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.maximus.productivityappfinalproject.domain.model.HistoryItems;

import java.util.List;

@Dao
public interface HistoryDao {

    @Query("SELECT * FROM History")
    List<HistoryItems> getApps();

    @Query("SELECT * FROM History WHERE name = :appName")
    HistoryItems getAppByName(String appName);

    @Insert
    void insertApp(HistoryItems historyItems);

    @Update
    void updateApp(HistoryItems historyItems);

    @Query("DELETE FROM History")
    void deleteApps();
}
