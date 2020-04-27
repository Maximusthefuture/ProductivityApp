package com.maximus.productivityappfinalproject.domain

import androidx.lifecycle.LiveData
import com.maximus.productivityappfinalproject.data.AppsRepository
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl
import com.maximus.productivityappfinalproject.domain.model.LimitedApps
import io.reactivex.Flowable
import javax.inject.Inject

class LimitedListUseCase @Inject constructor(private val mAppsRepositoryImpl: AppsRepository) {

    fun addToIgnoreList(item: LimitedApps) {
        mAppsRepositoryImpl.insertToIgnoreList(item)
    }

    fun deleteLimitedItem(packageName: String) {
        mAppsRepositoryImpl.deleteFromIgnoreList(packageName)
    }

    fun getLimitedList(): Flowable<List<LimitedApps>> {
        return mAppsRepositoryImpl.ignoreItems
    }

    fun deleteAllLimitedItems() {
        mAppsRepositoryImpl.deleteAllIgnoreList()
    }
}