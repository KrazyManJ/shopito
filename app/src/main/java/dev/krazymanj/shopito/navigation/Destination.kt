package dev.krazymanj.shopito.navigation

import dev.krazymanj.shopito.model.Location
import kotlinx.serialization.Serializable

sealed class Destination {
    @Serializable
    data object ShoppingListsSummaryScreen : Destination()

    @Serializable
    data object ShoppingListsScreen : Destination()

    @Serializable
    data class ViewShoppingList(
        val shoppingListId: String
    ): Destination()

    @Serializable
    data class MapLocationPickerScreen(
        val location: Location?,
        val navSource: NavStateKey<Location>
    ) : Destination()

    @Serializable
    data object SettingsScreen : Destination()

    @Serializable
    data object MapViewScreen : Destination()

    @Serializable
    data class LocationItemsList(
        val location: Location
    ) : Destination()

    @Serializable
    data class ScanShoppingListScreen(
        val shoppingListId: String
    ) : Destination()

    @Serializable
    data object AuthScreen : Destination()
}

