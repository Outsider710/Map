package com.apator.map.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.lang.reflect.Array

data class Details(
    val id: String,
    val elevation:Double,
    val tz: Double,
    val location: String,
    val city: String,
    val state: String,
    val acMontly: ArrayList<Double>,
    val dcMontly: ArrayList<Double>,
    val poaMontly:ArrayList<Double>,
    val solradMontly: ArrayList<Double>

    )