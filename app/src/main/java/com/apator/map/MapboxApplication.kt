package com.apator.map

import android.app.Application
import com.mapbox.mapboxsdk.Mapbox

class MapboxApplication : Application() {

    override fun onCreate() {
        super.onCreate()

// Mapbox Access token
        Mapbox.getInstance(this, resources.getString(R.string.API_KEY_MAPBOX))
    }
}