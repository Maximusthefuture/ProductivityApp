package com.maximus.productivityappfinalproject.domain

import android.annotation.SuppressLint
import android.util.Log
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

//можно использовать юз кейсы вместе с другими юз кейсами
class GetAppWithLimitUseCase @Inject constructor(private var appsRepository: AppsRepositoryImpl) {

    private var currentTime: Long = 0
    private var prevApp: String
    private var noAPP: String = "NO_APP"

    var TAG = "GetAppWithLimitUseCase"

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

    @SuppressLint("CheckResult")
    fun getAppUsageLimit(packageName: String): AppUsageLimitModel {
        var appUsageLimitModel = AppUsageLimitModel()
        appsRepository.limitObservable
//                .subscribeOn(Schedulers.io())
                .flatMap { Observable.fromIterable(it) }
                .filter {
                    it.isAppLimited && it.packageName == packageName
                }
                .subscribe( {
                    appUsageLimitModel =
                            AppUsageLimitModel(it.appName, it.timeLimitPerDay, it.timeLimitPerHour, it.isAppLimited)
                }, {
                    Log.d(TAG, it.localizedMessage)
                })

        return appUsageLimitModel

    }

    @SuppressLint("CheckResult")
    fun getAppUsageData(appPackageName: String):PhoneUsage {
        var phoneUsage = PhoneUsage()
        appsRepository.phoneUsageFlowable
                .flatMap { Flowable.fromIterable(it) }
                .filter {
                    it.packageName == appPackageName
                }
                .subscribe({
                    phoneUsage = PhoneUsage(it.packageName, it.timeCompletedInHour, it.timeCompletedInDay)
                }, {
                    Log.e(TAG, it.localizedMessage)
                })
        return phoneUsage

    }



    private fun updateAppUsage(currentAppForeground: String, timeCompletedThisUse: Long) {
        var phoneUsage: PhoneUsage = PhoneUsage()
        var isFound = false
        var currentPackageName: String? = null
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
            appsRepository.insertPhoneUsage(phoneUsage)
        } else {
            phoneUsage = PhoneUsage(currentAppForeground, getTimeHour, getTimeDay)
            appsRepository.insertPhoneUsage(phoneUsage)
        }
    }

    //todo when no app is opened and user in home screen,
    // and then going in limited app, time is adding to prev app
    fun updateCurrentAppStats(currentAppForeground: String) {
        var currentTimeForeground = System.currentTimeMillis() - currentTime
        if (!prevApp.equals(currentAppForeground)) {
            if (!prevApp.equals(noAPP)) {
                Log.d(TAG, "usageUpdated to $prevApp for $currentTimeForeground")
//                prevApp?.let { updateAppUsage(it, currentTimeForeground) }
               updateAppUsage(prevApp, currentTimeForeground)
            }
            prevApp = currentAppForeground
            currentTime = System.currentTimeMillis()

        } else if (currentTimeForeground >= 15000) {
//            prevApp?.let{  updateAppUsage(it, currentTimeForeground)}
            updateAppUsage(prevApp, currentTimeForeground)
            Log.d(TAG, "usageUpdated to $prevApp for $currentTimeForeground")
            currentTime = System.currentTimeMillis()

        }
    }
}