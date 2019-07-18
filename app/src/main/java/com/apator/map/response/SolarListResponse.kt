package com.apator.map.response

import com.apator.map.model.singlesolar.Solar
import com.apator.map.model.solarlist.AllStation
import com.apator.map.model.solarlist.SolarsList

data class SolarListResponse(
    val results: List<SolarsList>

){
}