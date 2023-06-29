package com.powerhouseai.myweather.data.remote.service

import com.powerhouseai.myweather.data.model.request.WeatherCityNameRequest
import com.powerhouseai.myweather.data.model.request.WeatherLatLonRequest
import com.powerhouseai.myweather.data.model.response.WeatherResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    suspend fun getWeatherByLatLon(
        @Body latLonRequest: WeatherLatLonRequest
    ): WeatherResponse

    @GET("weather")
    suspend fun getWeatherByCityName(
        @Body cityNameRequest: WeatherCityNameRequest
    ): WeatherResponse
}