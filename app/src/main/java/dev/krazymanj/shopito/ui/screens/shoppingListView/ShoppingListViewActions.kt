package dev.krazymanj.shopito.ui.screens.shoppingListView

import dev.krazymanj.shopito.database.entities.ShoppingItem

interface ShoppingListViewActions {
    fun loadShoppingListData(shoppingListId: String)
    fun deleteShoppingItem(shoppingItem: ShoppingItem)
    fun changeItemCheckState(shoppingItem: ShoppingItem, state: Boolean)
    fun clearIsCreatedState()
    fun openShoppingItemDetails(shoppingItem: ShoppingItem?)
    fun saveLastDeletedItem(shoppingItem: ShoppingItem)
    fun addBackDeletedItem()
}