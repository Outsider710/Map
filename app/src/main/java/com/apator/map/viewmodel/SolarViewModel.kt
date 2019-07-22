package com.apator.map.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apator.map.ApiFactory
import com.apator.map.database.Entity.DetailsEntity
import com.apator.map.database.Entity.SolarEntity
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


    val solarLiveData = MutableLiveData<SolarsList>()
    var solarDetailsLiveData = MutableLiveData<Solar>()


    fun fetchSolar(
        format: String = "json",
        api_key: String = "5Gibh08OmtfPAZXEF4qhLJc7ckXxBPL8PBlst9ws",
        lat: Double = 0.0,
        lon: Double = 0.0,
        file_id: String = "2-566510-EPW-CSWD-epw",
        system_capacity: Double = 4.0,
        azimuth: Double = 180.0,
        tilt: Double = 40.0,
        array_type: Int = 1,
        module_type: Int = 1,
        losses: Double = 10.0
    ) {
        scope.launch {

            val solar = repository.getDetails(
                format,
                api_key,
                lat,
                lon,
                file_id,
                system_capacity,
                azimuth,
                tilt,
                array_type,
                module_type,
                losses
            )
            solarDetailsLiveData.postValue(solar)
        }
    }

    fun fetchSolarsAmerica() {
        scope.launch {
            val solars = repository.getSolarListAmerica()
            solarLiveData.postValue(solars)

        }
    }

    fun fetchSolarsAsia() {
        scope.launch {
            val solars = repository.getSolarListAsia()
            solarLiveData.postValue(solars)

        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()

}
