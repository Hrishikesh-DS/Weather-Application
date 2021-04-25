package com.capgemini.myweatherapp

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capgemini.weatherappp.OpenWeatherAPI7Days.Daily
import java.util.*


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyDailyRecyclerViewAdapter(val weatherList: List<Daily>)
    : RecyclerView.Adapter<MyDailyRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val posterIV = view.findViewById<ImageView>(R.id.iV)
        val titleT = view.findViewById<TextView>(R.id.titleT)
        val tempT = view.findViewById<TextView>(R.id.tempT)
        val dateT = view.findViewById<TextView>(R.id.dateT)
        val locationT = view.findViewById<TextView>(R.id.locationT)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_daily, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = weatherList[position]

        val c = Calendar.getInstance();
        c.timeInMillis = item.dt.toLong() * 1000
        val time = c.time.toString().split(" ")

        val pref = holder.itemView.context.getSharedPreferences("Location", Context.MODE_PRIVATE)
        val lat = pref?.getString("lat", "0")!!
        val long = pref?.getString("lon", "0")!!

        val geocoder = Geocoder(holder.itemView.context, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(lat.toDouble(), long.toDouble(), 1)
        var location: String
        var placeName:String
        if (addresses.isNotEmpty()) {
            location = addresses[0].getAddressLine(0)
            placeName = location.split(",")[3]
        }
        else{
            location=""
            placeName = ""
        }

        Log.d("City Name", placeName)

        holder.locationT.text = placeName
        holder.titleT.text = item.weather[0].main
        holder.tempT.text = "${(item.temp.max - 273).toInt()}°C/${(item.temp.min - 273).toInt()}°C"
        holder.dateT.text = "${time[0]} ${time[1]} ${time[2]}"

        val imageUrl = "https://openweathermap.org/img/w/${item.weather[0].icon}.png"
        Glide.with(holder.itemView.context).load(Uri.parse(imageUrl)).into(holder.posterIV)


        val frag = WeatherDetailsFragment()
        holder.itemView.setOnClickListener {

            val pref = it.context?.getSharedPreferences("Day", Context.MODE_PRIVATE)
            val editor = pref?.edit()!!
            editor.putString("source", "daily")
            editor.putString("posterIV", item.weather[0].icon)
            editor.putString("titleT", item.weather[0].main)
            editor.putInt("minT", item.temp.min.toInt())
            editor.putInt("maxT", item.temp.max.toInt())
            editor.putLong("dateT", item.dt.toLong() * 1000)
            editor.putString("desT", item.weather[0].description)
            editor.putString("humT", item.humidity.toString())
            editor.commit()

            holder.itemView.findNavController().navigate(R.id.action_dailyFragment_to_weatherDetailsFragment)
        }
    }

    override fun getItemCount(): Int = weatherList.size


}