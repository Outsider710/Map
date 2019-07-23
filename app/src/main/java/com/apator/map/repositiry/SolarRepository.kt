package com.apator.map.repositiry

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.preference.PreferenceManager
import com.apator.map.SolarApi
import com.apator.map.database.Entity.DetailsEntity
import com.apator.map.database.Entity.SolarEntity
import com.apator.map.database.SolarDatabase
import com.apator.map.model.singlesolar.Solar
import com.apator.map.model.solarlist.SolarsList

class SolarRepository(private val api: SolarApi, application: Application) : BaseRepository() {
    /*
    *   Databse
    */


    private val database = SolarDatabase.getDatabse(application)
    val solarDao = database.solarDao()

    @WorkerThread
    suspend fun insertAllSolars(solars: List<SolarEntity>) {

        solarDao.insertAllSolars(*solars.toTypedArray())

    }

    @WorkerThread
    suspend fun insertDetails(detailsEntity: DetailsEntity)
    {
        solarDao.insertDetails(detailsEntity)
    }


    /*
    *    JSON
    */
    suspend fun getSolarListAmerica(apiKey:String): SolarsList? {
        //safeApiCall is defined in BaseRepository.kt (https://gist.github.com/navi25/67176730f5595b3f1fb5095062a92f15)
        val solarResponse = safeApiCall(
            call = { api.getSolarLsitAmerica(apiKey).await() },
            errorMessage = "Error Fetching Popular Movies"
        )
        return solarResponse
    }

    suspend fun getSolarListAsia(apiKey:String): SolarsList? {

        //safeApiCall is defined in BaseRepository.kt (https://gist.github.com/navi25/67176730f5595b3f1fb5095062a92f15)
        val solarResponse = safeApiCall(
            call = { api.getSolarLsitAsia(apiKey).await() },
            errorMessage = "Error Fetching list of solar"
        )
        return solarResponse
    }

    suspend fun getDetails(
        api_key: String,
        file_id:String): Solar? {
        val response = safeApiCall(
            call = {
                api.getSolarDetails(
                    api_key,
                    file_id
                ).await()
            },
            errorMessage = "Error Fetching details of solar"
        )
        return response
    }
}