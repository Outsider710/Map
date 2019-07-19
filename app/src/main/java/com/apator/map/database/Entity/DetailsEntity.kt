package com.apator.map.database.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Details")
data class DetailsEntity(
    @PrimaryKey val id:String,
@ColumnInfo(name="location")val location:String

)