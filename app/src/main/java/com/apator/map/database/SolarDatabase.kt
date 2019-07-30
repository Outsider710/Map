package com.apator.map.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.apator.map.database.Entity.DetailsEntity
import com.apator.map.database.Entity.SolarEntity
import com.apator.map.database.dao.SolarDao

@Database(entities = [SolarEntity::class,DetailsEntity::class], version = 2, exportSchema = false)
abstract class SolarDatabase : RoomDatabase() {
    abstract fun solarDao(): SolarDao

    companion object {
        fun getDatabse(context: Context) = Room.databaseBuilder(context, SolarDatabase::class.java, "APP_database").build()
    }
}