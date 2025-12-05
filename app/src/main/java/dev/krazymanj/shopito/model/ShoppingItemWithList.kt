package dev.krazymanj.shopito.model

import androidx.room.Embedded
import androidx.room.Relation
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList

data class ShoppingItemWithList(
    @Embedded val item: ShoppingItem,
    @Relation(
        parentColumn = "listId",
        entityColumn = "id"
    )
    val list: ShoppingList?
)