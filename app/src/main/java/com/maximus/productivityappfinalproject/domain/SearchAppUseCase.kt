package com.maximus.productivityappfinalproject.domain

import android.annotation.SuppressLint
import com.maximus.productivityappfinalproject.domain.model.AppsModel
import com.maximus.productivityappfinalproject.presentation.AppRecyclerViewAdapter
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchAppUseCase @Inject constructor(
       private val getAppsUseCase: GetAppsUseCase) {


    @SuppressLint("CheckResult")
    private fun search(query: String?): List<AppsModel> {
        val list = mutableListOf<AppsModel>()
        getAppsUseCase.getAllAppsObservable(false, 0)
                .subscribeOn(Schedulers.newThread())
                .flatMap { Observable.fromIterable(it) }
                .filter { t -> t.appName.toLowerCase().contains(query!!) }
                .subscribe ({
                    list.add(AppsModel(it.packageName, it.appName, it.appIcon, it.lastTimeUsed, it.appUsageTime))
                }, {

                })
        return list
    }

    fun searchWithSearchView(search: Flowable<String>, mAdapter: AppRecyclerViewAdapter): Disposable {
        return search
                .subscribeOn(Schedulers.computation())
                .filter { s: String -> s.length >= 3 }
                .map{ s -> search(s.toLowerCase()) }
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list: List<AppsModel>? -> mAdapter.setList(list) }) { e: Throwable? -> }
    }

}