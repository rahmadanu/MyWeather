package com.powerhouseai.myweather.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.powerhouseai.myweather.data.model.ui.WeatherUiModel

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWeather(weather: WeatherUiModel)

    @Query(
        value = """
            DELETE FROM weather
            WHERE id in (:ids)
        """,
    )
    suspend fun deleteWeather(ids: List<Int?>)

    @Query("SELECT * FROM weather")
    fun getWeather(): List<WeatherUiModel>
}