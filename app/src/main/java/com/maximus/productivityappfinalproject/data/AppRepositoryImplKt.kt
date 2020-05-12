package com.maximus.productivityappfinalproject.data

import com.maximus.productivityappfinalproject.data.prefs.SharedPrefManager
import com.maximus.productivityappfinalproject.domain.model.AppUsageLimitModel
import com.maximus.productivityappfinalproject.domain.model.LimitedApps
import com.maximus.productivityappfinalproject.domain.model.PhoneUsage
import io.reactivex.Flowable
import io.reactivex.Observable

class AppRepositoryImplKt (
        val phoneUsageDataSource: PhoneUsageDataSource? = null,
        val appDataSource: AppLimitDataSource? = null,
        val sharedPrefManager: SharedPrefManager? = null,
        val limitedAppsDataSource: LimitedAppsDataSource? = null
): AppsRepository, ApiRepository{


    override fun updatePhoneUsage(phoneUsage: PhoneUsage?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getIgnoreItems(): Observable<MutableList<LimitedApps>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setClosestHour(hour: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeLimitedApp(packageName: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteFromIgnoreList(item: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLimitedItems(): MutableList<AppUsageLimitModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setClosestDay(day: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getClosestDay(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resetHourly() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deletePhoneUsage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getClosestHour(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertToIgnoreList(item: LimitedApps?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertPhoneUsage(phoneUsage: PhoneUsage?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addToLimit(app: AppUsageLimitModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAllIgnoreList() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resetDaily() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPhoneUsageData(): MutableList<PhoneUsage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPhraseFromApi(phrase: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}