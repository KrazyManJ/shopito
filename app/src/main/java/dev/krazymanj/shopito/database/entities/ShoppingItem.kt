package dev.krazymanj.shopito.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.krazymanj.shopito.model.Location
import java.util.UUID

@Entity(
    tableName = "shopping_item",
    foreignKeys = [
        ForeignKey(
            entity = ShoppingList::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("listId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ShoppingItem(
    var itemName: String,
    var amount: Int,

    var isDone: Boolean = false,
    var buyTime: Long? = null,

    @Embedded val location: Location? = null,

    @ColumnInfo(index = true)
    var listId: String? = null,

    @PrimaryKey
    var id: String = UUID.randomUUID().toString()
) {
    companion object {
        fun default(): ShoppingItem = ShoppingItem("",0)
    }
}
