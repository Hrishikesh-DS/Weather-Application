package com.capgemini.weatherappp.OpenWeatherAPI7Days


import com.google.gson.annotations.SerializedName

data class WeatherX(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)