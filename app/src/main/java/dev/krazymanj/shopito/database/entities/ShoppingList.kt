package dev.krazymanj.shopito.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list")
data class ShoppingList(
    var name: String,
    var description: String,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
) {
    fun isDefault(): Boolean = this == ShoppingList("","")
}
