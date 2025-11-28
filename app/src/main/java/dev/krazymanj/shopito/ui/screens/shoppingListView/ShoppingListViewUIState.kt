package dev.krazymanj.shopito.ui.screens.shoppingListView

import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.model.Location
import dev.krazymanj.shopito.model.SavedLocation


data class ShoppingListViewUIState(
    val isLoading: Boolean = true,

    val shoppingList: ShoppingList? = null,
    val shoppingItems: List<ShoppingItem> = emptyList(),
    val currentShownShoppingItem: ShoppingItem? = null,
    val placesOptions: Set<SavedLocation> = linkedSetOf(),

    val itemInput: String = "",
    val dateInput: Long? = null,

    val locationInput: Location? = null,

    val isCreated: Boolean = false,
    val lastDeletedItem: ShoppingItem? = null,
)
