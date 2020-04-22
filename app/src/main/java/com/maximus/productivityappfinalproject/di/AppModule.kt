package com.maximus.productivityappfinalproject.di

import android.content.Context
import com.maximus.productivityappfinalproject.data.AppLimitDataSource
import com.maximus.productivityappfinalproject.data.AppsRepositoryImpl
import com.maximus.productivityappfinalproject.domain.GetAppWithLimitUseCase
import com.maximus.productivityappfinalproject.framework.AppLimitDataSourceImpl
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Provides

@Module
 class AppModule {


    @Provides
    fun provideLimitUseCase(repositoryImpl: AppsRepositoryImpl): GetAppWithLimitUseCase {
        return GetAppWithLimitUseCase(repositoryImpl);
    }

    @Provides
    fun provide(context: Context): AppLimitDataSource {
        return AppLimitDataSourceImpl(context);
    }

    @Provides
    fun provideRepository(appLimitDataSource: AppLimitDataSource): AppsRepositoryImpl {
        return AppsRepositoryImpl(appLimitDataSource);
    }



}