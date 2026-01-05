package dev.krazymanj.shopito.ui.screens.scanShoppingList

interface ScanShoppingListActions {
    fun loadShoppingListData(shoppingListId: Long)
    fun onTextScanned(text: String)
    fun addScannedItemsToShoppingList()
}