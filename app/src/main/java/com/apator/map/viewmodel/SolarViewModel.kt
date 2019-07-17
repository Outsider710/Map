package com.apator.map.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apator.map.ApiFactory
import com.apator.map.model.singlesolar.Solar
import com.apator.map.repositiry.SolarRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class SolarViewModel: ViewModel()
{
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository = SolarRepository(ApiFactory.soalrApi)


    val solarLiveData = MutableLiveData<List<Solar>>()

    fun fetchSolars(){
        scope.launch {
            val soalars = repository.getSolars()
            solarLiveData.postValue(soalars)
        }
    }


    fun cancelAllRequests() = coroutineContext.cancel()

}
