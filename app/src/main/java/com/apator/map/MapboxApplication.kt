package com.apator.map

import android.app.Application
import com.apator.map.database.viewmodel.SolarDbViewModel
import com.mapbox.mapboxsdk.Mapbox
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MapboxApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Mapbox Access token
        Mapbox.getInstance(this, resources.getString(R.string.API_KEY_MAPBOX))

        startKoin {
            androidContext(this@MapboxApplication)
            modules(listOf(module { viewModel { SolarDbViewModel(androidApplication()) } }))
        }


    }
}