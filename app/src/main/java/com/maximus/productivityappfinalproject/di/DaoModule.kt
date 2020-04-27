package com.maximus.productivityappfinalproject.di

import com.maximus.productivityappfinalproject.data.*
import com.maximus.productivityappfinalproject.data.prefs.SharedPrefManager
import com.maximus.productivityappfinalproject.data.prefs.SharedPrefManagerImpl
import com.maximus.productivityappfinalproject.framework.AppLimitDataSourceImpl
import com.maximus.productivityappfinalproject.framework.IgnoreAppDataSourceImp
import com.maximus.productivityappfinalproject.framework.PhoneUsageDataSourceImp
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DaoModule {

    @Binds
    @Singleton
    abstract fun providePhoneUsageDataSource(
            phoneUsageDataSourceImp: PhoneUsageDataSourceImp): PhoneUsageDataSource

    @Binds
    @Singleton
    abstract fun provideAppLimitDataSource(
            appLimitDataSourceImpl: AppLimitDataSourceImpl): AppLimitDataSource


    @Binds
    @Singleton
    abstract fun provideSharedPrefManager(
            sharedPrefManagerImpl: SharedPrefManagerImpl): SharedPrefManager

    @Binds
    @Singleton
    abstract fun provideIgnoreDataSource(
            ignoreAppDataSourceImp: IgnoreAppDataSourceImp):IgnoreAppDataSource

    @Binds
    abstract fun provideRepository(appsRepositoryImpl: AppsRepositoryImpl):AppsRepository
}