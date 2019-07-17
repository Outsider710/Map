package com.apator.map.model.singlesolar


import com.google.gson.annotations.SerializedName

data class SscInfo(
    @SerializedName("build")
    val build: String?,
    @SerializedName("version")
    val version: Int?
)