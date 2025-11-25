package dev.krazymanj.shopito.ui.screens.settings

import dev.krazymanj.shopito.datastore.DataStoreKeys


data class SettingsUIState(
    val loading: Boolean = true,
    val startNavigationSetting: Boolean = DataStoreKeys.GoogleMapsStartNavigationKey.default
)
