package com.suvojeet.suvstudy

import android.app.Application
import com.suvojeet.suvstudy.di.appModule
import com.suvojeet.suvstudy.domain.manager.ReminderManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.android.ext.android.get

class SuvStudyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@SuvStudyApplication)
            modules(appModule)
        }

        val reminderManager: ReminderManager = get()
        reminderManager.scheduleDailyReminder()
    }
}
