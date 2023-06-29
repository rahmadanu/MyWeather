package com.powerhouseai.myweather.data.model.request

data class WeatherLatLonRequest(
    val latitude: Double,
    val longitude: Double,
    val mode: String = "",
    val units: String = "",
    val lang: String = "",
)