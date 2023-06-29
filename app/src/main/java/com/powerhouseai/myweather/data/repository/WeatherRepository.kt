package com.powerhouseai.myweather.data.repository

import com.powerhouseai.myweather.data.model.response.WeatherResponse
import com.powerhouseai.myweather.data.remote.datasource.WeatherRemoteDataSource
import com.powerhouseai.myweather.util.wrapper.Resource
import com.powerhouseai.myweather.util.wrapper.StateHelper.proceed
import javax.inject.Inject

interface WeatherRepository {

    suspend fun getCurrentLocationWeather(latitude: Double, longitude: Double): Resource<WeatherResponse>

    suspend fun getWeatherByCityName(city: String): Resource<WeatherResponse>
}

class WeatherRepositoryImpl @Inject constructor(private val dataSource: WeatherRemoteDataSource): WeatherRepository {
    override suspend fun getCurrentLocationWeather(latitude: Double, longitude: Double): Resource<WeatherResponse> {
        return proceed {
            dataSource.getWeatherByLatLon(
                latitude = latitude,
                longitude = longitude,
                mode = "",
                units = "",
                language = ""
            )
        }
    }

    override suspend fun getWeatherByCityName(city: String): Resource<WeatherResponse> {
        return proceed {
            dataSource.getWeatherByCityName(
                city = city,
                mode = "",
                units = "",
                language = ""
            )
        }
    }

}