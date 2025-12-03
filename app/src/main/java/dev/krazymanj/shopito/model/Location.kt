package dev.krazymanj.shopito.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.krazymanj.shopito.extension.round
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class Location(
    @field:Json(name = "latitude")
    val latitude: Double,
    @field:Json(name = "longitude")
    val longitude: Double
) : ClusterItem {
    fun toLatLng(): LatLng = LatLng(latitude,longitude)

    override fun toString(): String {
        return "${latitude.round()} ${longitude.round()}"
    }

    override fun getPosition(): LatLng {
        return this.toLatLng()
    }

    override fun getTitle(): String? {
        return null
    }

    override fun getSnippet(): String? {
        return null
    }

    override fun getZIndex(): Float? {
        return 0.0f
    }
}