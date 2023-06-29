package com.powerhouseai.myweather.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powerhouseai.myweather.data.model.response.WeatherResponse
import com.powerhouseai.myweather.data.repository.WeatherRepository
import com.powerhouseai.myweather.util.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherRepository
): ViewModel() {

    private val _currentLocationWeather = MutableLiveData<Resource<WeatherResponse>>()
    val currentLocationWeather: LiveData<Resource<WeatherResponse>> get() = _currentLocationWeather

    private val _cityWeather = MutableLiveData<Resource<WeatherResponse>>()
    val cityWeather: LiveData<Resource<WeatherResponse>> get() = _cityWeather

    fun getCurrentLocationWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getCurrentLocationWeather(latitude, longitude)

            viewModelScope.launch(Dispatchers.Main) {
                _currentLocationWeather.postValue(response)
            }
        }
    }

    fun getWeatherByCityName(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getWeatherByCityName(city)

            viewModelScope.launch(Dispatchers.Main) {
                _cityWeather.postValue(response)
            }
        }
    }
}