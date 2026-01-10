package dev.krazymanj.shopito.ui.screens.locationItemsList

import dev.krazymanj.shopito.model.ShoppingItemWithList

data class LocationItemsListUIState(
    val isLoading: Boolean = true,
    val items: List<ShoppingItemWithList> = emptyList(),
    val currentShownShoppingItem: ShoppingItemWithList? = null,

    val locationLabel: String? = null,
    val locationError: Int? = null
)
