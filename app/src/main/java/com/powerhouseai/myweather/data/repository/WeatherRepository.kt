package com.powerhouseai.myweather.data.repository

import android.content.Context
import com.powerhouseai.myweather.R
import com.powerhouseai.myweather.data.model.response.WeatherResponse
import com.powerhouseai.myweather.data.model.ui.WeatherUiModel
import com.powerhouseai.myweather.data.remote.datasource.WeatherRemoteDataSource
import com.powerhouseai.myweather.util.date.DateUtil
import com.powerhouseai.myweather.util.extension.Strings
import com.powerhouseai.myweather.util.wrapper.Resource
import com.powerhouseai.myweather.util.network.StateHelper.proceed
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface WeatherRepository {

    suspend fun getCurrentLocationWeather(latitude: Double, longitude: Double): Resource<WeatherUiModel>

    suspend fun getWeatherByCityName(city: String): Resource<WeatherResponse>
}

class WeatherRepositoryImpl @Inject constructor(
    private val dataSource: WeatherRemoteDataSource,
    @ApplicationContext private val context: Context,
): WeatherRepository {
    override suspend fun getCurrentLocationWeather(latitude: Double, longitude: Double): Resource<WeatherUiModel> {
        return proceed(context) {
            val weatherResponse = dataSource.getWeatherByLatLon(
                latitude = latitude,
                longitude = longitude,
                mode = "",
                units = "metric",
                language = ""
            )
            mapWeatherResponseToUiModel(weatherResponse)
        }
    }

    override suspend fun getWeatherByCityName(city: String): Resource<WeatherResponse> {
        return proceed(context) {
            dataSource.getWeatherByCityName(
                city = city,
                mode = "",
                units = "metric",
                language = ""
            )
        }
    }


    private fun mapWeatherResponseToUiModel(response: WeatherResponse): WeatherUiModel =
        WeatherUiModel(
            id = response.id,
            currentCity = response.name,
            weatherIcon = Strings.get(R.string.txt_weather_icon_url, response.weather?.get(0)?.icon.toString()),
            weatherDescription = Strings.get(R.string.txt_weather_description, response.weather?.get(0)?.main.toString(), response.weather?.get(0)?.description.toString()),
            todayDate = DateUtil.getCurrentDate(),
            temperature = Strings.get(R.string.txt_temperature, response.main?.temp ?: 0),
            temperatureFeelsLike = Strings.get(R.string.txt_temperature_feels_like, response.main?.feelsLike ?: 0),
            temperatureMinMax = Strings.get(R.string.txt_temperature_min_max, response.main?.tempMin ?: 0, response.main?.tempMax ?: 0),
            humidity = Strings.get(R.string.txt_humidity, response.main?.humidity ?: 0),
            clouds = Strings.get(R.string.txt_clouds, response.clouds?.all ?: 0),
            wind = Strings.get(R.string.txt_wind, response.wind?.speed ?: 0),
            pressure = Strings.get(R.string.txt_pressure, response.main?.pressure ?: 0),
            visibility = Strings.get(R.string.txt_visibility, response.visibility?.div(1000) ?: 0)
        )
}