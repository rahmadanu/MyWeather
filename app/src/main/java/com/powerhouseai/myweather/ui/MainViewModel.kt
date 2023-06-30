package com.powerhouseai.myweather.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powerhouseai.myweather.data.model.response.WeatherResponse
import com.powerhouseai.myweather.data.model.ui.WeatherUiModel
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

    private val _currentLocationWeather = MutableLiveData<Resource<WeatherUiModel>>()
    val currentLocationWeather: LiveData<Resource<WeatherUiModel>> get() = _currentLocationWeather

    private val _currentLocationWeatherLocal = mutableListOf<WeatherUiModel>()
    val currentLocationWeatherLocal: List<WeatherUiModel> get() = _currentLocationWeatherLocal

    private val _cityWeather = MutableLiveData<Resource<WeatherResponse>>()
    val cityWeather: LiveData<Resource<WeatherResponse>> get() = _cityWeather

    fun getCurrentLocationWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            _currentLocationWeather.postValue(Resource.Loading())

            repository.setCurrentLocation(latitude, longitude)

            val weather = repository.getCurrentLocationWeather(latitude, longitude)

            viewModelScope.launch(Dispatchers.Main) {
                _currentLocationWeather.postValue(weather)
            }
        }
    }

    fun getWeatherByCityName(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _cityWeather.postValue(Resource.Loading())

            val weather = repository.getWeatherByCityName(city)

            viewModelScope.launch(Dispatchers.Main) {
                _cityWeather.postValue(weather)
            }
        }
    }
}