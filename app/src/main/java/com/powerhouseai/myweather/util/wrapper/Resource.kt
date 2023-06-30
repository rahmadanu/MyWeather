package com.powerhouseai.myweather.util.wrapper

sealed class Resource<T>(
    val payload: T? = null,
    val message: String? = null,
    val exception: Exception? = null,
) {
    class Success<T>(val data: T?, message: String? = null) : Resource<T>(data, message)
    class Empty<T> : Resource<T>()
    class Error<T>(exception: Exception?, message: String?) :
        Resource<T>(message = message, exception = exception)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}