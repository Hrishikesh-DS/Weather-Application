package com.capgemini.myweatherapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capgemini.weatherappp.OpenWeatherAPI7Days.SSevenDays
import com.capgemini.weatherappp.OpenWeatherApi.OWInterface
import retrofit2.Call
import retrofit2.Response
import java.util.*

class WeatherDetailsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_details, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = activity?.getSharedPreferences("Location", Context.MODE_PRIVATE)
        val lat = pref?.getString("lat","NoLat")!!
        val lon = pref?.getString("lon","NoLon")!!
        Log.d("WeatherDetailsFragment","$lat $lon")

        val posterIV = view.findViewById<ImageView>(R.id.iVmain)
        val titleT = view.findViewById<TextView>(R.id.wtitleT)
        val minT = view.findViewById<TextView>(R.id.wminT)
        val maxT = view.findViewById<TextView>(R.id.wmaxT)
        val dateT = view.findViewById<TextView>(R.id.wdateT)
        val desT = view.findViewById<TextView>(R.id.wdesT)
        val humT = view.findViewById<TextView>(R.id.humT)

        val dayPref =  activity?.getSharedPreferences("Day", Context.MODE_PRIVATE)
        val url = dayPref?.getString("posterIV","")

        val imageUrl = "https://openweathermap.org/img/w/${url}.png"
        Glide.with(view).load(Uri.parse(imageUrl)).into(posterIV)

        titleT.text = dayPref?.getString("titleT","")!!
        val date = dayPref?.getLong("dateT",0)!!
        val c = Calendar.getInstance()
        c.timeInMillis = date
        val time = c.time.toString().split(" ")
        if(dayPref?.getString("source","")=="daily"){
            val maxi = dayPref?.getInt("maxT",0)!!
            maxT.text="Temperature: ${maxi-273}/"
            val mini = dayPref?.getInt("minT",0)!!
            minT.text="${mini-273}°C"
            dateT.text="${time[0]} ${time[1]} ${time[2]}"
        }
        else if(dayPref?.getString("source","")=="hour"){
            val maxi = dayPref?.getInt("maxT",0)!!
            maxT.text="Temperature: ${maxi-273}°C"
            minT.text=""
            dateT.text="${time[0]} ${time[3]}"
        }


        desT.text = dayPref?.getString("desT","")!!
        humT.text = "Humidity: ${dayPref?.getString("humT","")!!}"

        // Set the adapter
        val key ="48106e1fb79e63b7a3014a6044a83836"
        val req = OWInterface.getInstance().getHourlyWeather(lat=lat, lon=lon, key=key)
        req.enqueue(WeatherDataCallback())
    }

    inner class WeatherDataCallback: retrofit2.Callback<SSevenDays>{
        override fun onResponse(call: Call<SSevenDays>, response: Response<SSevenDays>) {
            if(response.isSuccessful)
            {   val hours=response.body() as SSevenDays
                Log.d("WeatherFragment",response.raw().toString())
                Log.d("WeatherFragment",hours.toString())
                hours.hourly.forEach {
                    it.dt
                    val c = Calendar.getInstance();
                    c.setTimeInMillis(it.dt.toLong()*1000);
                    Log.d("times","${c.time}")
                }
                hours.hourly.let {
//                    rView.adapter = MyweatherRecyclerViewAdapter(it)
                }

            }
        }

        override fun onFailure(call: Call<SSevenDays>, t: Throwable) {
            TODO("Not yet implemented")
        }

    }



}