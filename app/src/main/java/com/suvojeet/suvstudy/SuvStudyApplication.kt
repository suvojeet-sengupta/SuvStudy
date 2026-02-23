package com.suvojeet.suvstudy

import android.app.Application
import com.suvojeet.suvstudy.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SuvStudyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@SuvStudyApplication)
            modules(appModule)
        }
    }
}
