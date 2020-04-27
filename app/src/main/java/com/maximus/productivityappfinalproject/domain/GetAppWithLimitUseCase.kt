package com.maximus.productivityappfinalproject.domain

import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase
import com.maximus.productivityappfinalproject.utils.Utils
import io.reactivex.schedulers.Schedulers
import java.util.function.Predicate
import javax.inject.Inject

//можно использовать юз кейсы вместе с другими юз кейсами
class GetAppWithLimitUseCase @Inject constructor(private var appsRepository: AppsRepositoryImpl) {

    private var currentTime: Long = 0
    private var prevApp: String
    private var noAPP: String = "NO_APP"

    var  TAG = "GetAppWithLimitUseCase"

    init {
        prevApp = noAPP
    }

    fun isLimitSet(packageName: String?): Boolean {
        var currentPackageName: String?
        val list = appsRepository.limitedItems
        for (appUsageLimitModel in list) {
            currentPackageName = appUsageLimitModel.packageName
            if (currentPackageName == packageName) {
                return true
            }
        }
        return false
    }

    fun getAppUsageLimitFromDB(packageName: String): AppUsageLimitModel {
        var appUsageLimitModel = AppUsageLimitModel(packageName, 0, 0, isLimitSet(packageName))

            var list = appsRepository.limitedItems
            for (limitModel in list) {
                if (limitModel.isAppLimited
                        && limitModel.packageName == packageName) {
                    appUsageLimitModel = AppUsageLimitModel(limitModel.appName,
                            limitModel.timeLimitPerDay, limitModel.timeLimitPerHour, limitModel.isAppLimited)
                }
        }
        return appUsageLimitModel
    }

    fun getAppUsageData(appPackageName: String): PhoneUsage {
        var phoneUsageModel = PhoneUsage()
        var currentPackageName: String = ""
        var timeUsedThisDay: Long = 0
        var timeUsedThisHour: Long = 0
        var list = appsRepository.phoneUsageData

        var isFound = false

        for (phoneUsage in list) {
            currentPackageName = phoneUsage.packageName
            if (currentPackageName == appPackageName) {
                isFound = true
                timeUsedThisDay = phoneUsage.timeCompletedInDay
                timeUsedThisHour = phoneUsage.timeCompletedInHour
            }
        }
        phoneUsageModel = if (isFound) {
            PhoneUsage(appPackageName, timeUsedThisHour, timeUsedThisDay)
        } else {
            PhoneUsage(currentPackageName, 0, 0)
        }

        return phoneUsageModel
    }

    private fun updateAppUsage(currentAppForeground: String, timeCompletedThisUse: Long) {
        var phoneUsage: PhoneUsage = PhoneUsage()
        var isFound = false
        var currentPackageName: String?= null
        var getTimeDay: Long = 0
        var getTimeHour: Long = 0
        var timeCompletedThisHour: Long = 0
        var timeCompletedThisDay: Long = 0

        var list = appsRepository.phoneUsageData
        //todo можно ли это сделать через rxjava?
        for (phoneUsageList in list) {
            currentPackageName = phoneUsageList.packageName
            if (currentPackageName == currentAppForeground) {
                isFound = true
                getTimeDay = phoneUsageList.timeCompletedInDay
                getTimeHour = phoneUsageList.timeCompletedInHour
            }
        }
        if (isFound) {
            timeCompletedThisHour = getTimeHour
            timeCompletedThisHour += timeCompletedThisUse
            timeCompletedThisDay = getTimeDay
            timeCompletedThisDay += timeCompletedThisUse
            phoneUsage = PhoneUsage(currentAppForeground, timeCompletedThisHour, timeCompletedThisDay)
            appsRepository.updatePhoneUsage(phoneUsage)
        } else {
            phoneUsage = PhoneUsage(currentAppForeground, getTimeHour, getTimeDay)
            appsRepository.insertPhoneUsage(phoneUsage)
        }
    }

    fun updateCurrentAppStats(currentAppForeground: String) {
        var currentTimeForeground = System.currentTimeMillis() - currentTime
        if (prevApp != currentAppForeground) {
            if (prevApp != noAPP) {
                updateAppUsage(prevApp, currentTimeForeground)
            }
            prevApp = currentAppForeground
            currentTime = System.currentTimeMillis()

        } else if(currentTimeForeground >= 15000) {
            updateAppUsage(prevApp, currentTimeForeground)
            currentTime = System.currentTimeMillis()

        }
    }
}