package com.powerhouseai.myweather.data.repository

import android.content.Context
import android.util.Log
import com.powerhouseai.myweather.R
import com.powerhouseai.myweather.data.local.datasource.WeatherLocalDataSource
import com.powerhouseai.myweather.data.model.response.WeatherResponse
import com.powerhouseai.myweather.data.model.ui.WeatherUiModel
import com.powerhouseai.myweather.data.remote.datasource.WeatherRemoteDataSource
import com.powerhouseai.myweather.util.date.DateUtil
import com.powerhouseai.myweather.util.extension.Strings
import com.powerhouseai.myweather.util.wrapper.Resource
import com.powerhouseai.myweather.util.network.StateHelper.proceedResource
import com.powerhouseai.myweather.util.work.Syncable
import com.powerhouseai.myweather.util.work.Synchronizer
import com.powerhouseai.myweather.util.work.syncWeather
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import javax.inject.Inject

interface WeatherRepository : Syncable {
    suspend fun setCurrentLocation(latitude: Double, longitude: Double)
    suspend fun getCurrentLocationWeather(latitude: Double, longitude: Double): Resource<WeatherUiModel>
    suspend fun getWeatherByCityName(city: String): Resource<WeatherResponse>
}

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource,
    @ApplicationContext private val context: Context,
): WeatherRepository {
    override suspend fun setCurrentLocation(latitude: Double, longitude: Double) {
        localDataSource.setCurrentLocation(setOf(latitude.toString(), longitude.toString()))
    }

    override suspend fun getCurrentLocationWeather(latitude: Double, longitude: Double): Resource<WeatherUiModel> {
        return proceedResource(context) {
            val weatherLocal = localDataSource.getWeather()

            if (weatherLocal.isEmpty()) {
                val weatherRemote = remoteDataSource.getWeatherByLatLon(
                    latitude,
                    longitude,
                    mode = "",
                    units = "metric",
                    language = ""
                )
                localDataSource.insertWeather(mapWeatherResponseToUiModel(weatherRemote))
                mapWeatherResponseToUiModel(weatherRemote)
            } else {
                weatherLocal[0]
            }
        }
    }

    override suspend fun getWeatherByCityName(city: String): Resource<WeatherResponse> {
        return proceedResource(context) {
            remoteDataSource.getWeatherByCityName(
                city = city,
                mode = "",
                units = "metric",
                language = ""
            )
        }
    }

    override suspend fun syncWith(
        synchronizer: Synchronizer
    ): Boolean =
        synchronizer.syncWeather(
            modelDeleter = {
                localDataSource.deleteWeather(localDataSource.getWeather().map { it.id })
            },
            modelUpdater = {
                val latLon = localDataSource.getCurrentLocation().last()

                val weatherRemote = remoteDataSource.getWeatherByLatLon(
                    latLon.first().toDouble(),
                    latLon.last().toDouble(),
                    mode = "",
                    units = "metric",
                    language = ""
                )

                localDataSource.insertWeather(mapWeatherResponseToUiModel(weatherRemote))
            }
        )

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
            visibility = Strings.get(R.string.txt_visibility, response.visibility?.div(1000) ?: 0),
            lastUpdatedOn = Strings.get(R.string.txt_last_updated_on, DateUtil.getLastUpdatedDate(response.dt))
        )
}