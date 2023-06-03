package com.mphasis.tanaji.weatherapp.api

import com.mphasis.tanaji.weatherapp.model.CityWiseModel
import com.mphasis.tanaji.weatherapp.model.CityWiseModelNew
import com.mphasis.tanaji.weatherapp.model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APiService {
    //q=18.494737,73.947922&format=json&num_of_days=7
    @GET("weather/")
    suspend fun getCityWeather(
        @Query("q") cityName: String,
        @Query("APPID") appId: String
    ): Response<CityWiseModel?>?

    @GET("weather/")
    suspend fun getCityWiseWeather(
        @Query("lat") lat: String,
        @Query("lon") lng: String,
        @Query("appid") appId: String
    ): Response<CityWiseModel?>?

    @GET("weather.ashx")
    suspend fun getCityWiseWeatherNew(
        @Query("key") key: String,
        @Query("q") cityName: String,
        @Query("format") format: String,
        @Query("num_of_days") noOfDays: String
    ): Response<CityWiseModelNew?>?
}