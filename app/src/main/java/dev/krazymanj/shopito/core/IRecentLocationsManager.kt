package dev.krazymanj.shopito.core

import dev.krazymanj.shopito.model.SavedLocation

interface IRecentLocationsManager {
    fun addToRecentLocations(savedLocation: SavedLocation)
    fun removeFromRecentLocations(savedLocation: SavedLocation)
}