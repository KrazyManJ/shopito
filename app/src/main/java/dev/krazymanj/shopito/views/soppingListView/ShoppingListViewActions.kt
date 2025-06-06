package dev.krazymanj.shopito.views.soppingListView

import dev.krazymanj.shopito.database.entities.ShoppingItem

interface ShoppingListViewActions {
    fun loadShoppingListData(shoppingListId: Long)
    fun deleteShoppingItem(shoppingItem: ShoppingItem)
    fun changeItemCheckState(shoppingItem: ShoppingItem, state: Boolean)
}