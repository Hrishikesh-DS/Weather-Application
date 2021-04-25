package com.capgemini.myweatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun locClick(view: View) {
        when(view.id){
            R.id.locaB->{
                val transaction = supportFragmentManager.beginTransaction()
                val frag = LocationFragment()
                transaction.replace(R.id.frameL, frag, "location")
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
    }
}