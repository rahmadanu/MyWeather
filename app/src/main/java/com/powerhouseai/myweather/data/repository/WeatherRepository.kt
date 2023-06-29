package com.powerhouseai.myweather.data.repository

import com.powerhouseai.myweather.data.model.request.WeatherCityNameRequest
import com.powerhouseai.myweather.data.model.request.WeatherLatLonRequest
import com.powerhouseai.myweather.data.model.response.WeatherResponse
import com.powerhouseai.myweather.data.remote.datasource.WeatherRemoteDataSource
import javax.inject.Inject

interface WeatherRepository {

    suspend fun getWeatherByLatLon(lat: Double, lon: Double): WeatherResponse

    suspend fun getWeatherByCityName(city: String): WeatherResponse
}

class WeatherRepositoryImpl @Inject constructor(private val dataSource: WeatherRemoteDataSource): WeatherRepository {
    override suspend fun getWeatherByLatLon(lat: Double, lon: Double): WeatherResponse {
        return dataSource.getWeatherByLatLon(
            WeatherLatLonRequest(
                latitude = lat,
                longitude = lon,
                mode = "",
                units = "",
                lang = ""
            )
        )
    }

    override suspend fun getWeatherByCityName(city: String): WeatherResponse {
        return dataSource.getWeatherByCityName(
            WeatherCityNameRequest(
                city = city,
                mode = "",
                units = "",
                lang = ""
            )
        )
    }

}