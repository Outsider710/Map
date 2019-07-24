package com.apator.map.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apator.map.ApiFactory
import com.apator.map.database.Entity.DetailsEntity
import com.apator.map.database.Entity.SolarEntity
import com.apator.map.helpers.mappers.SolarListJSONToDb
import com.apator.map.model.singlesolar.Solar
import com.apator.map.model.solarlist.SolarsList
import com.apator.map.repositiry.SolarRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class SolarViewModel(application: Application) : AndroidViewModel(application) {
    //
    //  database
    //

    val repository = SolarRepository(ApiFactory.solarApi, application)


    fun insertAllStations(solars: List<SolarEntity>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertAllSolars(solars)
    }

    fun insertDetails(detailsEntity: DetailsEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertDetails(detailsEntity)
    }

    fun getAllSolars() = repository.solarDao.getAllSolars()
    fun getDetailsById(id: String) = repository.solarDao.getDetailsById(id)

    //
    // JSON
    //

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    val solarLiveData = MutableLiveData<List<SolarEntity>>()
    var solarDetailsLiveData = MutableLiveData<Solar>()


    fun fetchSolar(

        api_key: String = "5Gibh08OmtfPAZXEF4qhLJc7ckXxBPL8PBlst9ws",
        file_id: String = "2-566510-EPW-CSWD-epw"
    ) {
        scope.launch {

            val solar = repository.getDetails(
                api_key,
                file_id
            )
            solarDetailsLiveData.postValue(solar)
        }
    }

    fun fetchSolarsAmerica(apiKey: String) {
        scope.launch {
            val solars = repository.getSolarListAmerica(apiKey)
            val mappedSolars = solars?.outputs?.allStations?.map { SolarListJSONToDb.map(it!!) }
            solarLiveData.postValue(mappedSolars)
        }
    }

    fun fetchSolarsAsia(apiKey: String) {
        scope.launch {
            val solars = repository.getSolarListAsia(apiKey)
            val mappedSolars = solars?.outputs?.allStations?.map { SolarListJSONToDb.map(it!!) }
            solarLiveData.postValue(mappedSolars)
        }
    }

    fun fetchAllSolars(apiKey: String) {
        scope.launch {
            val americaSolars = repository.getSolarListAmerica(apiKey)
            val asiaSolars = repository.getSolarListAsia(apiKey)
            if(americaSolars == null || asiaSolars == null) {
                solarLiveData.postValue(null)
            } else {
                val mappedAmericaSolars = americaSolars.outputs?.allStations?.map { SolarListJSONToDb.map(it!!) }
                val mappedAsiaSolars = asiaSolars.outputs?.allStations?.map { SolarListJSONToDb.map(it!!) }
                val joinedSolars = ArrayList<SolarEntity>()

                if(mappedAmericaSolars != null) {
                    joinedSolars.addAll(mappedAmericaSolars)
                }
                if(mappedAsiaSolars != null) {
                    joinedSolars.addAll(mappedAsiaSolars)
                }
                solarLiveData.postValue(joinedSolars)
            }
        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()

}
