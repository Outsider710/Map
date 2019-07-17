package com.apator.map.model.solarlist


import com.google.gson.annotations.SerializedName

data class SolarsList(
    @SerializedName("errors")
    val errors: List<Any?>?,
    @SerializedName("inputs")
    val inputs: Inputs?,
    @SerializedName("metadata")
    val metadata: Metadata?,
    @SerializedName("outputs")
    val outputs: Outputs?,
    @SerializedName("version")
    val version: String?,
    @SerializedName("warnings")
    val warnings: List<String?>?
)