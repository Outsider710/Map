package com.apator.map.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.apator.map.database.Dao.SolarDao
import com.apator.map.database.Entity.SolarEntity

@Database(entities = [SolarEntity::class], version = 1, exportSchema = false)
abstract class SolarDatabase : RoomDatabase() {
    abstract fun solarDao(): SolarDao

    companion object {
        fun getDatabse(context: Context) = Room.databaseBuilder(context, SolarDatabase::class.java, "Solar").build()
    }
}