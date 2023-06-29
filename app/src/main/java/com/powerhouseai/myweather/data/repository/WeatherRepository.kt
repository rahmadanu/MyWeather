package com.powerhouseai.myweather.data.repository

import com.powerhouseai.myweather.data.model.request.WeatherCityNameRequest
import com.powerhouseai.myweather.data.model.request.WeatherLatLonRequest
import com.powerhouseai.myweather.data.model.response.WeatherResponse
import com.powerhouseai.myweather.data.remote.datasource.WeatherRemoteDataSource
import com.powerhouseai.myweather.util.wrapper.Resource
import com.powerhouseai.myweather.util.wrapper.StateHelper.proceed
import javax.inject.Inject

interface WeatherRepository {

    suspend fun getCurrentLocationWeather(lat: Double, lon: Double): Resource<WeatherResponse>

    suspend fun getWeatherByCityName(city: String): Resource<WeatherResponse>
}

class WeatherRepositoryImpl @Inject constructor(private val dataSource: WeatherRemoteDataSource): WeatherRepository {
    override suspend fun getCurrentLocationWeather(lat: Double, lon: Double): Resource<WeatherResponse> {
        return proceed {
            dataSource.getWeatherByLatLon(
                WeatherLatLonRequest(
                    latitude = lat,
                    longitude = lon,
                    mode = "",
                    units = "",
                    lang = ""
                )
            )
        }
    }

    override suspend fun getWeatherByCityName(city: String): Resource<WeatherResponse> {
        return proceed {
            dataSource.getWeatherByCityName(
                WeatherCityNameRequest(
                    city = city,
                    mode = "",
                    units = "",
                    lang = ""
                )
            )
        }
    }

}