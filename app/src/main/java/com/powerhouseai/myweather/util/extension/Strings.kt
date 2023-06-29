package com.powerhouseai.myweather.util.extension

import androidx.annotation.StringRes
import com.powerhouseai.myweather.MyWeatherApp

object Strings {

    fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
        return MyWeatherApp.instance.getString(stringRes, *formatArgs)
    }
}