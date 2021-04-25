package com.capgemini.myweatherapp

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.bumptech.glide.Glide

import com.capgemini.weatherapp.OpenWeatherAPI7Days.Hourly
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyHourRecyclerViewAdapter(val hourList: List<Hourly>)
    : RecyclerView.Adapter<MyHourRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val posterIV = view.findViewById<ImageView>(R.id.hrIV)
        val titleT = view.findViewById<TextView>(R.id.ttitleT)
        val tempT = view.findViewById<TextView>(R.id.ttempT)
        val timeT = view.findViewById<TextView>(R.id.timeT)
        val locationT = view.findViewById<TextView>(R.id.loctT)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_hour, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = hourList[position]

        val c = Calendar.getInstance();
        c.timeInMillis = item.dt.toLong()*1000
        val time = c.time.toString().split(" ")

        val pref = holder.itemView.context.getSharedPreferences("Location", Context.MODE_PRIVATE)
        val lat = pref?.getString("lat","0")!!
        val long = pref?.getString("lon","0")!!

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

        holder.locationT.text = placeName
        holder.titleT.text = item.weather[0].main
        holder.tempT.text = "${item.temp.toInt()-273}°C"
        holder.timeT.text = "${time[0]} ${time[3]}"

        val imageUrl = "https://openweathermap.org/img/w/${item.weather[0].icon}.png"
        Glide.with(holder.itemView.context).load(Uri.parse(imageUrl)).into(holder.posterIV)



        val frag = WeatherDetailsFragment()
        holder.itemView.setOnClickListener{

            val pref = it.context?.getSharedPreferences("Day", Context.MODE_PRIVATE)
            val editor = pref?.edit()!!
            editor.putString("source","hour")
            editor.putString("posterIV",item.weather[0].icon)
            editor.putString("titleT",item.weather[0].main)
            editor.putInt("minT",item.temp.toInt())
            editor.putInt("presT",item.pressure)
            editor.putLong("dateT",item.dt.toLong()*1000)
            editor.putString("desT",item.weather[0].description)
            editor.putString("humT",item.humidity.toString())
            editor.putLong("uvT",item.uvi.toLong())
            editor.commit()

            holder.itemView.findNavController().navigate(R.id.action_hourFragment_to_weatherDetailsFragment2)
        }

    }

    override fun getItemCount(): Int = hourList.size



}