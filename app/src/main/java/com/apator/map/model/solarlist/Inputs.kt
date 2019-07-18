package com.apator.map.model.solarlist


import com.google.gson.annotations.SerializedName

data class Inputs(
    @SerializedName("all")
    val all: String?,
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("lon")
    val lon: String?,
    @SerializedName("radius")
    val radius: String?
)