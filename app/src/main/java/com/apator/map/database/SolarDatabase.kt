package com.apator.map.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.apator.map.database.dao.SolarDao
import com.apator.map.database.entitis.SolarEntity

@Database(entities = [SolarEntity::class], version = 1)
abstract class SolarDatabase : RoomDatabase() {
    abstract fun solarDao(): SolarDao

    companion object {
        var INSTANCE: SolarDatabase? = null

        fun getDatabase(context: Context): SolarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = buildDatabase(context)
                INSTANCE = instance
                return instance
            }
        }


        private fun buildDatabase(context: Context): SolarDatabase {
            return Room.databaseBuilder(context, SolarDatabase::class.java, "Solar")
                .build()
        }

    }
}

