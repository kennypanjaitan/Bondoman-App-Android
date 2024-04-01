package com.example.myapplication.adapter

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale

class LocationAdapter(context: Context, activity: Activity) {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var address: String = ""
    private var context: Context = context
    private var activity: Activity = activity

    fun getLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(context,android.Manifest.permission.ACCESS_FINE_LOCATION )
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),100
            )
            return
        }
        val location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            if (it != null) {
                val temp = it.latitude
                latitude = temp
                longitude = it.longitude
                val geocoder = Geocoder(context, Locale.getDefault())
                try {
                    val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
                    val obj: Address = addresses!![0]
                    address = obj.locality
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    fun getLatitude():Double{
        return latitude
    }

    fun getLongitude():Double {
        return longitude
    }

    fun getAddress():String{
        return address
    }

}