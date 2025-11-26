package dev.krazymanj.shopito.ui.elements.modal

import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList

data class ShoppingItemModalSheetUIState(
    val loading: Boolean = true,
    val item: ShoppingItem = ShoppingItem.default(),
    val list: ShoppingList? = null,
    val dismissed: Boolean = false
)
