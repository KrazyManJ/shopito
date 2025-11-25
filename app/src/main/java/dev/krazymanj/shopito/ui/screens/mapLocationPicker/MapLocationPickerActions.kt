package dev.krazymanj.shopito.ui.screens.mapLocationPicker

import dev.krazymanj.shopito.model.Location

interface MapLocationPickerActions {
    fun locationChanged(location: Location)
}