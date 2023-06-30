package com.powerhouseai.myweather

import android.app.Application
import com.powerhouseai.myweather.util.work.Sync
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyWeatherApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        Sync.initialize(context = this)
    }

    companion object {
        lateinit var instance: MyWeatherApp private set
    }
}