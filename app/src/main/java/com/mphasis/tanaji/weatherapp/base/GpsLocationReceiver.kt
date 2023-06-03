package com.mphasis.tanaji.weatherapp.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager

class GpsLocationReceiver : BroadcastReceiver() {
    var listener: GpsLocationListener? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.location.PROVIDERS_CHANGED") {
            val service = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            listener?.onGpsLocationChanged(service.isProviderEnabled(LocationManager.GPS_PROVIDER))
        }
    }
}

interface GpsLocationListener {
    fun onGpsLocationChanged(isConnected: Boolean)
}