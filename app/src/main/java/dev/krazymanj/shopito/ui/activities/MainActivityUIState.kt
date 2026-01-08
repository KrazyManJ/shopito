package dev.krazymanj.shopito.ui.activities

import dev.krazymanj.shopito.datastore.DataStoreKey
import dev.krazymanj.shopito.navigation.Destination
import dev.krazymanj.shopito.navigation.StartDestinationSetting


data class MainActivityUIState(
    val isLoading: Boolean = true,
    val startScreenSetting: StartDestinationSetting = DataStoreKey.StartScreenSetting.default,
    val startShoppingListId: String? = null
) {
    fun resolveStartDestination(): Destination {
        return when (startScreenSetting) {
            StartDestinationSetting.ShoppingListScreen ->
                if (startShoppingListId != null)
                    Destination.ViewShoppingList(startShoppingListId)
                else
                    Destination.ShoppingListsSummaryScreen
            StartDestinationSetting.ShoppingSummaryScreen -> Destination.ShoppingListsSummaryScreen
        }
    }
}
