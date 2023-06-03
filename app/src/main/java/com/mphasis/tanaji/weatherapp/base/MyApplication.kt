package com.mphasis.tanaji.weatherapp.base

import android.app.Application
import com.mphasis.tanaji.weatherapp.api.APiService
import com.mphasis.tanaji.weatherapp.api.RetrofitClient
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    companion object {
        private lateinit var retrofitBuilder: APiService
    }

    override fun onCreate() {
        super.onCreate()
        retrofitBuilder = RetrofitClient.getClient("https://api.openweathermap.org/data/2.5/")
            .create(APiService::class.java)
    }
//    private var mAppInstance: MyApplication? = null
//
//    fun getInstance(): MyApplication? {
//        if (mAppInstance == null) {
//            mAppInstance = MyApplication()
//        }
//        return mAppInstance
//    }
}