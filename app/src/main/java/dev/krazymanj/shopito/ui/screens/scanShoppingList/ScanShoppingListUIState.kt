package dev.krazymanj.shopito.ui.screens.scanShoppingList

import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList

data class ScanShoppingListUIState(
    val isLoading: Boolean = true,
    val shoppingList: ShoppingList? = null,

    val isScanning: Boolean = false,
    val scanError: Int? = null,
    val scannedItems: List<ShoppingItem> = emptyList(),

    val hasAddedItems: Boolean = false,
)
