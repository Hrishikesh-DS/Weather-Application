package com.capgemini.weatherapp.OpenWeatherAPI7Days

import com.capgemini.weatherappp.OpenWeatherAPI7Days.*
import com.google.gson.annotations.SerializedName

data class Hourly (
    val dt: Int,
    val temp: Double,
    val feelsLike: FeelsLike,
    val humidity: Int,
    val pressure: Int,
    val uvi: Double,
    val weather: List<WeatherX>
)