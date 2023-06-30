package com.powerhouseai.myweather.data.local.datasource

import com.powerhouseai.myweather.data.local.database.WeatherDao
import com.powerhouseai.myweather.data.local.preference.WeatherDataStoreManager
import com.powerhouseai.myweather.data.model.ui.WeatherUiModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface WeatherLocalDataSource {
    fun getWeatherQueryList(): List<String>
    suspend fun insertWeather(weather: WeatherUiModel)
    suspend fun deleteWeather(ids: List<Int?>)
    fun getWeather(): List<WeatherUiModel>

    suspend fun setCurrentLocation(latLon: Set<String>)
    fun getCurrentLocation(): Flow<Set<String>>
}

class WeatherLocalDataSourceImpl @Inject constructor(
    private val dao: WeatherDao,
    private val dataStore: WeatherDataStoreManager
): WeatherLocalDataSource {
    override fun getWeatherQueryList(): List<String> {
        return listOf("New York", "Singapore", "Mumbai", "Delhi", "Sydney", "Melbourne")
    }

    override suspend fun insertWeather(weather: WeatherUiModel) {
        dao.insertWeather(weather)
    }

    override suspend fun deleteWeather(ids: List<Int?>) {
        dao.deleteWeather(ids)
    }

    override fun getWeather(): List<WeatherUiModel> {
        return dao.getWeather()
    }

    override suspend fun setCurrentLocation(latLon: Set<String>) {
        dataStore.setCurrentLocation(latLon)
    }

    override fun getCurrentLocation(): Flow<Set<String>> {
        return dataStore.getCurrentLatitude()
    }

}