package dev.krazymanj.shopito.ui.screens.mapView

import dev.krazymanj.shopito.model.Location

data class MapViewUIState(
    val isLoading: Boolean = true,
    val locations: List<Location> = emptyList()
)
