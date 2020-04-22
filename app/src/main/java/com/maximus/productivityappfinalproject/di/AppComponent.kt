package com.maximus.productivityappfinalproject.di

import android.content.Context
import com.maximus.productivityappfinalproject.service.CheckAppLaunchService
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AppModule::class, ContextModule::class])
interface AppComponent {

//    @Component.Factory
//    interface Factory {
//        fun create(@BindsInstance context: Context): AppComponent
//    }

    fun inject(checkAppLaunchService: CheckAppLaunchService)
}