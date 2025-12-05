package dev.krazymanj.shopito.ui.screens.locationItemsList

import dev.krazymanj.shopito.model.ShoppingItemWithList
import dev.krazymanj.shopito.database.entities.ShoppingItem

interface LocationItemsListActions {
    fun openShoppingItemDetails(shoppingItem: ShoppingItemWithList?)
    fun saveLastDeletedItem(shoppingItem: ShoppingItem)
    fun addBackDeletedItem()
    fun deleteShoppingItem(item: ShoppingItem)
    fun changeItemCheckState(shoppingItem: ShoppingItem, state: Boolean)
}