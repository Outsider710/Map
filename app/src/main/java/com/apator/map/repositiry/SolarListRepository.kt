package com.apator.map.repositiry

import com.apator.map.SolarApi
import com.apator.map.model.solarlist.SolarsList
import com.mapbox.mapboxsdk.geometry.LatLng

class SolarListRepository(private val api:SolarApi) : BaseRepository() {

    suspend fun getSolarListAmerica() : SolarsList?{

        //safeApiCall is defined in BaseRepository.kt (https://gist.github.com/navi25/67176730f5595b3f1fb5095062a92f15)
        val solarResponse = safeApiCall(
            call = {api.getSolarLsitAmerica().await()},
            errorMessage = "Error Fetching Popular Movies"
        )

        return solarResponse


    }
    suspend fun getSolarListAsia() : SolarsList?{

        //safeApiCall is defined in BaseRepository.kt (https://gist.github.com/navi25/67176730f5595b3f1fb5095062a92f15)
        val solarResponse = safeApiCall(
            call = {api.getSolarLsitAsia().await()},
            errorMessage = "Error Fetching Popular Movies"
        )

        return solarResponse


    }
}