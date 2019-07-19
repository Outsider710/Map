package com.apator.map.database.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Details")
data class DetailsEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "elevation") val elevation:Double,
    @ColumnInfo(name = "tz") val tz: Double,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "state") val state: String,
    @ColumnInfo(name = "acMontly") val acMontly: String,
    @ColumnInfo(name = "dcMontly") val dcMontly: String,
    @ColumnInfo(name = "poaMontly") val poaMontly:String,
    @ColumnInfo(name = "solradMontly") val solradMontly: String

)


