package dev.krazymanj.shopito.views.addEditShoppingItem

import dev.krazymanj.shopito.database.entities.ItemKeyword
import dev.krazymanj.shopito.database.entities.ShoppingItem


data class AddEditShoppingItemState(
    val shoppingItem: ShoppingItem = ShoppingItem.default(),
    val itemKeywords: List<ItemKeyword> = emptyList(),

    val amountInput: String = "0",

    val isSaved: Boolean = false,
)
