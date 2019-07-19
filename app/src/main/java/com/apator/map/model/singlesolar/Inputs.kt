package com.apator.map.model.singlesolar


import com.google.gson.annotations.SerializedName

data class Inputs(
    @SerializedName("array_type")
    val arrayType: String?,
    @SerializedName("azimuth")
    val azimuth: String?,
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("lon")
    val lon: String?,
    @SerializedName("file_id")
    val file_id:String?,
    @SerializedName("losses")
    val losses: String?,
    @SerializedName("module_type")
    val moduleType: String?,
    @SerializedName("system_capacity")
    val systemCapacity: String?,
    @SerializedName("tilt")
    val tilt: String?
)