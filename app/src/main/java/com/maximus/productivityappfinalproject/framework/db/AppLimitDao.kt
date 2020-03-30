package com.maximus.productivityappfinalproject.framework.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel
import io.reactivex.Flowable

@Dao
interface AppLimitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAppLimit(limitApp: AppUsageLimitModel)

    @Query("SELECT * FROM app_limit_table")
    fun getUsageLimitList(): List<AppUsageLimitModel>

    @Query("DELETE FROM app_limit_table WHERE packageName =:packageName")
    fun deleteAppLimit(packageName: String)
}