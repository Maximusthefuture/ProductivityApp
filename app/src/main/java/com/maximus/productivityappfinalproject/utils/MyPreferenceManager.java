package com.maximus.productivityappfinalproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferenceManager {

    private static MyPreferenceManager sManager;
    private static SharedPreferences sPreferences;

    public static void init(Context context) {
        sManager = new MyPreferenceManager();
        sPreferences = context.getApplicationContext().getSharedPreferences("Prefs", 0);
    }

    public static MyPreferenceManager getInstance() {
        return sManager;
    }

    public void putBoolean(String key, boolean value) {
        sPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key) {
        return sPreferences.getBoolean(key, false);
    }
}
