package com.powerhouseai.myweather.util.wrapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.powerhouseai.myweather.data.model.response.ErrorResponse
import retrofit2.HttpException

object StateHelper {

    suspend fun <T> proceed(coroutines: suspend () -> T): Resource<T> {
        Resource.Loading(coroutines.invoke())
        return try {
            Resource.Success(coroutines.invoke())
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    val errorMessageResponseType = object : TypeToken<ErrorResponse>() {}.type
                    val error: ErrorResponse = Gson().fromJson(e.response()?.errorBody()?.charStream(),
                        errorMessageResponseType)
                    Resource.Error(e, "${error.message}")
                }
                else -> {
                    Resource.Error(e, e.message)
                }
            }
        }
    }
}