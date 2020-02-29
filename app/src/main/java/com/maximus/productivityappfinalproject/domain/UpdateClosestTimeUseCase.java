package com.maximus.productivityappfinalproject.domain;

import com.maximus.productivityappfinalproject.utils.Utils;

public class UpdateClosestTimeUseCase {

    private SetClosestTimeUseCase mSetClosestTimeUseCase;

    public UpdateClosestTimeUseCase(SetClosestTimeUseCase setClosestTimeUseCase) {

        mSetClosestTimeUseCase = setClosestTimeUseCase;
    }

    public void updateClosestHour() {
        long hour = Utils.getComingHour();
        mSetClosestTimeUseCase.setClosestHour(hour);
    }

    public void updateClosestDay() {
        long day = Utils.getComingDay();
        mSetClosestTimeUseCase.setClosestDay(day);
    }

}
