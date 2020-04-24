package com.maximus.productivityappfinalproject.data.prefs

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class SharedPrefManagerImpl @Inject constructor(context: Context) : SharedPrefManager {

    private val mSharedPreferences: SharedPreferences = context.getSharedPreferences("TimePrefs", Context.MODE_PRIVATE)

    companion object {
        private const val PREF_KEY_CLOSEST_HOUR = "PREF_KEY_CLOSEST_HOUR"
        private const val PREF_KEY_CLOSEST_DAY = "PREF_KEY_CLOSEST_DAY"
        private const val PREF_KEY_SELECTED_TIME = "PREF_KEY_SELECTED_TIME"
    }

    override fun setClosestHour(hour: Long) {
        mSharedPreferences.edit().putLong(PREF_KEY_CLOSEST_HOUR, hour).apply()
    }

    override fun getClosestHour(): Long {
        return mSharedPreferences.getLong(PREF_KEY_CLOSEST_HOUR, 0)
    }

    override fun setClosestDay(day: Long) {
       mSharedPreferences.edit().putLong(PREF_KEY_CLOSEST_DAY, day).apply()
    }

    override fun getClosestDay(): Long {
        return mSharedPreferences.getLong(PREF_KEY_CLOSEST_DAY, 0)
    }

    override fun isTimeLimitChanged(): Boolean {
        return mSharedPreferences.getBoolean(PREF_KEY_SELECTED_TIME, false)
    }

    override fun setTimeLimitChanged(bool: Boolean) {
       mSharedPreferences.edit().putBoolean(PREF_KEY_SELECTED_TIME, bool).apply()
    }


}