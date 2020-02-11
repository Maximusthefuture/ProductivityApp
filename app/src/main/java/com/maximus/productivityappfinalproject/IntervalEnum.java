package com.maximus.productivityappfinalproject;

public enum IntervalEnum {

    TODAY(0),
    YESTERDAY(1),
    THIS_WEEK(2);

    int mInterval;

    IntervalEnum(int interval) {
        mInterval = interval;
    }

    public static IntervalEnum getInterval(int interval) {
        switch (interval) {
            case 0:
                return IntervalEnum.TODAY;
            case 1:
                return IntervalEnum.YESTERDAY;
            case 2:
                return IntervalEnum.THIS_WEEK;

        }
        return IntervalEnum.TODAY;
    }

}
