package dev.krazymanj.shopito.ui.screens.mapLocationPicker

import dev.krazymanj.shopito.model.Location


data class MapLocationPickerUIState(
    val location: Location = Location(
        latitude = 49.8175,
        longitude = 15.4730
    )
)
