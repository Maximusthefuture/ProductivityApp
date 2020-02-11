package com.maximus.productivityappfinalproject;

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
        long end = start + (86400 * 1000) > timeNow ? timeNow : start + (86400 * 1000);
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
        long end = start + 86400 * 1000 > timeNow ? timeNow : start + 86400 * 1000;
        return new long[]{start, end};
    }


}
