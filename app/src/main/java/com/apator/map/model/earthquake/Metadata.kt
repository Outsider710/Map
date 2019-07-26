package com.apator.map.model.earthquake


import com.google.gson.annotations.SerializedName

data class Metadata(
    @SerializedName("api")
    val api: String?,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("generated")
    val generated: Long?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("url")
    val url: String?
)