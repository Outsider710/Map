package com.apator.map.database.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Solar")
data class SolarEntity(@PrimaryKey val id:String,
                       @ColumnInfo(name = "lat") var lat:Double,
                       @ColumnInfo(name = "lon") var lon:Double)