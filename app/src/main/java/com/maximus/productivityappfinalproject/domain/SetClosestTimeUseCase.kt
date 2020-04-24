package com.maximus.productivityappfinalproject.domain

import com.maximus.productivityappfinalproject.data.AppsRepository
import javax.inject.Inject

class SetClosestTimeUseCase @Inject constructor(private var appsRepository: AppsRepository){

    fun setClosestHour(hour: Long) {
        appsRepository.closestHour = hour
    }

    fun setClosestDay(day: Long) {
        appsRepository.closestDay = day
    }

}