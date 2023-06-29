package com.powerhouseai.myweather.di

import android.util.Log
import com.powerhouseai.myweather.BuildConfig
import com.powerhouseai.myweather.data.remote.service.WeatherApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("apiService", message)
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideNetworkInterceptor(): Interceptor {
        val appId = "appid"
        val apiKey = BuildConfig.OPEN_WEATHER_API_KEY

        val interceptor = Interceptor { chain ->
            val oldUrlWithKey = chain.request().url.newBuilder().addQueryParameter(appId, apiKey).build()
            val newUrl = chain.request().newBuilder().url(oldUrlWithKey)
            chain.proceed(newUrl.build())
        }
        return interceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor, networkInterceptor: Interceptor): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(networkInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(loggingInterceptor)
        }
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApiService(retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }
}