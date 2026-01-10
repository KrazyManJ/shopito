package dev.krazymanj.shopito.ui.screens.locationItemsList

import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.model.ShoppingItemWithList

interface LocationItemsListActions {
    fun openShoppingItemDetails(shoppingItem: ShoppingItemWithList?)
    fun deleteShoppingItem(item: ShoppingItem)
    fun changeItemCheckState(shoppingItem: ShoppingItem, state: Boolean)
}