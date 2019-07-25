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
import com.apator.map.helpers.ValuesGenerator
import com.apator.map.model.earthquake.Earthquake
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    /*
        private val parentJob = Job()

        private val coroutineContext: CoroutineContext
            get() = parentJob + Dispatchers.IO

        private val scope = CoroutineScope(coroutineContext)*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        fetchNearEarthquakes()
    }

    private fun fetchNearEarthquakes() {
        val solarApi = ApiFactory.solarApi
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        val prevDay = calendar.time
        solarApi.getNearEarthquakes(
            ValuesGenerator.getDateForEarthquake(prevDay),
            ValuesGenerator.getDateForEarthquake(Date()),
            38.239930,
            -122.672335
        ).enqueue(object : Callback<Earthquake> {
            override fun onFailure(call: Call<Earthquake>, t: Throwable) {

            }

            override fun onResponse(call: Call<Earthquake>, response: Response<Earthquake>) {
                onEarthquakeFetched(response.body()!!)
            }
        })//.observe(this, Observer {onEarthquakeFetched(it.body()!!)})
    }

    private fun onEarthquakeFetched(earthquake: Earthquake) {
        Log.d("", earthquake.toString())
        val notification = NotificationCompat.Builder(this, getString(R.string.near_earthquake_channel_id))
            .setContentTitle(getString(R.string.near_earthquake_title))
            .setContentText("${getString(R.string.near_earthquake_content_1)} ${earthquake.features?.size} ${getString(R.string.near_earthquake_content_2)}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
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
}

