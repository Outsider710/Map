//package com.apator.map.database.repository
//
//import android.app.Application
//import androidx.annotation.WorkerThread
//import androidx.lifecycle.Observer
//import com.apator.map.database.Entity.SolarEntity
//import com.apator.map.database.SolarDatabase
//
//class SolarDbRepository(application: Application) {
//
//    private val database = SolarDatabase.getDatabse(application)
//    val solarDao = database.solarDao()
//
//    @WorkerThread
//    suspend fun insertAllSolars(solars:List<SolarEntity>)
//    {
//
//        solarDao.insertAllSolars(*solars.toTypedArray())
//
//    } }
