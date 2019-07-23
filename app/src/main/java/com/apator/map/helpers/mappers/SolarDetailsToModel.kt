package com.apator.map.helpers.mappers

import com.apator.map.database.Entity.DetailsEntity
import com.apator.map.helpers.formater.StringDoubleArraySerializer
import com.apator.map.model.Details

object SolarDetailsToModel {
    private val serializer = StringDoubleArraySerializer()
    fun map(data: DetailsEntity) = Details(
        data.id,
        data.elevation,
        data.tz,
        data.location,
        data.city,
        data.state,
        serializer.deserialize(data.acMontly),
        serializer.deserialize(data.dcMontly),
        serializer.deserialize(data.poaMontly),
        serializer.deserialize(data.solradMontly)
    )
}