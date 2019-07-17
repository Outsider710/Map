package com.apator.map.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apator.map.ApiFactory
import com.apator.map.model.singlesolar.Solar
import com.apator.map.model.solarlist.SolarsList
import com.apator.map.repositiry.SolarListRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class SolarListViewModel: ViewModel()
{
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository = SolarListRepository(ApiFactory.soalrApi)


    val solarLiveData = MutableLiveData<SolarsList>()

    fun fetchSolars(){
        scope.launch {
            val solars = repository.getSolarList()
            solarLiveData.postValue(solars)
        }
    }


    fun cancelAllRequests() = coroutineContext.cancel()

}
