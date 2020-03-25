package com.maximus.productivityappfinalproject.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhoneUsageKt(
        @ColumnInfo
        @PrimaryKey
        val packageName: String,
        @ColumnInfo
        val usageCount: Int,
        @ColumnInfo
        val usageTime: Int,
        @ColumnInfo
        val timeCompletedInHour: Long,
        @ColumnInfo
        val timeCompletedInDay: Long
)
