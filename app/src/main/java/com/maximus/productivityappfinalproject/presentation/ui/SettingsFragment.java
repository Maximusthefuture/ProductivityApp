package com.maximus.productivityappfinalproject.presentation.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.maximus.productivityappfinalproject.R;
import com.maximus.productivityappfinalproject.service.CheckAppLaunchService;
import com.maximus.productivityappfinalproject.service.NotificationService;
import com.maximus.productivityappfinalproject.utils.MyPreferenceManager;


public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SettingsFragment";
    private NavController mNavController;



    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        Preference preferencef = findPreference(getString(R.string.show_time_preference_key));
        //TODO Add remaintime limited apps fragment
        if (preferencef != null) {
            preferencef.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    mNavController.navigate(R.id.apps_dest);
                    return true;
                }
            });
        }

        Preference preference = findPreference(getString(R.string.show_notification_key));
        MyPreferenceManager.init(requireContext());

        Preference preferenceNotification = findPreference(getString(R.string.notification_before_lock_key));
        //TODO 1
        preferenceNotification.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return false;
            }
        });

        preference.setOnPreferenceChangeListener((preference1, newValue) -> {
            Intent intent = new Intent(getContext(), CheckAppLaunchService.class);

            if (newValue.equals(true)) {
                MyPreferenceManager.getInstance().putBoolean(getString(R.string.show_notification_key), true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getContext().startForegroundService(intent);
                }
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
