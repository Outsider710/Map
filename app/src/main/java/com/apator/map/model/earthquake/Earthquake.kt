package com.apator.map.model.earthquake


import com.google.gson.annotations.SerializedName

data class Earthquake(
    @SerializedName("bbox")
    val bbox: List<Double?>?,
    @SerializedName("features")
    val features: List<Feature?>?,
    @SerializedName("metadata")
    val metadata: Metadata?,
    @SerializedName("type")
    val type: String?
)