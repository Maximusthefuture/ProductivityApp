package com.maximus.productivityappfinalproject.domain

import com.maximus.productivityappfinalproject.utils.Utils
import javax.inject.Inject

class UpdateClosestTimeUseCaseKt @Inject constructor(
        private var setClosestTimeUseCase: SetClosestTimeUseCase) {

    fun updateClosestHour() {
        var hour = Utils.getComingHour()
        setClosestTimeUseCase.setClosestHour(hour)
    }

    fun updateClosestDay() {
        var day = Utils.getComingDay()
        setClosestTimeUseCase.setClosestDay(day)
    }
}