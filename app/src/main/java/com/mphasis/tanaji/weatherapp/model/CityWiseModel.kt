package com.mphasis.tanaji.weatherapp.model

data class CityWiseModel(
    val base: String,
    val clouds: Clouds,
    val cod: String,
    val coord: Coord,
    val dt: String,
    val id: String,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: String,
    val visibility: String,
    val weather: List<Weather>,
    val wind: Wind
) {
    data class Clouds(
        val all: String
    )

    data class Coord(
        val lat: Double,
        val lon: Double
    )

    data class Main(
        val feels_like: String,
        val grnd_level: String,
        val humidity: String,
        val pressure: String,
        val sea_level: String,
        val temp: String,
        val temp_max: String,
        val temp_min: String
    )

    data class Sys(
        val country: String,
        val id: String,
        val sunrise: String,
        val sunset: String,
        val type: String
    )

    data class Weather(
        val description: String,
        val icon: String,
        val id: String,
        val main: String
    )

    data class Wind(
        val deg: String,
        val gust: String,
        val speed: String
    )
}
