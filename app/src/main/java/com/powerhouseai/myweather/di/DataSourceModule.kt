package com.powerhouseai.myweather.di

import com.powerhouseai.myweather.data.local.datasource.WeatherLocalDataSource
import com.powerhouseai.myweather.data.local.datasource.WeatherLocalDataSourceImpl
import com.powerhouseai.myweather.data.remote.datasource.WeatherRemoteDataSource
import com.powerhouseai.myweather.data.remote.datasource.WeatherRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun provideWeatherRemoteDataSource(weatherRemoteDataSourceImpl: WeatherRemoteDataSourceImpl): WeatherRemoteDataSource

    @Binds
    abstract fun provideWeatherLocalDataSource(weatherLocalDataSourceImpl: WeatherLocalDataSourceImpl): WeatherLocalDataSource
}