package com.powerhouseai.myweather.data.model.ui

data class WeatherUiModel(
    val currentCity: String? = "",
    val weatherIcon: String? = "",
    val weatherDescription: String? = "",
    val todayDate: String? = "",
    val temperature: String? = "",
    val temperatureFeelsLike: String? = "",
    val temperatureMinMax: String? = "",
    val humidity: String? = "",
    val clouds: String? = "",
    val wind: String? = "",
    val pressure: String? = "",
    val visibility: String? = "",
)
