package dev.krazymanj.shopito.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_item")
data class ShoppingItem(
    // Variables here

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
)
