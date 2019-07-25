package com.apator.map

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import com.apator.map.database.Entity.SolarEntity
import com.apator.map.helpers.GpsHelper
import com.apator.map.helpers.ValuesGenerator
import com.apator.map.model.earthquake.Earthquake
import com.apator.map.model.earthquake.Feature
import com.apator.map.viewmodel.SolarViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    /*
        private val parentJob = Job()

        private val coroutineContext: CoroutineContext
            get() = parentJob + Dispatchers.IO

        private val scope = CoroutineScope(coroutineContext)*/

    private val solarViewModel: SolarViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        fetchNearEarthquakes()
    }

    private fun fetchNearEarthquakes() {
        val solarApi = ApiFactory.solarApi
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -30)
        val prevDay = calendar.time
        solarApi.getAllEarthquakes(
            ValuesGenerator.getDateForEarthquake(prevDay),
            ValuesGenerator.getDateForEarthquake(Date())
        ).enqueue(object : Callback<Earthquake> {
            override fun onFailure(call: Call<Earthquake>, t: Throwable) {}

            override fun onResponse(call: Call<Earthquake>, response: Response<Earthquake>) {
                solarViewModel.getAllSolars().observe(this@MainActivity, Observer { solars ->
                    val endangeredSolars = checkQuakesForSolars(response.body()!!.features!!, solars)
                    if (!endangeredSolars.isNullOrEmpty()) {
                        displayNotification(endangeredSolars)
                    }
                })
            }
        })
    }

    private fun displayNotification(endangeredSoalrs: List<String>) {
        val notification = NotificationCompat.Builder(this, getString(R.string.near_earthquake_channel_id))
            .setContentTitle(getString(R.string.near_earthquake_title))
            .setContentText("Endangered solars: ${endangeredSoalrs.size}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setStyle(NotificationCompat.BigTextStyle().bigText("$endangeredSoalrs"))
            .build()
        NotificationManagerCompat.from(this).notify(123541, notification)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.near_earthquake_channel)
            val descriptionText = getString(R.string.near_earthquake_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(getString(R.string.near_earthquake_channel_id), name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkQuakesForSolars(earthquakes: List<Feature?>, solars: List<SolarEntity>): ArrayList<String> {
        val endangeredSolars = ArrayList<String>()

        earthquakes.forEach { quake ->
            solars.forEach { solar ->
                val distance = GpsHelper.gpsDistance(
                    solar.lat,
                    solar.lon,
                    quake?.geometry!!.coordinates!![1]!!,
                    quake.geometry.coordinates!![0]!!
                )
                if (distance < 60) {
                    Log.d("", "Distance: $distance | solar_id: ${solar.id}")
                    endangeredSolars.add(solar.id)
                }
            }
        }
        return endangeredSolars
    }
}

