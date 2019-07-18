package com.apator.map

import com.apator.map.model.singlesolar.Solar
import com.apator.map.model.solarlist.SolarsList
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SolarApi {

    @GET("api/pvwatts/v6.json?api_key={apiKey}&lat=36&lon=86&system_capacity=4&azimuth=180&tilt=40&array_type=1&module_type=1&losses=10")
    fun getSolarDetails(@Path("apiKey") apiKey:String):Deferred<Response<Solar>>

    @GET("api/solar/data_query/v1.json?api_key=5Gibh08OmtfPAZXEF4qhLJc7ckXxBPL8PBlst9ws&lat=40&lon=-105&radius=2000&all=1")
    fun getSolarLsitAmerica(): Deferred<Response<SolarsList>>

    @GET("api/solar/data_query/v1.json?api_key=5Gibh08OmtfPAZXEF4qhLJc7ckXxBPL8PBlst9ws&lat=40&lon=85&radius=2000&all=1")
    fun getSolarLsitAsia(): Deferred<Response<SolarsList>>


}