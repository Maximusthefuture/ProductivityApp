package com.maximus.productivityappfinalproject.domain

import com.maximus.productivityappfinalproject.data.AppsRepository
import javax.inject.Inject

class ResetAppUseTimeDbUseCase @Inject constructor(private var appsRepository: AppsRepository){

    fun resetHourAppUsage() {
        appsRepository.resetHourly()
    }

    fun resetDayAppUsage() {
        appsRepository.resetDaily()
    }
}