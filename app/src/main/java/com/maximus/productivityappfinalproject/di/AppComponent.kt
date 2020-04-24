package com.maximus.productivityappfinalproject.di


import com.maximus.productivityappfinalproject.service.CheckAppLaunchService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, DbModule::class])
interface AppComponent {

//    @Component.Factory
//    interface Factory {
//        fun create(@BindsInstance context: Context): AppComponent
//    }

    fun inject(checkAppLaunchService: CheckAppLaunchService)
}