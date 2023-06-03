package com.mphasis.tanaji.weatherapp.repository

import androidx.lifecycle.MutableLiveData
import com.mphasis.tanaji.weatherapp.api.APiService
import com.mphasis.tanaji.weatherapp.api.BaseUrl
import com.mphasis.tanaji.weatherapp.model.CityWiseModel
import com.mphasis.tanaji.weatherapp.model.CityWiseModelNew
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class MainRepository @Inject constructor(@Named("getBaseUrl") private val api: APiService) {

    fun getCityWiseData(
        lat: String,
        lng: String,
        appId: String,
        response: MutableLiveData<CityWiseModel>
    ) = CoroutineScope(Dispatchers.IO).launch {
        val cityWiseResponse = api.getCityWiseWeather(lat, lng, appId)

        if (cityWiseResponse != null) {
            if (cityWiseResponse.isSuccessful) {
                response.postValue(cityWiseResponse.body())
            } else response.postValue(null)
        } else {
            response.postValue(null)
        }
    }

    fun getCityWiseWeatherNew(
        cityName: String,
        response: MutableLiveData<CityWiseModelNew>
    ) = CoroutineScope(Dispatchers.IO).launch {
        val cityWiseResponse = api.getCityWiseWeatherNew(BaseUrl.appIdNew, cityName, "json", "7")

        if (cityWiseResponse != null) {
            if (cityWiseResponse.isSuccessful) {
                response.postValue(cityWiseResponse.body())
            } else response.postValue(null)
        } else {
            response.postValue(null)
        }
    }

    fun getCityWeather(
        cityName: String,
        appId: String,
        response: MutableLiveData<CityWiseModel>
    ) = CoroutineScope(Dispatchers.IO).launch {
        val cityWiseResponse = api.getCityWeather(cityName, appId)

        if (cityWiseResponse != null) {
            if (cityWiseResponse.isSuccessful) {
                response.postValue(cityWiseResponse.body())
            } else response.postValue(null)
        } else {
            response.postValue(null)
        }
    }

}