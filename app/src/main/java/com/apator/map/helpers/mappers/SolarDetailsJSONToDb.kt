package com.apator.map.helpers.mappers

import com.apator.map.database.Entity.DetailsEntity
import com.apator.map.helpers.formater.DoubleToString
import com.apator.map.model.singlesolar.Solar

object SolarDetailsJSONToDb {
    fun map(solar: Solar) = DetailsEntity(
        solar.inputs!!.file_id!!,
        solar.stationInfo!!.elev!!,
        solar.stationInfo.tz!!,
        solar.stationInfo.location!!,
        solar.stationInfo.city!!,
        solar.stationInfo.state!!,
        DoubleToString.format(';',solar.outputs!!.acMonthly),
        DoubleToString.format(';',solar.outputs.dcMonthly),
       DoubleToString.format(';', solar.outputs.poaMonthly),
       DoubleToString.format(';', solar.outputs.solradMonthly)
    )
}