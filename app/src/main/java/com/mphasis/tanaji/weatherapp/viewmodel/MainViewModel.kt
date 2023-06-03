package com.mphasis.tanaji.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mphasis.tanaji.weatherapp.model.CityWiseModel
import com.mphasis.tanaji.weatherapp.model.CityWiseModelNew
import com.mphasis.tanaji.weatherapp.model.WeatherModel
import com.mphasis.tanaji.weatherapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val isInternetConnected = MutableLiveData<Boolean>()
    val isGpsLocationConnected = MutableLiveData<Boolean?>()

    private var _weatherResponse = MutableLiveData<CityWiseModelNew>()
    val weatherResponse: LiveData<CityWiseModelNew> get() = _weatherResponse

    private var _cityWiseResponse = MutableLiveData<CityWiseModel>()
    val cityWiseResponse: LiveData<CityWiseModel> get() = _cityWiseResponse


    fun getCityWiseData(
        lat: String,
        lng: String,
        appId: String
    ) {
        repository.getCityWiseData(lat, lng, appId, _cityWiseResponse)
    }

    fun getCityWeather(
        cityName: String,
        appId: String
    ) {
        repository.getCityWeather(cityName, appId, _cityWiseResponse)
    }
    fun getCityWeatherNew(
        cityName: String
    ) {
        repository.getCityWiseWeatherNew(cityName, _weatherResponse)
    }


//    fun getWeather(distCode: String) {
//        repository.getTalukaOnState(distCode, _weatherResponse)
//    }

}