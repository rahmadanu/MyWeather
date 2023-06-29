package com.powerhouseai.myweather.data.remote.datasource

import com.powerhouseai.myweather.data.model.request.WeatherCityNameRequest
import com.powerhouseai.myweather.data.model.request.WeatherLatLonRequest
import com.powerhouseai.myweather.data.model.response.WeatherResponse
import com.powerhouseai.myweather.data.remote.service.WeatherApiService
import retrofit2.http.GET
import javax.inject.Inject

interface WeatherRemoteDataSource {

    suspend fun getWeatherByLatLon(latLongRequest: WeatherLatLonRequest): WeatherResponse

    suspend fun getWeatherByCityName(cityNameRequest: WeatherCityNameRequest): WeatherResponse
}

class WeatherRemoteDataSourceImpl @Inject constructor(private val apiService: WeatherApiService): WeatherRemoteDataSource {
    override suspend fun getWeatherByLatLon(latLongRequest: WeatherLatLonRequest): WeatherResponse {
        return apiService.getWeatherByLatLon(latLongRequest)
    }

    override suspend fun getWeatherByCityName(cityNameRequest: WeatherCityNameRequest): WeatherResponse {
        return apiService.getWeatherByCityName(cityNameRequest)
    }

}