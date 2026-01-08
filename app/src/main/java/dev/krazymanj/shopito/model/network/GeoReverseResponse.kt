package dev.krazymanj.shopito.model.network

import com.squareup.moshi.Json

data class GeoReverseResponse(
    val error: String? = null,
    @param:Json(name = "display_name")
    val displayName: String? = null,
//    val address: AddressDetails? = null,
    val lat: Double? = null,
    val lon: Double? = null,
)