package com.maximus.productivityappfinalproject.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maximus.productivityappfinalproject.data.AppsRepository
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl
import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper
import com.maximus.productivityappfinalproject.domain.LimitedListUseCase
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel
import com.maximus.productivityappfinalproject.domain.model.LimitedApps
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage
import com.maximus.productivityappfinalproject.utils.Utils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class LimitedListViewModel @Inject constructor(
        private var appsRepository: AppsRepository,
        private var limitedListUseCase: LimitedListUseCase,
        private var myUsageStatsManagerWrapper: MyUsageStatsManagerWrapper)
    : ViewModel() {

    val TAG = "LimitedListViewModel"

    @Inject
    lateinit var appRepositoryImpl: AppsRepositoryImpl
    var limitedApps = mutableListOf<LimitedApps>()
    private val mAllIgnoreItems = MutableLiveData<List<LimitedApps>>()
    val allIgnoreItems: LiveData<List<LimitedApps>>
        get() = mAllIgnoreItems;

    private val _time = MutableLiveData<String>()
    val time: LiveData<String>
        get() = _time;


    fun deleteAllLimitedItems() {
        limitedListUseCase.deleteAllLimitedItems();
    }

    fun getAllLimitedItems() {
        var d = limitedListUseCase.getLimitedList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onLimitedItemsFetched(it)
                }, {
                    Log.d(TAG, "OnError")
                })
    }

    private fun onLimitedItemsFetched(allLimitedApps: List<LimitedApps>) {
        limitedApps = ArrayList(allLimitedApps)
        mAllIgnoreItems.value = limitedApps
    }

    fun getRemainTime(packageName: String) {
        var d = Observable.zip(appRepositoryImpl.phoneUsage, appRepositoryImpl.limitObservable,
                BiFunction<List<PhoneUsage>, List<AppUsageLimitModel>, Long> { t1: List<PhoneUsage>, t2: List<AppUsageLimitModel> ->
                    getRemainTimeFromDb(t1, t2, packageName)
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it <= 0) {
                        _time.value = "Перерасход епта"
                    } else {
                        _time.value = Utils.formatMillisToSeconds(it)
                    }
                }, {
                    Log.d(TAG, it.localizedMessage)
                })
    }

    private fun getRemainTimeFromDb(phoneUsage: List<PhoneUsage>, appUsageLimitModel: List<AppUsageLimitModel>, packageName: String): Long {
        var phoneUsageTime: Long = 0
        var appUsage: Long = 0
        var result: Long = 0
        for (phoneModel in phoneUsage) {
            if (packageName == phoneModel.packageName) {
                phoneUsageTime = phoneModel.timeCompletedInHour
            }
        }

        for (appUsageModel in appUsageLimitModel) {
            if (packageName == appUsageModel.packageName) {
                appUsage = appUsageModel.timeLimitPerHour.toLong()
            }
        }

        result = appUsage - phoneUsageTime
        Log.d(TAG, "$result")

        return result
    }

}