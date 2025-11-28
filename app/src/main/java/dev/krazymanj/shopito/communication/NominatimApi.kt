package dev.krazymanj.shopito.communication

import dev.krazymanj.shopito.BuildConfig
import dev.krazymanj.shopito.model.GeoReverseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NominatimApi {
    @GET("reverse")
    @Headers(
        "Content-Type: application/json",
        "User-Agent: ${BuildConfig.NOMINATIM_USER_AGENT}"
    )
    suspend fun reverse(
        @Query("format") format: String = "jsonv2",
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): Response<GeoReverseResponse>
}