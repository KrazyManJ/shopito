package dev.krazymanj.shopito.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "latitude")
    val latitude: Double?,
    @Json(name = "longitude")
    val longitude: Double?
)