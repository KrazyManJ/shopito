package dev.krazymanj.shopito.ui.screens.settings

import dev.krazymanj.shopito.datastore.DataStoreKey


data class SettingsUIState(
    val loading: Boolean = true,
    val startNavigationSetting: Boolean = DataStoreKey.GoogleMapsStartNavigationKey.default
)
