package dev.krazymanj.shopito.ui.screens.itemKeywordsList

import dev.krazymanj.shopito.database.entities.ItemKeyword

interface ItemKeywordsListActions {
    fun loadItemKeywords()
    fun deleteItemKeyword(itemKeyword: ItemKeyword)
}