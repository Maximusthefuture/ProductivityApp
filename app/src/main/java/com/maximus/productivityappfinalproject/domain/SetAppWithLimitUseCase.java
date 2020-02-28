package com.maximus.productivityappfinalproject.domain;

import android.content.Context;
import android.widget.Toast;

import com.maximus.productivityappfinalproject.R;
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

    public void checkUserInput(int perDayHours, int perDayMinutes, int perHourMinutes) {
        if (perDayHours > 24) {
            Toast.makeText(mContext ,mContext.getString(R.string.day_24_hours), Toast.LENGTH_LONG).show();
            return;
        }
        if (perDayMinutes > 60) {
//             msg = mContext.getString(R.string.hour_60_minutes);
             return;
        }
        if (perHourMinutes > 60) {
//            msg =  mContext.getString(R.string.hour_60_minutes);
            return;
        }
        if (perDayHours == 0 && perDayMinutes == 0 && perHourMinutes == 0) {
//            msg = mContext.getString(R.string.you_are_not_ready);
            return;
        }
    }
}
