package dev.krazymanj.shopito.model

import com.squareup.moshi.Json

data class GeoReverseResponse(
    val error: String? = null,
    @Json(name = "display_name")
    val displayName: String? = null,
//    val address: AddressDetails? = null,
    val lat: Double? = null,
    val lon: Double? = null,
)

//data class AddressDetails(
//    val shop: String? = null,
//    val suburb: String? = null,
//    val town: String? = null
//)
