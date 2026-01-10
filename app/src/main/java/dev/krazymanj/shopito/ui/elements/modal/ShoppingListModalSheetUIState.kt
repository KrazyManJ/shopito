package dev.krazymanj.shopito.ui.elements.modal

import dev.krazymanj.shopito.database.entities.ShoppingList

data class ShoppingListModalSheetUIState(
    val isLoading: Boolean = true,
    val shoppingList: ShoppingList = ShoppingList.default(),
    val isEdit: Boolean = false,
) {
    fun isFormValid() = shoppingList.name.isNotBlank()
}
