package dev.krazymanj.shopito.ui.screens.shoppingListView

import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.model.Location

interface ShoppingListViewActions {
    fun loadShoppingListData(shoppingListId: Long)
    fun deleteShoppingItem(shoppingItem: ShoppingItem)
    fun changeItemCheckState(shoppingItem: ShoppingItem, state: Boolean)
    fun clearIsCreatedState()
    fun openShoppingItemDetails(shoppingItem: ShoppingItem?)
    fun changeCurrentViewingShoppingItemName(newName: String)
    fun changeCurrentViewingShoppingItemCheckState(newState: Boolean)
    fun changeCurrentViewingShoppingItemAmount(newAmount: Int)
    fun changeCurrentViewingShoppingItemDate(newDate: Long?)
    fun changeCurrentViewingShoppingItemLocation(newLocation: Location?)
    fun updateCurrentViewingShoppingItem()
    fun deleteCurrentViewingShoppingItem()
}