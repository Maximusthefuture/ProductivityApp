package com.maximus.productivityappfinalproject.domain;

import android.util.Log;

import com.maximus.productivityappfinalproject.utils.Utils;

public class UpdateClosestTimeUseCase {

    private SetClosestTimeUseCase mSetClosestTimeUseCase;
    private static final String TAG = "UpdateClosestTimeUseCas";

    public UpdateClosestTimeUseCase(SetClosestTimeUseCase setClosestTimeUseCase) {

        mSetClosestTimeUseCase = setClosestTimeUseCase;
    }

    public void updateClosestHour() {
        long hour = Utils.getComingHour();
        Log.d(TAG, "updateClosestHour: " + hour);
        mSetClosestTimeUseCase.setClosestHour(hour);
    }

    public void updateClosestDay() {
        long day = Utils.getComingDay();
        mSetClosestTimeUseCase.setClosestDay(day);
    }

}
