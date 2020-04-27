package com.maximus.productivityappfinalproject.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maximus.productivityappfinalproject.ViewModelFactory
import com.maximus.productivityappfinalproject.data.AppsRepository
import com.maximus.productivityappfinalproject.device.MyUsageStatsManagerWrapper
import com.maximus.productivityappfinalproject.domain.GetAppsUseCase
import com.maximus.productivityappfinalproject.domain.LimitedListUseCase
import com.maximus.productivityappfinalproject.domain.SetAppWithLimitUseCase
import com.maximus.productivityappfinalproject.presentation.AppsViewModel
import com.maximus.productivityappfinalproject.presentation.LimitedListViewModel
import com.maximus.productivityappfinalproject.presentation.UsageLimitViewModel
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
    fun getAppViewModel(limitedListUseCase: LimitedListUseCase, myUsageStatsManagerWrapper: MyUsageStatsManagerWrapper, getAppsUseCase: GetAppsUseCase): ViewModel {
        return AppsViewModel(limitedListUseCase, myUsageStatsManagerWrapper, getAppsUseCase)
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