package com.apator.map.database.repositories

import android.content.Context
import androidx.annotation.WorkerThread
import com.apator.map.database.SolarDatabase
import com.apator.map.database.entitis.SolarEntity

class SolarDbRepository(context: Context) {
    private val db = SolarDatabase.getDatabase(context)
    val solarDao = db.solarDao()
    val solars = solarDao.getAllSolars()

    @WorkerThread
    suspend fun insertSolar(solarEntity: SolarEntity) {
        solarDao.insertSolar(solarEntity)
    }

}
