package com.maximus.productivityappfinalproject.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManagerImpl implements SharedPrefManager {
    private static final String PREF_KEY_CLOSEST_HOUR = "PREF_KEY_CLOSEST_HOUR";
    private static final String PREF_KEY_CLOSEST_DAY = "PREF_KEY_CLOSEST_DAY";

    private SharedPreferences mSharedPreferences;

    public SharedPrefManagerImpl(Context context, String prefFileName) {
       mSharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public void setClosestHour(long hour) {
        mSharedPreferences.edit().putLong(PREF_KEY_CLOSEST_HOUR, hour).apply();
    }

    @Override
    public Long getClosestHour() {
        return mSharedPreferences.getLong(PREF_KEY_CLOSEST_HOUR, 0);
    }

    @Override
    public void setClosestDay(long day) {
        mSharedPreferences.edit().putLong(PREF_KEY_CLOSEST_DAY, day).apply();
    }

    @Override
    public Long getClosetDay() {
        return mSharedPreferences.getLong(PREF_KEY_CLOSEST_DAY, 0);
    }
}
