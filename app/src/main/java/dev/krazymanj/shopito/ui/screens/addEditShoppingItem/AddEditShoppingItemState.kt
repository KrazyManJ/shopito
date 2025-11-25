package dev.krazymanj.shopito.ui.screens.addEditShoppingItem

import dev.krazymanj.shopito.database.entities.ItemKeyword
import dev.krazymanj.shopito.database.entities.ShoppingItem


data class AddEditShoppingItemState(
    val shoppingItem: ShoppingItem = ShoppingItem.default(),
    val itemKeywords: List<ItemKeyword> = emptyList(),

    val amountInput: String = "0",

    val nameInputError: Int? = null,
    val amountInputError: Int? = null,

    val isSaved: Boolean = false,
){
    fun isInputValid(): Boolean = shoppingItem.itemName.isNotBlank() && amountInput.toIntOrNull() != null
}
