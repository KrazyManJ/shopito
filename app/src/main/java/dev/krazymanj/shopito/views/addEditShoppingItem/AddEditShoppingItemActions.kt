package dev.krazymanj.shopito.views.addEditShoppingItem

import dev.krazymanj.shopito.model.Location

interface AddEditShoppingItemActions {
    fun loadShoppingListItem(shoppingListId: Long ,shoppingItemId: Long?)

    fun onItemNameChange(itemName: String)
    fun onAmountChange(amount: String)
    fun onBuyTimeChanged(buyTime: Long?)

    fun submit()
    fun onLocationChanged(location: Location?)
}