package com.apator.map.model.earthquake


import com.google.gson.annotations.SerializedName

data class Properties(
    @SerializedName("alert")
    val alert: Any?,
    @SerializedName("cdi")
    val cdi: Any?,
    @SerializedName("code")
    val code: String?,
    @SerializedName("detail")
    val detail: String?,
    @SerializedName("dmin")
    val dmin: Double?,
    @SerializedName("felt")
    val felt: Any?,
    @SerializedName("gap")
    val gap: Int?,
    @SerializedName("ids")
    val ids: String?,
    @SerializedName("mag")
    val mag: Double?,
    @SerializedName("magType")
    val magType: String?,
    @SerializedName("mmi")
    val mmi: Any?,
    @SerializedName("net")
    val net: String?,
    @SerializedName("nst")
    val nst: Int?,
    @SerializedName("place")
    val place: String?,
    @SerializedName("rms")
    val rms: Double?,
    @SerializedName("sig")
    val sig: Int?,
    @SerializedName("sources")
    val sources: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("time")
    val time: Long?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("tsunami")
    val tsunami: Int?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("types")
    val types: String?,
    @SerializedName("tz")
    val tz: Int?,
    @SerializedName("updated")
    val updated: Long?,
    @SerializedName("url")
    val url: String?
)