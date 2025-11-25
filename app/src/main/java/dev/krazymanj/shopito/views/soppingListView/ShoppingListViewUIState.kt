package dev.krazymanj.shopito.views.soppingListView

import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList


data class ShoppingListViewUIState(
    val shoppingList: ShoppingList? = null,
    val shoppingItems: List<ShoppingItem> = emptyList(),

    val itemInput: String = "",
    val dateInput: Long? = null,
    val latitudeInput: Double? = null,
    val longitudeInput: Double? = null,

    val isCreated: Boolean = false,
)
