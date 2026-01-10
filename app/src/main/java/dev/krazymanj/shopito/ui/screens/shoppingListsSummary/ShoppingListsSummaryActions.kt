package dev.krazymanj.shopito.ui.screens.shoppingListsSummary

import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.model.ShoppingItemWithList

interface ShoppingListsSummaryActions {
    fun loadData(withLoadingState: Boolean = true)
    fun deleteShoppingItem(shoppingItem: ShoppingItem)
    fun changeItemCheckState(shoppingItem: ShoppingItem, state: Boolean)
    fun setCurrentViewingShoppingItem(shoppingItem: ShoppingItemWithList?)
}