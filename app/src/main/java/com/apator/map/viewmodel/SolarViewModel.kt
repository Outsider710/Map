package com.apator.map.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apator.map.ApiFactory
import com.apator.map.database.Entity.SolarEntity
import com.apator.map.model.solarlist.SolarsList
import com.apator.map.repositiry.SolarListRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class SolarViewModel(application: Application) : AndroidViewModel(application) {
    //
    //  database
    //

    val repository = SolarListRepository(ApiFactory.soalrApi,application)


    fun insertAllStations(solars: List<SolarEntity>) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertAllSolars(solars)
    }

    fun getAllSolars() = repository.solarDao.getAllSolars()


    //
    // JSON
    //

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)
    


    val solarLiveData = MutableLiveData<SolarsList>()

    fun fetchSolarsAmerica() {
        scope.launch {
            var solars = repository.getSolarListAmerica()
            solarLiveData.postValue(solars)

        }
    }

    fun fetchSolarsAsia() {
        scope.launch {
            var solars = repository.getSolarListAsia()
            solarLiveData.postValue(solars)

        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()

}
