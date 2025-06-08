package dev.krazymanj.shopito.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey

sealed class DataStoreKeys<T>(val key: Preferences.Key<T>, val default: T) {
    data object GoogleMapsStartNavigationKey : DataStoreKeys<Boolean>(
        key = booleanPreferencesKey("google_maps_start_navigation_key"),
        default = false
    )
}