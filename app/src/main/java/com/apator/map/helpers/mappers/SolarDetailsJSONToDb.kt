package com.apator.map.helpers.mappers


import com.apator.map.database.Entity.DetailsEntity
import com.apator.map.helpers.formater.StringDoubleArraySerializer
import com.apator.map.model.singlesolar.Solar

object SolarDetailsJSONToDb {
    private val serializer = StringDoubleArraySerializer()
    fun map(solar: Solar) = DetailsEntity(
        solar.inputs!!.file_id!!,
        solar.stationInfo!!.elev!!,
        solar.stationInfo.tz!!,
        solar.stationInfo.location!!,
        solar.stationInfo.city!!,
        solar.stationInfo.state!!,
        serializer.serialize(solar.outputs!!.acMonthly),
        serializer.serialize(solar.outputs.dcMonthly),
        serializer.serialize(solar.outputs.poaMonthly),
        serializer.serialize(solar.outputs.solradMonthly)
    )
}