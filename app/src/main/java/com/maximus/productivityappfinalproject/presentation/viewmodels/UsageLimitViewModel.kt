package com.maximus.productivityappfinalproject.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maximus.productivityappfinalproject.R
import com.maximus.productivityappfinalproject.domain.SetAppWithLimitUseCase
import javax.inject.Inject

class UsageLimitViewModel @Inject constructor(
        private val setAppWithLimitUseCase: SetAppWithLimitUseCase

) : ViewModel() {

    private val _snackBarText = MutableLiveData<Int>()
    val snackbar: LiveData<Int>
        get() = _snackBarText

    private val _chipText =
            MutableLiveData<String>().apply { value = "Лимит не установлен" }
    val chipText: LiveData<String>
        get() = _chipText

    fun setDailyLimit(appPackageName: String, appName: String, perDayHours: Int, perDayMinutes: Int) {
        setAppWithLimitUseCase.setLimit(appPackageName, appName, perDayHours, perDayMinutes, 0)
        _chipText.value = "Лимит установлен на $perDayHours ч $perDayMinutes мин"
        _snackBarText.value = R.string.limit_set
    }

    fun setHourlyLimit(appPackageName: String, appName: String, perHourMinutes: Int) {
        setAppWithLimitUseCase.setLimit(appPackageName, appName, 0, 0, perHourMinutes)
        _chipText.value =  "Лимит установлен на $perHourMinutes мин"
//        _chipText.value = getDataFromLimitedDb();
    }


//    fun showLimitedTime(list: List<AppUsageLimitModel>, mPackageName: String) {
////        getAppUsageLimitListFromDb.subscribe()...
////        .flatmap()
//        for (appUsageLimitModel in list) {
//            if (appUsageLimitModel.packageName == mPackageName) {
//                val timeLimitPerDay = appUsageLimitModel.timeLimitPerDay
//                val timeLimitPerHour = appUsageLimitModel.timeLimitPerHour
//                val timeCompleted = appUsageLimitModel.timeCompleted
//                if (timeLimitPerDay >= 90000000) { //TODO livedata?
//                    _chipText.value = "Лимит не установлен"
//                } else {
//                    _chipText.value = R.string.limit_in_day_chip_with_args
////
//                }
//                if (timeLimitPerHour >= 90000000) {
//                    _chipText.value = "Лимит не установлен"
//                } else {
//                    _chipText.value = R.string.hourly_limit_set_to
////                    mLimitHourlyChip.setText(mContext.getString(R.string.hourly_limit_set_to, Utils.formatMillisToSeconds(timeLimitPerHour.toLong())))
//                }
//
//            }
//            //            else {
////                mLimitDailyChip.setText("Лимит не установлен");
////                mLimitHourlyChip.setText("Лимит не установлен");
////            }
//        }
//    }


}
