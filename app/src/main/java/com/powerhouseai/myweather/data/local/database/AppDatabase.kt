package com.powerhouseai.myweather.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.powerhouseai.myweather.data.model.response.WeatherResponse
import com.powerhouseai.myweather.data.model.ui.WeatherUiModel
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(entities = [WeatherUiModel::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object {
        private const val DB_NAME = "weather.db"

        @Volatile
        var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    val passphrase: ByteArray =
                        SQLiteDatabase.getBytes("weather-hashed".toCharArray())
                    val factory = SupportFactory(passphrase)

                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .openHelperFactory(factory)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}