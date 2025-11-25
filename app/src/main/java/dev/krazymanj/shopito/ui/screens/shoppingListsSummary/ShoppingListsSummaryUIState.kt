package dev.krazymanj.shopito.ui.screens.shoppingListsSummary

import dev.krazymanj.shopito.database.ShoppingItemWithList


data class ShoppingListsSummaryUIState(
    val loading: Boolean = true,
    val shoppingItemsWithDate: Map<Long, List<ShoppingItemWithList>> = emptyMap(),
    val shoppingItemsWithoutDate: List<ShoppingItemWithList> = emptyList(),
)
