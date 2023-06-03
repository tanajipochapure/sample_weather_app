package com.mphasis.tanaji.weatherapp.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mphasis.tanaji.weatherapp.api.APiService
import com.mphasis.tanaji.weatherapp.api.RetrofitClient
import com.mphasis.tanaji.weatherapp.repository.MainRepository
import com.mphasis.tanaji.weatherapp.viewmodel.MainViewModel


class MainFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return MainViewModel(
            repository = MainRepository(
                RetrofitClient.getClient("https://openweathermap.org/api/").create(APiService::class.java),
            )
        ) as T
    }
}
