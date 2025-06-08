package dev.krazymanj.shopito.views.itemKeywordsList

import dev.krazymanj.shopito.database.entities.ItemKeyword


data class ItemKeywordsListUIState(
    val itemKeywords: List<ItemKeyword> = emptyList()
)
