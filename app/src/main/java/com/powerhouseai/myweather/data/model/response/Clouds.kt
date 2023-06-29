package com.powerhouseai.myweather.data.model.response


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    var all: Int? = null
)