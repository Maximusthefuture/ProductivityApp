package com.maximus.productivityappfinalproject.utils;

import android.app.AppOpsManager;
import android.content.Context;

import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel;

import java.util.Calendar;

public class Utils {

    public static float getMinutes(long millis) {
        float seconds = millis / 1000;
        float minutes = seconds / 60;
        return minutes;
    }

    public static float getHours(long millis) {
        float seconds = millis / 1000;
        float minutes = seconds / 60;
        float hours = (minutes / 60);
        return hours;
    }

    public static String formatMillisToSeconds(long millis) {
        long second = millis / 1000L;
        if (second < 60) {
            return String.format("%ss", second);
        } else if (second < 60 * 60) {
            return String.format("%sm %ss", second / 60, second % 60);
        } else {
            return String.format(("%sh %sm %ss"), second / 3600, second % (3600) / 60, second % (3600) % 60);
        }
    }

    public static long[] getYesterday() {
        long timeNow = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeNow - (86400 * 1000));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long start = calendar.getTimeInMillis();
        long end = Math.min(start + (86400 * 1000), timeNow);
        return new long[]{start, end};
    }

    public static long[] getInterval(IntervalEnum intervalEnum) {
        long[] interval;
        switch (intervalEnum) {
            case TODAY:
                interval = getToday();
                break;
            case YESTERDAY:
                interval = getYesterday();
                break;
            case THIS_WEEK:
                interval = getThisWeek();
                break;
            default:
                interval = getToday();
        }
        return interval;
    }

    private static long[] getToday() {
        long timeNow = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new long[]{calendar.getTimeInMillis(), timeNow};
    }

    private static long[] getThisWeek() {
        long timeNow = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long start = cal.getTimeInMillis();
        Utils.formatMillisToSeconds(start);
        return new long[]{start, timeNow};

    }

    public static boolean isTimeBeforeBad(AppUsageLimitModel appUsageLimitModel, int hourBeforeBed, int minutesBeforeBed, int hourAtMorning, int minutesAtMorning) {
        if (System.currentTimeMillis() > restrictAccessBeforeBed(appUsageLimitModel, hourBeforeBed, minutesBeforeBed)
                && System.currentTimeMillis() < allowAccessAtMorning(appUsageLimitModel, hourAtMorning, minutesAtMorning)) {  //  && System.currentTimeMillis() < allowAccessAtMorning(appUsageLimitModel)
            return true;
        }
        return false;
    }



    private static long restrictAccessBeforeBed(AppUsageLimitModel appUsageLimitModel, int hour, int minutes) {
        long time = 0;
        if (appUsageLimitModel.isAppLimited()) {
            Calendar timeBeforeBed = Calendar.getInstance();
            timeBeforeBed.set(Calendar.HOUR_OF_DAY, hour);
            timeBeforeBed.set(Calendar.MINUTE, minutes);
            timeBeforeBed.set(Calendar.SECOND, 0);
            time = timeBeforeBed.getTimeInMillis();
        }

        return time;
    }

    private static long allowAccessAtMorning(AppUsageLimitModel appUsageLimitModel, int hour, int minutes) {
        long time = 0;
        if (appUsageLimitModel.isAppLimited()) {
            Calendar morningTime = Calendar.getInstance();
            int dayOfMonth = morningTime.get(Calendar.DAY_OF_WEEK);
            dayOfMonth++;
            morningTime.set(Calendar.DAY_OF_WEEK, dayOfMonth);
            morningTime.set(Calendar.HOUR_OF_DAY, hour);
            morningTime.set(Calendar.MINUTE, minutes);
            morningTime.set(Calendar.SECOND, 0);
            time = morningTime.getTimeInMillis();
        }
        return time;
    }

    public static boolean hasPermission(Context context) {
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        if (appOpsManager != null) {
            int mode = appOpsManager.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), context.getPackageName());
            return mode == AppOpsManager.MODE_ALLOWED;
        }
        return false;
    }

    public static long getComingHour() {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        hour++;
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static long getComingDay() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        return calendar.getTimeInMillis();
    }


}
