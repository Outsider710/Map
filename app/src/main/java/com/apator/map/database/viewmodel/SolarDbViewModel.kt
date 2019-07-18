//package com.apator.map.database.viewmodel
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.viewModelScope
//import com.apator.map.database.Entity.SolarEntity
//import com.apator.map.database.repository.SolarDbRepository
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//
//class SolarDbViewModel(application: Application) : AndroidViewModel(application) {
//    val solarRepository = SolarDbRepository(application)
//
//
//    fun insertAllStations(solars: List<SolarEntity>) = viewModelScope.launch(Dispatchers.IO) {
//        solarRepository.insertAllSolars(solars)
//    }
//
//    fun getAllSolars() = solarRepository.solarDao.getAllSolars()
//}
//
