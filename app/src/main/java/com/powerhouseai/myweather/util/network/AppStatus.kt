package com.powerhouseai.myweather.util.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

enum class AppStatus {
    NONE,
    WIFI,
    MOBILE,
    OTHER,
    MAYBE;

    companion object {

        fun checkConnectivity(context: Context): AppStatus {

            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            cm.run {
                getNetworkCapabilities(activeNetwork)?.run {
                    return when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> WIFI
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> MOBILE
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> OTHER
                        hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> MAYBE
                        else -> NONE
                    }
                }
            }
            return NONE
        }

    }

}