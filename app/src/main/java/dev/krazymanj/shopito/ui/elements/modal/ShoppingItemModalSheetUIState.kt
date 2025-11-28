package dev.krazymanj.shopito.ui.elements.modal

import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.model.SavedLocation

data class ShoppingItemModalSheetUIState(
    val loading: Boolean = true,
    val item: ShoppingItem = ShoppingItem.default(),
    val list: ShoppingList? = null,
    val placesOptions: Set<SavedLocation> = linkedSetOf(),
    val dismissed: Boolean = false
)
