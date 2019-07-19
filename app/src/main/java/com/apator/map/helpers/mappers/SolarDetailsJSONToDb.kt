package com.apator.map.helpers.mappers

import com.apator.map.database.Entity.DetailsEntity
import com.apator.map.model.singlesolar.Solar

object SolarDetailsJSONToDb {
        fun map(solar: Solar) = DetailsEntity(solar.inputs!!.file_id!!, solar.stationInfo!!.location!!)
}