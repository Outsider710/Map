package com.apator.map.model.solarlist


import com.google.gson.annotations.SerializedName

data class AllStation(
    @SerializedName("city")
    val city: String?,
    @SerializedName("country")
    val country: Any?,
    @SerializedName("dataset")
    val dataset: String?,
    @SerializedName("distance")
    val distance: Int?,
    @SerializedName("elevation")
    val elevation: Int?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("lat")
    val lat: String?,
    @SerializedName("lon")
    val lon: String?,
    @SerializedName("resolution")
    val resolution: Int?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("timezone")
    val timezone: Int?
)