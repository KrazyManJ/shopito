package dev.krazymanj.shopito.navigation

import kotlinx.serialization.Serializable

sealed class Destination {
    @Serializable
    data object ShoppingListsSummaryScreen : Destination()
    @Serializable
    data object ShoppingListsScreen : Destination()
}