package com.apator.map

import com.apator.map.model.singlesolar.Solar
import com.apator.map.model.solarlist.SolarsList
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SolarApi {

    @GET("api/pvwatts/v6.{format}?api_key=5Gibh08OmtfPAZXEF4qhLJc7ckXxBPL8PBlst9ws&system_capacity=4&azimuth=180&tilt=40&array_type=1&module_type=1&losses=10")
    fun getSolarDetails(
        @Path("format") format: String,
        //@Query("api_key") api_key: String,
        @Query("file_id") file_id:String
        //@Query("lat") lat: Double
//        @Query("lon") lon: Double,
        // @Query("system_capacity") system_capacity: Double
//        @Query("azimuth") azimuth: Double,
//        @Query("tilt") tilt: Double,
//        @Query("array_type") array_type: Int,
//        @Query("module_type") module_type: Int,
//        @Query("losses") losses: Double
    ): Deferred<Response<Solar>>

    @GET("api/solar/data_query/v1.json?api_key=5Gibh08OmtfPAZXEF4qhLJc7ckXxBPL8PBlst9ws&lat=40&lon=-105&radius=2000&all=1")
    fun getSolarLsitAmerica(): Deferred<Response<SolarsList>>

    @GET("api/solar/data_query/v1.json?api_key=5Gibh08OmtfPAZXEF4qhLJc7ckXxBPL8PBlst9ws&lat=40&lon=85&radius=2000&all=1")
    fun getSolarLsitAsia(): Deferred<Response<SolarsList>>


}