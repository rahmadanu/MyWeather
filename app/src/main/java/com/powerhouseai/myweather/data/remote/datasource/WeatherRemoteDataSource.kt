package com.powerhouseai.myweather.data.remote.datasource

import com.powerhouseai.myweather.data.model.response.WeatherResponse
import com.powerhouseai.myweather.data.remote.service.WeatherApiService
import javax.inject.Inject

interface WeatherRemoteDataSource {

    suspend fun getWeatherByLatLon(
        latitude: Double,
        longitude: Double,
        mode: String,
        units: String,
        language: String,
    ): WeatherResponse

    suspend fun getWeatherByCityName(
        city: String,
        mode: String,
        units: String,
        language: String
    ): WeatherResponse
}

class WeatherRemoteDataSourceImpl @Inject constructor(private val apiService: WeatherApiService): WeatherRemoteDataSource {
    override suspend fun getWeatherByLatLon(
        latitude: Double,
        longitude: Double,
        mode: String,
        units: String,
        language: String,
    ): WeatherResponse {
        return apiService.getWeatherByLatLon(latitude, longitude, mode, units, language)
    }

    override suspend fun getWeatherByCityName(
        city: String,
        mode: String,
        units: String,
        language: String,
    ): WeatherResponse {
        return apiService.getWeatherByCityName(city, mode, units, language)
    }

}