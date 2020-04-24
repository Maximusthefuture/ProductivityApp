package com.maximus.productivityappfinalproject.data.prefs

interface SharedPrefManager {
    fun setClosestHour(hour: Long)
    fun getClosestHour(): Long
    fun setClosestDay(day: Long)
    fun getClosestDay(): Long
    fun isTimeLimitChanged(): Boolean
    fun setTimeLimitChanged(bool: Boolean)
}