package com.maximus.productivityappfinalproject

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.maximus.productivityappfinalproject.di.AppComponent
import com.maximus.productivityappfinalproject.di.DaggerAppComponent
import com.maximus.productivityappfinalproject.presentation.ui.Reminder
import java.util.*

class MyApplication: Application() {

    var alarmManager: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)


    }

    override fun onCreate() {
        super.onCreate()
//        alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmIntent = Intent(applicationContext, Reminder::class.java).let {
//            intent -> PendingIntent.getActivity(applicationContext, 0, intent, 0)
//        }
//        val calendar: Calendar = Calendar.getInstance().apply {
//            timeInMillis = System.currentTimeMillis()
//            set(Calendar.HOUR_OF_DAY, 17)
//            set(Calendar.MINUTE, 21)
//        }
//        alarmManager?.setInexactRepeating(
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis,
//                AlarmManager.INTERVAL_DAY,
//                alarmIntent
//
//        )

    }

}


