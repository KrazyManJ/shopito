package dev.krazymanj.shopito.ui.screens.shoppingListsSummary

import dev.krazymanj.shopito.model.ShoppingItemWithList
import dev.krazymanj.shopito.database.entities.ShoppingItem


data class ShoppingListsSummaryUIState(
    val isLoading: Boolean = true,
    val shoppingItemsWithDate: Map<Long, List<ShoppingItemWithList>> = emptyMap(),
    val shoppingItemsWithoutDate: List<ShoppingItemWithList> = emptyList(),
    val currentShownShoppingItem: ShoppingItemWithList? = null,
    val lastDeletedItem: ShoppingItem? = null,
) {
    fun hasNoData(): Boolean {
        return shoppingItemsWithDate.isEmpty() && shoppingItemsWithoutDate.isEmpty()
    }
}
