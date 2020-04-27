package com.maximus.productivityappfinalproject.di


import android.content.Context
import com.maximus.productivityappfinalproject.presentation.ui.AppsFragment
import com.maximus.productivityappfinalproject.presentation.ui.TrackingListFragment
import com.maximus.productivityappfinalproject.service.CheckAppLaunchService
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DaoModule::class, RoomModule::class, ViewModelModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(checkAppLaunchService: CheckAppLaunchService)
    fun inject(limitedListFragment: TrackingListFragment)
    fun inject(appsFragment: AppsFragment)
}