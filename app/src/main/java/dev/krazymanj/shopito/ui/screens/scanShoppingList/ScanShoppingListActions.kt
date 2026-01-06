package dev.krazymanj.shopito.ui.screens.scanShoppingList

import android.graphics.Bitmap

interface ScanShoppingListActions {
    fun loadShoppingListData(shoppingListId: Long)
    fun onImageCaptured(bitmap: Bitmap)
    fun addScannedItemsToShoppingList()
}