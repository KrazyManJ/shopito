package dev.krazymanj.shopito.views.addEditShoppingList

import dev.krazymanj.shopito.database.entities.ShoppingList


data class AddEditShoppingUIState(
    val shoppingList: ShoppingList = ShoppingList("",""),

    val isSaved: Boolean = false,
    val isDeleted: Boolean = false,
)
