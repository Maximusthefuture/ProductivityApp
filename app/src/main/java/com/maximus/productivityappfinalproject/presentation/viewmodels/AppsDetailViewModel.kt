package com.maximus.productivityappfinalproject.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maximus.productivityappfinalproject.domain.GetAppIntervalUseCase
import com.maximus.productivityappfinalproject.domain.model.AppsModel
import com.maximus.productivityappfinalproject.utils.IntervalEnum
import javax.inject.Inject

class AppsDetailViewModel @Inject constructor(
      private val getAppIntervalUseCase: GetAppIntervalUseCase
): ViewModel() {


    private val _apps = MutableLiveData<AppsModel>()
    val apps: LiveData<AppsModel>
        get() = _apps

    private val _dayInterval = MutableLiveData<Int>()
    val dayInterval: LiveData<Int>
        get() = _dayInterval

    init {
        setFiltering(IntervalEnum.TODAY)
    }

    fun intervalList(packageName: String): List<AppsModel> {
        return getAppIntervalUseCase.getAppUsedInterval(packageName, _dayInterval.value!!)
    }


    fun setFiltering(intervalEnum: IntervalEnum) {
        when(intervalEnum) {
            IntervalEnum.TODAY ->  {
                _dayInterval.value = 0

            }
            IntervalEnum.YESTERDAY -> _dayInterval.value = 1
            IntervalEnum.THIS_WEEK -> _dayInterval.value = 2
        }
    }







}