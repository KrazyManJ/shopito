package dev.krazymanj.shopito.ui.screens.settings

import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.datastore.DataStoreKey
import dev.krazymanj.shopito.model.TokenData
import dev.krazymanj.shopito.navigation.StartDestinationSetting


data class SettingsUIState(
    val isLoading: Boolean = true,

    val startNavigationSetting: Boolean = DataStoreKey.GoogleMapsStartNavigationKey.default,
    val startScreenSetting: StartDestinationSetting = DataStoreKey.StartScreenSetting.default,
    val startShoppingListId: String? = DataStoreKey.StartShoppingListId.default,

    val shoppingLists: List<ShoppingList> = emptyList(),

    val loggedData: TokenData? = null,

    val isSyncing: Boolean = false,
    val lastTimeSynced: Long? = null,
    val syncResult: String? = null
) {
    fun isLoggedIn() = loggedData != null
}
