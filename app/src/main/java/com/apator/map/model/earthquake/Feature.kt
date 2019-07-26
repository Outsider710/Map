package com.apator.map.model.earthquake


import com.google.gson.annotations.SerializedName

data class Feature(
    @SerializedName("geometry")
    val geometry: Geometry?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("properties")
    val properties: Properties?,
    @SerializedName("type")
    val type: String?
)