package dev.krazymanj.shopito.views.mapLocationPicker

import dev.krazymanj.shopito.model.Location

interface MapLocationPickerActions {
    fun locationChanged(location: Location)
}