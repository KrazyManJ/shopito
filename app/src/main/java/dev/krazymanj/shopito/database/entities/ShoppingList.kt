package dev.krazymanj.shopito.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "shopping_list")
data class ShoppingList(
    var name: String,
    var description: String,

    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
) {
    companion object {
        fun default(): ShoppingList = ShoppingList("","")
    }
}
