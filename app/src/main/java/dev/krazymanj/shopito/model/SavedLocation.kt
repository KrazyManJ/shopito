package dev.krazymanj.shopito.model

import kotlinx.serialization.Serializable

@Serializable
data class SavedLocation(
    val label: String,
    val location: Location
)