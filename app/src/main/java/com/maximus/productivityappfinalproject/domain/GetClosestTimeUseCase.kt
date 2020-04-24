package com.maximus.productivityappfinalproject.domain

import com.maximus.productivityappfinalproject.data.AppsRepository
import javax.inject.Inject

class GetClosestTimeUseCase @Inject constructor(private var appsRepository: AppsRepository){


    fun getClosestHour(): Long {
        return appsRepository.closestHour
    }

    fun getClosestDay(): Long {
        return appsRepository.closestDay
    }
}