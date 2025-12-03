package dev.krazymanj.shopito.ui.screens.locationItemsList

import dev.krazymanj.shopito.database.ShoppingItemWithList
import dev.krazymanj.shopito.database.entities.ShoppingItem

data class LocationItemsListUIState(
    val isLoading: Boolean = true,
    val items: List<ShoppingItemWithList> = emptyList(),
    val currentShownShoppingItem: ShoppingItemWithList? = null,
    val lastDeletedItem: ShoppingItem? = null,

    val locationLabel: String? = null,
    val locationError: Int? = null
)
