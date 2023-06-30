package com.powerhouseai.myweather.ui

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

    private val _citiesWeather = MutableLiveData<Resource<List<WeatherUiModel>>>()
    val citiesWeather: LiveData<Resource<List<WeatherUiModel>>> get() = _citiesWeather

    fun getCurrentLocationWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            _currentLocationWeather.postValue(Resource.Loading())

            repository.setCurrentLocation(latitude, longitude)

            val currentLocationWeather = repository.getCurrentLocationWeather(latitude, longitude)

            viewModelScope.launch(Dispatchers.Main) {
                _currentLocationWeather.postValue(currentLocationWeather)
            }
        }
    }

    fun getCities() {
        viewModelScope.launch(Dispatchers.IO) {
            val citiesWeather = repository.getCitiesWeather()

            viewModelScope.launch(Dispatchers.Main) {
                _citiesWeather.postValue(citiesWeather)
            }
        }
    }
}