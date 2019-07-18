package com.apator.map.database.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel

import com.apator.map.database.entitis.SolarEntity
import com.apator.map.database.repositories.SolarDbRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SolarDbViewModel(applcation: Application) : AndroidViewModel(applcation) {
    private val solarRepository = SolarDbRepository(applcation)

    fun getAllSolars() = solarRepository.solars


    fun insertSolar(solarEntity: SolarEntity) = viewModelScope.launch(Dispatchers.IO) {
        solarRepository.insertSolar(solarEntity)
    }
}
