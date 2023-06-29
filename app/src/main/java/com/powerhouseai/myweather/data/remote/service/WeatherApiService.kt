package com.powerhouseai.myweather.data.remote.service

import com.powerhouseai.myweather.data.model.response.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    suspend fun getWeatherByLatLon(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("mode") mode: String = "",
        @Query("units") units: String = "",
        @Query("lang") language: String = "",
    ): WeatherResponse

    @GET("weather")
    suspend fun getWeatherByCityName(
        @Query("q") city: String,
        @Query("mode") mode: String = "",
        @Query("units") units: String = "",
        @Query("lang") language: String = "",
    ): WeatherResponse
}