package com.maximus.productivityappfinalproject.presentation.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.service.NotificationService;


public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SettingsFragment";


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        Preference preference = findPreference(getString(R.string.show_notification_key));

        preference.setOnPreferenceChangeListener((preference1, newValue) -> {
            Intent intent = new Intent(getContext(), NotificationService.class);
            if (newValue.equals(true)) {
                getContext().startService(intent);
                return true;
            } else {
                getContext().stopService(intent);
                return true;
            }
//                Log.d(TAG, "onPreferenceChange: " + newValue);
//                return true;
        });

    }

}
