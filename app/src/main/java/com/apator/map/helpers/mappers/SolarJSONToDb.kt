package com.apator.map.helpers.mappers


import com.apator.map.database.Entity.SolarEntity
import com.apator.map.model.solarlist.AllStation

object SolarJSONToDb {
    fun map(station: AllStation) = SolarEntity(
        station.id!!,
        station.lat!!.toDouble(),
        station.lon!!.toDouble()
    )
}