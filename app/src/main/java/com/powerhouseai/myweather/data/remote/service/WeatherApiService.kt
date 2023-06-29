package com.powerhouseai.myweather.data.remote.service

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    fun getWeatherByLatLong(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
    )

    @GET("weather")
    fun getWeatherByCityName(
        @Query("q") city: String,
    )
}