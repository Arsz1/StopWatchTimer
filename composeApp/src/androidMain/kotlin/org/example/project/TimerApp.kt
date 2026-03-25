package org.example.project

import android.app.Application
import com.example.timer.di.initKoin

class TimerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Koin once at app startup
        initKoin(this)
    }
}