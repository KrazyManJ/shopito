package dev.krazymanj.shopito.ui.screens.shoppingLists

import dev.krazymanj.shopito.database.entities.ShoppingList


data class ShoppingListsUIState(
    val loading: Boolean = true,
    val lists: List<ShoppingList> = emptyList()
)
