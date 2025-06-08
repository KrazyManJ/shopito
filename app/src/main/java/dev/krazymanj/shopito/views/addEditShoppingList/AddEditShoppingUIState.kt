package dev.krazymanj.shopito.views.addEditShoppingList

import dev.krazymanj.shopito.database.entities.ShoppingList


data class AddEditShoppingUIState(
    val shoppingList: ShoppingList = ShoppingList.default(),

    val nameInputError: Int? = null,

    val isSaved: Boolean = false,
    val isDeleted: Boolean = false,
) {
    fun isInputValid(): Boolean = shoppingList.name.isNotBlank()
}
