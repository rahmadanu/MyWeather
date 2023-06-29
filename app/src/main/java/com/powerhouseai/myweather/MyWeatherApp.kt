package com.powerhouseai.myweather

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyWeatherApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: MyWeatherApp private set
    }
}