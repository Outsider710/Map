package com.apator.map.database.entitis

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Solar")
data class SolarEntity(
    @PrimaryKey() val id:String,
    @ColumnInfo(name = "lat") val lat:Double,
    @ColumnInfo(name = "lon") val lon:Double
)