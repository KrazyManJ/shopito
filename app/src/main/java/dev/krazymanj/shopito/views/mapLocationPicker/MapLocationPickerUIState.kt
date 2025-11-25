package dev.krazymanj.shopito.views.mapLocationPicker

import dev.krazymanj.shopito.model.Location


data class MapLocationPickerUIState(
    val location: Location = Location(
        latitude = 49.8175,
        longitude = 15.4730
    )
)
