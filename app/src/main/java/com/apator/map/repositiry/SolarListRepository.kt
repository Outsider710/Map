package com.apator.map.repositiry

import com.apator.map.SolarApi
import com.apator.map.model.singlesolar.Solar
import com.apator.map.model.solarlist.AllStation
import com.apator.map.model.solarlist.SolarsList

class SolarListRepository(private val api:SolarApi) : BaseRepository() {

    suspend fun getSolarList() : SolarsList?{

        //safeApiCall is defined in BaseRepository.kt (https://gist.github.com/navi25/67176730f5595b3f1fb5095062a92f15)
        val solarResponse = safeApiCall(
            call = {api.getSolarLsit().await()},
            errorMessage = "Error Fetching Popular Movies"
        )

        return solarResponse


    }
}