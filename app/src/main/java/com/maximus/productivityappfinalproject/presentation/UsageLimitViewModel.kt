package com.maximus.productivityappfinalproject.presentation

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
    }

}
