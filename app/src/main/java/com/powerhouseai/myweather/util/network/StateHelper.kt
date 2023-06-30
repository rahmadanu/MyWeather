package com.powerhouseai.myweather.util.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.powerhouseai.myweather.R
import com.powerhouseai.myweather.data.model.response.ErrorResponse
import com.powerhouseai.myweather.util.wrapper.Resource
import retrofit2.HttpException

object StateHelper {

    suspend fun <T> proceedResource(context: Context, coroutines: suspend () -> T): Resource<T> {
        return if (AppStatus.checkConnectivity(context) != AppStatus.NONE) {
            try {
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
        } else {
            try {
                if (coroutines.invoke() != null) {
                    Resource.Success(coroutines.invoke(), context.getString(R.string.txt_error_no_internet))
                } else {
                    Resource.Error(Exception(), context.getString(R.string.txt_error_no_internet))
                }
            } catch (e: Exception) {
                Resource.Error(Exception(), context.getString(R.string.txt_error_no_internet))
            }
        }
    }
}