package dev.krazymanj.shopito.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "shopping_list")
data class ShoppingList(
    var name: String,
    var description: String,

    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),

    val updatedAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false,
    val isDirty: Boolean = false,
    val isDeleted: Boolean = false
) {
    companion object {
        fun default(): ShoppingList = ShoppingList("","")
    }

    fun withUpdatedAt(): ShoppingList = this.copy(
        updatedAt = System.currentTimeMillis(),
        isDirty = true
    )
}
