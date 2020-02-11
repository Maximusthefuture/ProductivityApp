package com.maximus.productivityappfinalproject.data.db;

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

//    @Insert
//    void insertIgnoreItem(IgnoreItems ignoreItems);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAppItem(IgnoreItems appsModel);

    @Update
    void updateIgnoreItem(IgnoreItems ignoreItems);

    @Query("DELETE FROM ignore_table")
    void deleteAllItems();

    @Query("DELETE FROM ignore_table WHERE package_name =:packageName")
    int deleteByPackageName(String packageName);
}
