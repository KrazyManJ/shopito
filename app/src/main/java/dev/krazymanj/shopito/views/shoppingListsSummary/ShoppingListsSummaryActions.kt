package dev.krazymanj.shopito.views.shoppingListsSummary

import dev.krazymanj.shopito.database.entities.ShoppingItem

interface ShoppingListsSummaryActions {
    fun loadData()
    fun deleteShoppingItem(shoppingItem: ShoppingItem)
    fun changeItemCheckState(shoppingItem: ShoppingItem, state: Boolean)
}