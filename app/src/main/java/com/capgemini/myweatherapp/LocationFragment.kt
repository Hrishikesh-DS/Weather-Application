package com.capgemini.myweatherapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*


class LocationFragment : Fragment(),LocationListener {

    lateinit var lManager: LocationManager
    lateinit var locT: TextView
    lateinit var locationE: AutoCompleteTextView

    var currentLoc: Location? = null
    var latitude: Double=0.0
    var longitude: Double=0.0

    lateinit var coder: Geocoder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locT = view.findViewById<TextView>(R.id.textView2)
        checkPermissions()
        lManager = activity?.getSystemService(Context.LOCATION_SERVICE)!! as LocationManager

        val clocB = view.findViewById<ImageButton>(R.id.clocB)
        clocB.setOnClickListener {
            findLocation()
            callWeatherFragment()
        }

        val searchB = view.findViewById<Button>(R.id.searchB)
        locationE = view.findViewById<AutoCompleteTextView>(R.id.cityE)
        if(locationE.text.toString().isNotEmpty()) {
            searchB.setOnClickListener {
                coder = Geocoder(activity)
                val address = coder.getFromLocationName(locationE.text.toString(), 1)
                val loc = address.get(0)
                latitude = loc.latitude
                longitude = loc.longitude
                callWeatherFragment()
            }
        }else{
            Toast.makeText(activity,"Please enter Location",Toast.LENGTH_LONG).show()
        }
    }

    private fun callWeatherFragment() {
        val pref = activity?.getSharedPreferences("Location", Context.MODE_PRIVATE)
        val editor = pref?.edit()!!
        editor.putString("lat", latitude.toString())
        editor.putString("lon", longitude.toString())
        editor.commit()
        val intent = Intent(activity,MainBottomNav::class.java)
        startActivity(intent)
    }

    private fun findLocation() {
        val providerList = lManager.getProviders(true)
        var providerName = ""
        if (providerList.isNotEmpty()) {
            if (providerList.contains(LocationManager.GPS_PROVIDER)) {
                providerName = LocationManager.GPS_PROVIDER
            } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
                providerName = LocationManager.NETWORK_PROVIDER
            } else {
                providerName = providerList[0]
            }
//            Toast.makeText(activity, "Provider name: $providerName", Toast.LENGTH_LONG).show()
            Log.d("MainActivity", "Provider: $providerName")

            val loc = lManager.getLastKnownLocation(providerName)
            if (loc != null) {
                updateLocation(loc)
            } else {
                Toast.makeText(activity, "No Location found", Toast.LENGTH_LONG).show()
            }
            val time: Long = 1000
            val distance: Float = 10.0f
            lManager.requestLocationUpdates(providerName, time, distance, this)
        } else {
            Toast.makeText(activity, "Please Enable Location", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lManager.removeUpdates(this)
    }

    private fun checkPermissions() {
        if (activity?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            activity?.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 1
            )
        } else {
//            Toast.makeText(activity, "Location permission granted", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateLocation(loc: Location) {
        latitude = loc.latitude
        longitude = loc.longitude
        currentLoc = loc
//        Toast.makeText(activity, "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_LONG).show()
        Log.d("LocationFragment", "Latitude: $latitude, Longitude: $longitude")
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {

        if(grantResults.isNotEmpty()){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(activity, "Location permission granted", Toast.LENGTH_LONG).show()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onLocationChanged(location: Location) {
        updateLocation(location)
    }



}