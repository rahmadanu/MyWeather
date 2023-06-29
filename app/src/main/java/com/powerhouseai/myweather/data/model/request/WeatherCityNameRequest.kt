package com.powerhouseai.myweather.data.model.request

data class WeatherCityNameRequest(
    val city: String,
    val mode: String = "",
    val units: String = "",
    val lang: String = "",
)