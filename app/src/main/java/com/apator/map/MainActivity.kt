package com.apator.map

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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

        fetchNearEarthquakes()
    }

    fun fetchNearEarthquakes() {
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

    fun onEarthquakeFetched(earthquake: Earthquake) {
        Log.d("", earthquake.toString())
    }
}

