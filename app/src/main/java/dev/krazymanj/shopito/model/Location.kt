package dev.krazymanj.shopito.model

import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.krazymanj.shopito.extension.round
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class Location(
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double
) {
    fun toLatLng(): LatLng = LatLng(latitude,longitude)

    override fun toString(): String {
        return "${latitude.round()} ${longitude.round()}"
    }
}