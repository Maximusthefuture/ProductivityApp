package com.maximus.productivityappfinalproject.di

import android.content.Context
import androidx.room.Room
import com.maximus.productivityappfinalproject.framework.db.AppsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideRoomDb(context: Context): AppsDatabase {
        return Room.databaseBuilder(context.applicationContext,
                AppsDatabase::class.java, "Apps.db")
                .fallbackToDestructiveMigration()
                
                .build()
    }
}