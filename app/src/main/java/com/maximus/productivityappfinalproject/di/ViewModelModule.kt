package com.maximus.productivityappfinalproject.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maximus.productivityappfinalproject.ViewModelFactory
import com.maximus.productivityappfinalproject.data.AppsRepository
import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper
import com.maximus.productivityappfinalproject.domain.*
import com.maximus.productivityappfinalproject.presentation.viewmodels.AppsDetailViewModel
import com.maximus.productivityappfinalproject.presentation.viewmodels.AppsViewModel
import com.maximus.productivityappfinalproject.presentation.viewmodels.LimitedListViewModel
import com.maximus.productivityappfinalproject.presentation.viewmodels.UsageLimitViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class ViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(LimitedListViewModel::class)
    fun getLimitedListViewModel(appsRepository: AppsRepository, useCase: LimitedListUseCase, myUsageStatsManagerWrapper: MyUsageStatsManagerWrapper): ViewModel {
        return LimitedListViewModel(appsRepository, useCase, myUsageStatsManagerWrapper)
    }

    @Provides
    @IntoMap
    @ViewModelKey(AppsViewModel::class)
    fun getAppViewModel(limitedListUseCase: LimitedListUseCase, myUsageStatsManagerWrapper: MyUsageStatsManagerWrapper,
                        getAppsUseCase: GetAppsUseCase, searchAppUseCase: SearchAppUseCase): ViewModel {
        return AppsViewModel(limitedListUseCase, myUsageStatsManagerWrapper, getAppsUseCase, searchAppUseCase)
    }

    @Provides
    @IntoMap
    @ViewModelKey(AppsDetailViewModel::class)
    fun getAppsDetailViewModel(getAppIntervalUseCase: GetAppIntervalUseCase): ViewModel {
        return AppsDetailViewModel(getAppIntervalUseCase)
    }

    @Provides
    @IntoMap
    @ViewModelKey(UsageLimitViewModel::class)
    fun getUsageLimitViewModel(setAppWithLimitUseCase: SetAppWithLimitUseCase): ViewModel {
        return UsageLimitViewModel(setAppWithLimitUseCase)
    }

    @Provides
    fun getViewModelFactory(map: Map<Class<out ViewModel>, @JvmSuppressWildcards ViewModel>): ViewModelProvider.Factory {
        return ViewModelFactory(map)
    }

}