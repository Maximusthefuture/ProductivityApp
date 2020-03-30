package com.maximus.productivityappfinalproject.domain

import androidx.lifecycle.LiveData
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl
import com.maximus.productivityappfinalproject.domain.model.IgnoreItems

class LimitedListUseCase(private val mAppsRepositoryImpl: AppsRepositoryImpl) {

    fun addToIgnoreList(item: IgnoreItems) {
        mAppsRepositoryImpl.insertToIgnoreList(item)
    }

    fun deleteLimitedItem(packageName: String) {
        mAppsRepositoryImpl.deleteFromIgnoreList(packageName)
    }

    fun getLimitedList(): LiveData<List<IgnoreItems>> {
        return mAppsRepositoryImpl.ignoreItems
    }

    fun deleteAllLimitedItems() {
        mAppsRepositoryImpl.deleteAllIgnoreList()
    }
}