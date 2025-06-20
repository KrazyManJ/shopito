package dev.krazymanj.shopito.navigation

import kotlinx.serialization.Serializable

sealed class Destination {
    @Serializable
    data object ShoppingListsSummaryScreen : Destination()

    @Serializable
    data object ShoppingListsScreen : Destination()

    @Serializable
    data class AddEditShoppingList(
        val shoppingListId: Long?
    ) : Destination()

    @Serializable
    data class ViewShoppingList(
        val shoppingListId: Long
    ): Destination()

    @Serializable
    data class AddEditShoppingItem(
        val shoppingListId: Long,
        val shoppingItemId: Long?
    ): Destination()

    @Serializable
    data class MapLocationPickerScreen(
        val latitude: Double?,
        val longitude: Double?
    ) : Destination()

    @Serializable
    data object SettingsScreen : Destination()

    @Serializable
    data object ItemKeywordsListScreen : Destination()
}

