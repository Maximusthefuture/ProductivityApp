package com.maximus.productivityappfinalproject.presentation.ui;

import android.content.Intent;
import android.os.Bundle;


import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.service.NotificationService;
import com.maximus.productivityappfinalproject.utils.MyPreferenceManager;


public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SettingsFragment";


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        Preference preference = findPreference(getString(R.string.show_notification_key));
        MyPreferenceManager.init(requireContext());



        preference.setOnPreferenceChangeListener((preference1, newValue) -> {
            Intent intent = new Intent(getContext(), NotificationService.class);

            if (newValue.equals(true)) {
                MyPreferenceManager.getInstance().putBoolean(getString(R.string.show_notification_key), true);
                getContext().startService(intent);
                return true;
            } else {
                MyPreferenceManager.getInstance().putBoolean(getString(R.string.show_notification_key), false);
                getContext().stopService(intent);
                return true;
            }
//                Log.d(TAG, "onPreferenceChange: " + newValue);
//                return true;
        });

    }

}
