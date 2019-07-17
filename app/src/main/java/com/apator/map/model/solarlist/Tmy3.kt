package com.apator.map.model.solarlist


import com.google.gson.annotations.SerializedName

data class Tmy3(
    @SerializedName("city")
    val city: String?,
    @SerializedName("distance")
    val distance: Int?,
    @SerializedName("elevation")
    val elevation: Int?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("lat")
    val lat: Double?,
    @SerializedName("lon")
    val lon: Double?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("timezone")
    val timezone: Int?
)