package com.capgemini.myweatherapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capgemini.weatherappp.OpenWeatherAPI7Days.SSevenDays
import com.capgemini.weatherappp.OpenWeatherApi.OWInterface
import retrofit2.Call
import retrofit2.Response
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class HourFragment : Fragment() {

    private var columnCount = 1
    lateinit var rView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = 1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hour_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rView=view.findViewById<RecyclerView>(R.id.hrView)

        val pref = activity?.getSharedPreferences("Location", Context.MODE_PRIVATE)
        val lat = pref?.getString("lat","NoLat")!!
        val lon = pref?.getString("lon","NoLon")!!
        Log.d("WeatherListFragment","$lat $lon")
        // Set the adapter
        if (view is RecyclerView){
            rView.layoutManager = LinearLayoutManager(context)
        }
        val key ="48106e1fb79e63b7a3014a6044a83836"
        val req = OWInterface.getInstance().getHourlyWeather(lat=lat, lon=lon, key=key)
        req.enqueue(HourCallback())

    }

    inner class HourCallback: retrofit2.Callback<SSevenDays>{
        override fun onResponse(call: Call<SSevenDays>, response: Response<SSevenDays>) {
            if(response.isSuccessful)
            {   val hours=response.body() as SSevenDays
                Log.d("HourlyFragment",response.raw().toString())
                Log.d("HourlyFragment",hours.toString())
                hours.hourly.forEach {
                    it.dt
                    val c = Calendar.getInstance();
                    c.setTimeInMillis(it.dt.toLong()*1000);
                    Log.d("times","${c.time}")
                }
                hours.hourly.let {
                    rView.adapter = MyHourRecyclerViewAdapter(it)
                }

            }
        }

        override fun onFailure(call: Call<SSevenDays>, t: Throwable) {
            TODO("Not yet implemented")
        }

    }


}