package com.capgemini.weatherappp.OpenWeatherApi

import android.util.Log
import com.capgemini.weatherappp.OpenWeatherAPI7Days.SSevenDays
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface OWInterface {

    @GET("data/2.5/onecall")
    fun getDailyWeather(
        @Query("lat") lat: String, @Query("lon") lon: String,
        @Query("exclude") exclude: String = "minutely,hourly,alerts",
        @Query("appid") key: String
    ): Call<SSevenDays>
    @GET("data/2.5/onecall")
    fun getHourlyWeather(
        @Query("lat") lat: String, @Query("lon") lon: String,
        @Query("exclude") exclude: String = "minutely,alerts",
        @Query("appid") key: String
    ): Call<SSevenDays>

    companion object {
        val BASE_URL = "https://api.openweathermap.org/"
        fun getInstance(): OWInterface {
            val builder = Retrofit.Builder()
            builder.addConverterFactory(GsonConverterFactory.create())
            builder.baseUrl(BASE_URL)
            val retrofit = builder.build()

            return retrofit.create(OWInterface::class.java)
        }
    }
}