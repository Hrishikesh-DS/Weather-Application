package com.capgemini.weatherappp.OpenWeatherAPI7Days


import com.capgemini.weatherapp.OpenWeatherAPI7Days.Hourly
import com.google.gson.annotations.SerializedName

data class SSevenDays(
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int
)