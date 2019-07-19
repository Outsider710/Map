package com.apator.map.helpers.mappers

import com.apator.map.database.Entity.DetailsEntity
import com.apator.map.helpers.formater.StringToDouble
import com.apator.map.model.Details

object SolarDetailsToModel {
    fun map(data: DetailsEntity) = Details(
        data.id,
        data.elevation,
        data.tz,
        data.location,
        data.city,
        data.state,
        StringToDouble.format(';',data.acMontly),
        StringToDouble.format(';',data.dcMontly),
        StringToDouble.format(';',data.poaMontly),
        StringToDouble.format(';',data.solradMontly)
    )
}