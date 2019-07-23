package com.apator.map.database.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apator.map.database.Entity.DetailsEntity
import com.apator.map.database.Entity.SolarEntity

@Dao
interface SolarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllSolars(vararg solarEntity: SolarEntity)

    @Query("SELECT * FROM solar")
    fun getAllSolars(): LiveData<List<SolarEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetails(detailsEntity: DetailsEntity)

    @Query("SELECT * FROM details WHERE id == :id")
    fun getDetailsById(id:String):LiveData<DetailsEntity?>


}