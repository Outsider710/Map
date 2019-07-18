package com.apator.map.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.apator.map.database.entitis.SolarEntity

@Dao
interface SolarDao {
    @Insert
    fun insertSolar(solar: SolarEntity)

    @Query("SELECT * FROM Solar")
    fun getAllSolars(): LiveData<List<SolarEntity>>


}
