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
import kotlinx.coroutines.flow.last
import javax.inject.Inject

interface WeatherRepository : Syncable {
    suspend fun setCurrentLocation(latitude: Double, longitude: Double)
    suspend fun getCurrentLocationWeather(latitude: Double, longitude: Double): Resource<WeatherUiModel>
    suspend fun getCitiesWeather(): Resource<List<WeatherUiModel>>
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
                localDataSource.insertWeather(mapWeatherResponseToUiModel(weatherRemote, weatherRemote.id))

                mapWeatherResponseToUiModel(weatherRemote)
            } else {
                weatherLocal.first { it.currentLocationId != null }
            }
        }
    }

    override suspend fun getCitiesWeather(): Resource<List<WeatherUiModel>> {
        return proceedResource(context) {
            val weatherLocal = localDataSource.getWeather()
            val cities = localDataSource.getWeatherQueryList()

            if (weatherLocal.size < 2) {
                val result = mutableListOf<WeatherUiModel>()

                cities.forEach {
                    val citiesWeather = remoteDataSource.getWeatherByCityName(
                        city = it,
                        mode = "",
                        units = "metric",
                        language = ""
                    )
                    localDataSource.insertWeather(mapWeatherResponseToUiModel(citiesWeather))
                    result.add(mapWeatherResponseToUiModel(citiesWeather))
                }

                result
            } else {
                weatherLocal.filter { it.currentLocationId == null }
            }
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
                val cities = localDataSource.getWeatherQueryList()

                val currentLocationWeather = remoteDataSource.getWeatherByLatLon(
                    latLon.first().toDouble(),
                    latLon.last().toDouble(),
                    mode = "",
                    units = "metric",
                    language = ""
                )
                localDataSource.insertWeather(mapWeatherResponseToUiModel(currentLocationWeather, currentLocationWeather.id))

                cities.forEach {
                    val citiesWeather = remoteDataSource.getWeatherByCityName(
                        city = it,
                        mode = "",
                        units = "metric",
                        language = ""
                    )
                    localDataSource.insertWeather(mapWeatherResponseToUiModel(citiesWeather))
                }
            }
        )

    private fun mapWeatherResponseToUiModel(response: WeatherResponse, currentLocationId: Int? = null): WeatherUiModel =
        WeatherUiModel(
            id = response.id,
            currentLocationId = currentLocationId,
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
            countryCode = response.sys?.country,
            lastUpdatedOn = Strings.get(R.string.txt_last_updated_on, DateUtil.getLastUpdatedDate(response.dt))
        )
}