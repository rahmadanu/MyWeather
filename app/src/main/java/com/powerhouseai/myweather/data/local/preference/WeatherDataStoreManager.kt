package com.powerhouseai.myweather.data.local.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherDataStoreManager @Inject constructor(@ApplicationContext private val context: Context){

    suspend fun setCurrentLocation(latLon: Set<String>) {
        context.userDataStore.edit { preferences ->
            preferences[CURRENT_LOCATION] = latLon
        }
    }

    fun getCurrentLatitude(): Flow<Set<String>> {
        return context.userDataStore.data.map { preferences ->
            preferences[CURRENT_LOCATION] ?: setOf()
        }
    }

    companion object {
        private const val DATA_STORE_NAME = "weather_preferences"
        private val CURRENT_LOCATION = stringSetPreferencesKey("current_location")

        val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)
    }
}