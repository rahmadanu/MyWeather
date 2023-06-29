package com.powerhouseai.myweather.data.model.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("cod")
    val code: String? = null,
    @SerializedName("message")
    val message: String? = null,
)