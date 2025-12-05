package dev.krazymanj.shopito.ui.screens.shoppingListsSummary

import dev.krazymanj.shopito.model.ShoppingItemWithList
import dev.krazymanj.shopito.database.entities.ShoppingItem

interface ShoppingListsSummaryActions {
    fun loadData(withLoadingState: Boolean = true)
    fun deleteShoppingItem(shoppingItem: ShoppingItem)
    fun changeItemCheckState(shoppingItem: ShoppingItem, state: Boolean)
    fun setCurrentViewingShoppingItem(shoppingItem: ShoppingItemWithList?)
    fun saveLastDeletedItem(shoppingItem: ShoppingItem)
    fun addBackDeletedItem()
}