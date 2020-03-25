package com.maximus.productivityappfinalproject.data.prefs

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManagerImpl(context: Context, prefFileName: String) : SharedPrefManager {

    private val mSharedPreferences: SharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_KEY_CLOSEST_HOUR = "PREF_KEY_CLOSEST_HOUR"
        private const val PREF_KEY_CLOSEST_DAY = "PREF_KEY_CLOSEST_DAY"
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


}