package dev.krazymanj.shopito.views.itemKeywordsList

import dev.krazymanj.shopito.database.entities.ItemKeyword

interface ItemKeywordsListActions {
    fun loadItemKeywords()
    fun deleteItemKeyword(itemKeyword: ItemKeyword)
}