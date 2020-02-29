package com.maximus.productivityappfinalproject.domain;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl;
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;

public class SetAppWithLimitUseCase {
    private AppsRepositoryImpl mAppsRepository;
    private Context mContext;

    public SetAppWithLimitUseCase(AppsRepositoryImpl appsRepository, Context context) {
        mAppsRepository = appsRepository;
        mContext = context;
    }

    public void setLimit(String appPackageName, String appName,int perDayHours, int perDayMinutes, int perHourMinutes) {
        AppUsageLimitModel model;
        int timeAllowedPerDay;
        int timeAllowedPerHour;
        int millisToMinutes = 60 * 1000;
        if (perDayHours == 0 && perDayMinutes == 0) {
            timeAllowedPerDay = 900000000;
        } else {
            timeAllowedPerDay = perDayHours * 60 * millisToMinutes + perDayMinutes * millisToMinutes;
        }
        if (perHourMinutes != 0) {
            timeAllowedPerHour = perHourMinutes * millisToMinutes;
        } else {
            timeAllowedPerHour = 900000000;
        }

        model = new AppUsageLimitModel(appName, appPackageName, timeAllowedPerDay, timeAllowedPerHour, 0, true);

        mAppsRepository.addToLimit(model);
    }

    //TODO
    public TextWatcher checkInput(EditText inHour, EditText inDayHour, EditText inDayMinutes, Button materialButton) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int q = 0;
                int hour = 0;
                int minutes;
                int dayminutes;
                String w = String.valueOf(s);
                if (!w.isEmpty()) {
                    q = Integer.parseInt((w));
                }
                if (inHour.getText().hashCode() == s.hashCode()) {
                    hour = q;
                    if (hour > 60) {
                        Toast.makeText(mContext, " В часе 60 минут", Toast.LENGTH_SHORT).show();
                        materialButton.setEnabled(false);
                    } else if (hour < 60){
                        materialButton.setEnabled(true);
                    }
                } else
                if (inDayHour.getText().hashCode() == s.hashCode()) {
                    if (q > 24) {
                        Toast.makeText(mContext, "В дне 24 часа", Toast.LENGTH_SHORT).show();
                        materialButton.setEnabled(false);
                    } else if (q < 24 && hour < 60){
                        materialButton.setEnabled(true);
                    }

                } else
                if (inDayMinutes.getText().hashCode() == s.hashCode()) {
                    if (q > 60) {
                        Toast.makeText(mContext, "В часе 60 минут", Toast.LENGTH_SHORT).show();
                        materialButton.setEnabled(false);
                    } else if (q < 60 && hour < 60){
                        materialButton.setEnabled(true);
                    }
                    if (String.valueOf(s).startsWith("0")) {
                        Toast.makeText(mContext, "Должно начинаться с другой цифры", Toast.LENGTH_SHORT).show();
                        materialButton.setEnabled(false);
                    } else if (!String.valueOf(s).startsWith("0")) {
                        materialButton.setEnabled(true);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }
}
