package dev.krazymanj.shopito.ui.screens.scanShoppingList

import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.extension.empty
import dev.krazymanj.shopito.extension.extractLastAmount

data class ScanShoppingListUIState(
    val isLoading: Boolean = true,
    val shoppingList: ShoppingList? = null,
    val scannedText: String = String.empty,
    val hasAddedItems: Boolean = false
) {
    fun scannedTextToShoppingItems(): List<ShoppingItem> = scannedText.lines().map {
        val (name, amount) = it.extractLastAmount()
        ShoppingItem(
            listId = shoppingList?.id,
            itemName = name,
            amount = amount,
        )
    }
}
